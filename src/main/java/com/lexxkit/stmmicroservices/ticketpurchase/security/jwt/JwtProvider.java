package com.lexxkit.stmmicroservices.ticketpurchase.security.jwt;

import com.lexxkit.stmmicroservices.ticketpurchase.security.MyUserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Class to generate and validate access and refresh tokens.
 */
@Slf4j
@Component
public class JwtProvider {

  private final SecretKey jwtAccessSecret;
  private final SecretKey jwtRefreshSecret;
  @Value("${jwt.access.expiration.minutes}")
  private long expirationInMinutes;
  @Value("${jwt.refresh.expiration.days}")
  private long expirationInDays;

  public JwtProvider(
      @Value("${jwt.secret.access}") String jwtAccessSecret,
      @Value("${jwt.secret.refresh}") String jwtRefreshSecret) {
    this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
    this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
  }

  public String generateAccessToken(@NonNull MyUserPrincipal userPrincipal) {
    final LocalDateTime now = LocalDateTime.now();
    final Instant accessExpirationInstant = now.plusMinutes(expirationInMinutes)
        .atZone(ZoneId.systemDefault()).toInstant();
    final Date accessExpiration = Date.from(accessExpirationInstant);
    return Jwts.builder()
        .setSubject(String.valueOf(userPrincipal.getUser().getId()))
        .setExpiration(accessExpiration)
        .signWith(jwtAccessSecret)
        .claim("username", userPrincipal.getUsername())
        .compact();
  }

  public String generateRefreshToken(@NonNull MyUserPrincipal userPrincipal) {
    final LocalDateTime now = LocalDateTime.now();
    final Instant refreshExpirationInstant = now.plusDays(expirationInDays)
        .atZone(ZoneId.systemDefault()).toInstant();
    final Date refreshExpiration = Date.from(refreshExpirationInstant);
    return Jwts.builder()
        .setSubject(String.valueOf(userPrincipal.getUser().getId()))
        .setExpiration(refreshExpiration)
        .claim("username", userPrincipal.getUsername())
        .signWith(jwtRefreshSecret)
        .compact();
  }

  public boolean validateAccessToken(@NonNull String accessToken) {
    return validateToken(accessToken, jwtAccessSecret);
  }

  public boolean validateRefreshToken(@NonNull String refreshToken) {
    return validateToken(refreshToken, jwtRefreshSecret);
  }

  private boolean validateToken(@NonNull String token, @NonNull Key secret) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(secret)
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (ExpiredJwtException expEx) {
      log.error("Token expired: {}", expEx.getMessage());
    } catch (UnsupportedJwtException unsEx) {
      log.error("Unsupported jwt: {}", unsEx.getMessage());
    } catch (MalformedJwtException mjEx) {
      log.error("Malformed jwt: {}", mjEx.getMessage());
    } catch (SignatureException sEx) {
      log.error("Invalid signature: {}", sEx.getMessage());
    } catch (IllegalArgumentException e) {
      log.error("Invalid token: {}", e.getMessage());
    }
    return false;
  }

  public Claims getAccessClaims(@NonNull String token) {
    return getClaims(token, jwtAccessSecret);
  }

  public Claims getRefreshClaims(@NonNull String token) {
    return getClaims(token, jwtRefreshSecret);
  }

  private Claims getClaims(@NonNull String token, @NonNull Key secret) {
    return Jwts.parserBuilder()
        .setSigningKey(secret)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
}

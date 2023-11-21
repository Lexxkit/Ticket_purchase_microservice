package com.lexxkit.stmmicroservices.ticketpurchase.security.filter;

import com.lexxkit.stmmicroservices.ticketpurchase.security.jwt.JwtAuthentication;
import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUtils {

  public static JwtAuthentication generate(Claims claims) {
    final JwtAuthentication jwtInfoToken = new JwtAuthentication();
    jwtInfoToken.setUsername(claims.get("username", String.class));
    jwtInfoToken.setUserId(Long.valueOf(claims.getSubject()));
    return jwtInfoToken;
  }
}

package com.lexxkit.stmmicroservices.ticketpurchase.service;

import com.lexxkit.stmmicroservices.ticketpurchase.dto.LoginRequestJwtDto;
import com.lexxkit.stmmicroservices.ticketpurchase.dto.LoginResponseJwtDto;
import com.lexxkit.stmmicroservices.ticketpurchase.dto.RegisterRequestDto;
import com.lexxkit.stmmicroservices.ticketpurchase.exception.AuthenticationException;
import com.lexxkit.stmmicroservices.ticketpurchase.model.User;
import com.lexxkit.stmmicroservices.ticketpurchase.repository.UserRepository;
import com.lexxkit.stmmicroservices.ticketpurchase.security.MyUserDetailsService;
import com.lexxkit.stmmicroservices.ticketpurchase.security.MyUserPrincipal;
import com.lexxkit.stmmicroservices.ticketpurchase.security.jwt.JwtAuthentication;
import com.lexxkit.stmmicroservices.ticketpurchase.security.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

  private final MyUserDetailsService manager;
  private final PasswordEncoder encoder;
  private final UserRepository userRepository;
  private final JwtProvider jwtProvider;

  //Temporary storage, better to use REDIS instead
  private final Map<Long, String> refreshTokenStorage = new HashMap<>();

  public boolean register(RegisterRequestDto registerRequestDto) {
    Optional<User> userByLogin = userRepository.findUserByLogin(registerRequestDto.getLogin());
    if (userByLogin.isPresent()) {
      log.info("User with login: '{}' already exists!", registerRequestDto.getLogin());
      return false;
    }
    User newUser = User.builder()
        .login(registerRequestDto.getLogin())
        .passwordHash(encoder.encode(registerRequestDto.getPassword()))
        .name(registerRequestDto.getName())
        .surname(registerRequestDto.getSurname())
        .patronymicName(registerRequestDto.getPatronymicName())
        .build();
    userRepository.save(newUser);
    return true;
  }

  public LoginResponseJwtDto login(LoginRequestJwtDto loginRequestJwtDto) {
    final MyUserPrincipal userPrincipal = manager.loadUserByUsername(loginRequestJwtDto.getLogin());
    String password = userPrincipal.getPassword();

    if (!encoder.matches(loginRequestJwtDto.getPassword(), password)) {
      throw new AuthenticationException("Invalid password");
    }

    final String accessToken = jwtProvider.generateAccessToken(userPrincipal);
    final String refreshToken = jwtProvider.generateRefreshToken(userPrincipal);

    refreshTokenStorage.put(userPrincipal.getUser().getId(), refreshToken);

    return new LoginResponseJwtDto(
        userPrincipal.getUser().getId(),
        accessToken,
        refreshToken
    );
  }

  public LoginResponseJwtDto getAccessToken(String refreshToken) {
    if (jwtProvider.validateRefreshToken(refreshToken)) {
      final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
      final long userId = Long.parseLong(claims.getSubject());
      final String username = claims.get("username", String.class);
      final String savedRefreshToken = refreshTokenStorage.get(userId);
      if (savedRefreshToken != null && savedRefreshToken.equals(refreshToken)) {
        final MyUserPrincipal userPrincipal = manager.loadUserByUsername(username);
        final String accessToken = jwtProvider.generateAccessToken(userPrincipal);
        return new LoginResponseJwtDto(userId, accessToken, null);
      }
    }
    return new LoginResponseJwtDto(0, null, null);
  }

  public LoginResponseJwtDto getNewRefreshToken(String refreshToken) {
    if (jwtProvider.validateRefreshToken(refreshToken)) {
      final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
      final Long userId = Long.valueOf(claims.getSubject());
      final String username = claims.get("username", String.class);
      final String savedRefreshToken = refreshTokenStorage.get(userId);
      if (savedRefreshToken != null && savedRefreshToken.equals(refreshToken)) {
        final MyUserPrincipal userPrincipal = manager.loadUserByUsername(username);
        final String accessToken = jwtProvider.generateAccessToken(userPrincipal);
        final String newRefreshToken = jwtProvider.generateRefreshToken(userPrincipal);
        refreshTokenStorage.put(userId, newRefreshToken);
        return new LoginResponseJwtDto(userId, accessToken, newRefreshToken);
      }
    }
    throw new AuthenticationException("Invalid JWT token");
  }

  public JwtAuthentication getAuthInfo() {
    return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
  }

}

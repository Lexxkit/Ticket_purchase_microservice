package com.lexxkit.stmmicroservices.ticketpurchase.security.filter;

import com.lexxkit.stmmicroservices.ticketpurchase.security.jwt.JwtAuthentication;
import com.lexxkit.stmmicroservices.ticketpurchase.security.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

  private static final String BEARER_PREFIX = "Bearer ";

  private final JwtProvider jwtProvider;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain filterChain) throws IOException, ServletException {
    final String token = getTokenFromRequest((HttpServletRequest) request);

    if (token != null && jwtProvider.validateAccessToken(token)) {
      final Claims claims = jwtProvider.getAccessClaims(token);
      JwtAuthentication jwtAuth = JwtUtils.generate(claims);
      jwtAuth.setAuthenticated(true);
      SecurityContextHolder.getContext().setAuthentication(jwtAuth);
    }
    filterChain.doFilter(request, response);
  }

  private String getTokenFromRequest(HttpServletRequest request) {
    String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (StringUtils.hasText(bearer) && bearer.startsWith(BEARER_PREFIX)) {
      return bearer.substring(7);
    }
    return null;
  }
}

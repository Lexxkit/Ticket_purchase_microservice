package com.lexxkit.stmmicroservices.ticketpurchase.security;

import com.lexxkit.stmmicroservices.ticketpurchase.security.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final JwtFilter jwtFilter;
  private static final String[] AUTH_WHITELIST = {
      "/swagger-ui/**",
      "/swagger-ui/index.html",
      "/v3/api-docs/**",
      "/webjars/**",
      "/api/auth/login",
      "/api/auth/register",
      "/api/auth/token",
      "/error/**"
  };

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .httpBasic(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authz ->
        authz
            .requestMatchers(AUTH_WHITELIST).permitAll()
            .requestMatchers(HttpMethod.GET, "/api/tickets/**").permitAll()
            //.requestMatchers(HttpMethod.POST, "/api/tickets/**").authenticated()
            .anyRequest().authenticated()
        );

    http.addFilterBefore(jwtFilter,
        UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public PasswordEncoder getPassEnc() {
    return new BCryptPasswordEncoder();
  }
}

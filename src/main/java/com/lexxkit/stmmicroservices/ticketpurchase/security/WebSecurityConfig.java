package com.lexxkit.stmmicroservices.ticketpurchase.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

  private static final String[] AUTH_WHITELIST = {
      "/swagger-ui/**",
      "/swagger-ui/index.html",
      "/v3/api-docs/**",
      "/webjars/**",
      "/api/users/login",
      "/api/users/register",
      "/error/**"
  };

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .httpBasic(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests((authz) ->
        authz
            .requestMatchers(AUTH_WHITELIST).permitAll()
            .requestMatchers(HttpMethod.POST, "/api/tickets/**").authenticated()
            .requestMatchers(HttpMethod.GET, "/api/tickets/**").permitAll()
        );

    return http.build();
  }

  @Bean
  public PasswordEncoder getPassEnc() {
    return new BCryptPasswordEncoder();
  }
}

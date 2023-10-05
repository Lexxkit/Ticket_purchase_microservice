package com.lexxkit.stmmicroservices.ticketpurchase.controller;

import com.lexxkit.stmmicroservices.ticketpurchase.dto.LoginRequestJwtDto;
import com.lexxkit.stmmicroservices.ticketpurchase.dto.LoginResponseJwtDto;
import com.lexxkit.stmmicroservices.ticketpurchase.dto.RefreshJwtRequestDto;
import com.lexxkit.stmmicroservices.ticketpurchase.dto.RegisterRequestDto;
import com.lexxkit.stmmicroservices.ticketpurchase.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
    if (authService.register(registerRequestDto)) {
      return ResponseEntity.ok().build();
    }else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @PostMapping("/login")
  public LoginResponseJwtDto login(@Valid @RequestBody LoginRequestJwtDto loginRequestJwtDto) {
    return authService.login(loginRequestJwtDto);
  }

  @PostMapping("/token")
  public LoginResponseJwtDto getNewAccessToken(@Valid @RequestBody RefreshJwtRequestDto request) {
    return authService.getAccessToken(request.getRefreshToken());
  }

  @PostMapping("/refresh")
  public LoginResponseJwtDto getNewRefreshToken(@Valid @RequestBody RefreshJwtRequestDto request) {
    return authService.getNewRefreshToken(request.getRefreshToken());
  }
}

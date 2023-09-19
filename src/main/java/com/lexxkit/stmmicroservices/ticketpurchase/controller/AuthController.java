package com.lexxkit.stmmicroservices.ticketpurchase.controller;

import com.lexxkit.stmmicroservices.ticketpurchase.dto.LoginUserDto;
import com.lexxkit.stmmicroservices.ticketpurchase.dto.RegisterUserDto;
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
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<?> register(@Valid @RequestBody RegisterUserDto registerUserDto) {
    if (authService.register(registerUserDto)) {
      return ResponseEntity.ok().build();
    }else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginUserDto loginUserDto) {
    if (authService.login(loginUserDto)) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
  }
}

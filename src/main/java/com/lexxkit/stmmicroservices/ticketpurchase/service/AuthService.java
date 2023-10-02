package com.lexxkit.stmmicroservices.ticketpurchase.service;

import com.lexxkit.stmmicroservices.ticketpurchase.dto.LoginRequestJwtDto;
import com.lexxkit.stmmicroservices.ticketpurchase.dto.RegisterRequestDto;
import com.lexxkit.stmmicroservices.ticketpurchase.model.User;
import com.lexxkit.stmmicroservices.ticketpurchase.repository.UserRepository;
import com.lexxkit.stmmicroservices.ticketpurchase.security.MyUserDetailsService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

  private final MyUserDetailsService manager;
  private final PasswordEncoder encoder;
  private final UserRepository userRepository;

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

  public boolean login(LoginRequestJwtDto loginRequestJwtDto) {
    UserDetails userDetails = manager.loadUserByUsername(loginRequestJwtDto.getLogin());
    String password = userDetails.getPassword();

    return encoder.matches(loginRequestJwtDto.getPassword(), password);
  }
}

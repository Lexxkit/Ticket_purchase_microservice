package com.lexxkit.stmmicroservices.ticketpurchase.service;

import com.lexxkit.stmmicroservices.ticketpurchase.dto.LoginUserDto;
import com.lexxkit.stmmicroservices.ticketpurchase.dto.RegisterUserDto;
import com.lexxkit.stmmicroservices.ticketpurchase.exception.UserNotFoundException;
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

  public boolean register(RegisterUserDto registerUserDto) {
    Optional<User> userByLogin = userRepository.findUserByLogin(registerUserDto.getLogin());
    if (userByLogin.isPresent()) {
      log.info("User with login: '{}' already exists!", registerUserDto.getLogin());
      return false;
    }
    User newUser = User.builder()
        .login(registerUserDto.getLogin())
        .passwordHash(encoder.encode(registerUserDto.getPassword()))
        .name(registerUserDto.getName())
        .surname(registerUserDto.getSurname())
        .patronymicName(registerUserDto.getPatronymicName())
        .build();
    userRepository.save(newUser);
    return true;
  }

  public boolean login(LoginUserDto loginUserDto) {
    UserDetails userDetails = manager.loadUserByUsername(loginUserDto.getLogin());
    String password = userDetails.getPassword();

    return encoder.matches(loginUserDto.getPassword(), password);
  }
}

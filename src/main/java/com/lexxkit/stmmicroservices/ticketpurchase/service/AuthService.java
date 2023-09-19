package com.lexxkit.stmmicroservices.ticketpurchase.service;

import com.lexxkit.stmmicroservices.ticketpurchase.dto.LoginUserDto;
import com.lexxkit.stmmicroservices.ticketpurchase.dto.RegisterUserDto;
import com.lexxkit.stmmicroservices.ticketpurchase.exception.UserNotFoundException;
import com.lexxkit.stmmicroservices.ticketpurchase.model.User;
import com.lexxkit.stmmicroservices.ticketpurchase.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;

  public boolean register(RegisterUserDto registerUserDto) {
    Optional<User> userByLogin = userRepository.findUserByLogin(registerUserDto.getLogin());
    if (userByLogin.isPresent()) {
      log.info("User with login: '{}' already exists!", registerUserDto.getLogin());
      return false;
    }
    User newUser = User.builder()
        .login(registerUserDto.getLogin())
        .passwordHash(registerUserDto.getPassword())
        .name(registerUserDto.getName())
        .surname(registerUserDto.getSurname())
        .patronymicName(registerUserDto.getPatronymicName())
        .build();
    userRepository.save(newUser);
    return true;
  }

  public boolean login(LoginUserDto loginUserDto) {
    User userByLogin = userRepository.findUserByLogin(loginUserDto.getLogin())
        .orElseThrow(UserNotFoundException::new);
    return userByLogin.getPasswordHash().equals(loginUserDto.getPassword());
  }
}

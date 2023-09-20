package com.lexxkit.stmmicroservices.ticketpurchase.security;

import com.lexxkit.stmmicroservices.ticketpurchase.exception.UserNotFoundException;
import com.lexxkit.stmmicroservices.ticketpurchase.model.User;
import com.lexxkit.stmmicroservices.ticketpurchase.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

  private final MyUserPrincipal userPrincipal;
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findUserByLogin(username).orElseThrow(UserNotFoundException::new);
    userPrincipal.setUser(user);
    return userPrincipal;
  }
}

package com.lexxkit.stmmicroservices.ticketpurchase.repository;

import com.lexxkit.stmmicroservices.ticketpurchase.model.User;
import java.util.Optional;

public interface UserRepository {

  Optional<User> findUserByLogin(String login);

  int save(User newUser);
}

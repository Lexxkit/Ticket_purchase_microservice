package com.lexxkit.stmmicroservices.ticketpurchase.repository.impl;

import com.lexxkit.stmmicroservices.ticketpurchase.model.User;
import com.lexxkit.stmmicroservices.ticketpurchase.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

  private final JdbcTemplate jdbcTemplate;

  @Override
  public Optional<User> findUserByLogin(String login) {
    return jdbcTemplate.queryForStream(
        "select * from users where login = ?",
        new BeanPropertyRowMapper<>(User.class),
        login
    ).findAny();
  }

  @Override
  public int save(User newUser) {
    return jdbcTemplate.update(
        "insert into users (login, password_hash, name, surname, patronymic_name) values (?,?,?,?,?)",
        newUser.getLogin(), newUser.getPasswordHash(), newUser.getName(),
        newUser.getSurname(), newUser.getPatronymicName()
    );
  }
}

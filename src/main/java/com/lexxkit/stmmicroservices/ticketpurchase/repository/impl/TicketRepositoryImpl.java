package com.lexxkit.stmmicroservices.ticketpurchase.repository.impl;

import com.lexxkit.stmmicroservices.ticketpurchase.model.Ticket;
import com.lexxkit.stmmicroservices.ticketpurchase.repository.TicketRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TicketRepositoryImpl implements TicketRepository {

  private final JdbcTemplate jdbcTemplate;

  /**
   * Find all tickets that is available and with dateTime greater than now.
   *
   * @return List of available tickets.
   */
  @Override
  public List<Ticket> findAllAvailable() {
    return jdbcTemplate.query(
        "select * from tickets where is_available = true AND date_time > NOW()",
        new BeanPropertyRowMapper<>(Ticket.class)
    );
  }

  /**
   * Find {@link Ticket} by its id.
   *
   * @param id of ticket
   * @return optional of search result
   */
  @Override
  public Optional<Ticket> findById(long id) {
    return jdbcTemplate.queryForStream(
        "select * from tickets where id = ?",
        new BeanPropertyRowMapper<>(Ticket.class),
        id
    ).findAny();
  }
}

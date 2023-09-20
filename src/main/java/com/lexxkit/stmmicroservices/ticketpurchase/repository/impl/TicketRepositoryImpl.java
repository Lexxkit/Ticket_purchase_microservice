package com.lexxkit.stmmicroservices.ticketpurchase.repository.impl;

import com.lexxkit.stmmicroservices.ticketpurchase.model.Ticket;
import com.lexxkit.stmmicroservices.ticketpurchase.repository.TicketRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TicketRepositoryImpl implements TicketRepository {

  private final JdbcTemplate jdbcTemplate;
  private final RowMapper<Ticket> rowMapper;

  /**
   * Find all tickets that is available and with dateTime greater than now.
   *
   * @return List of available tickets.
   */
  @Override
  public List<Ticket> findAllAvailable() {
    return jdbcTemplate.query(
        "select t.*, r.id as r_id, r.start_point, r.end_point, c.id as c_id, c.title from tickets t "
            + "left join routes r on t.route_id = r.id "
            + "left join carriers c on r.carrier_id = c.id "
            + "where t.is_available = true AND t.date_time > NOW()",
        rowMapper
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
        "select id, date_time, seat_number, price, route_id, is_available from tickets where id = ?",
        new BeanPropertyRowMapper<>(Ticket.class),
        id
    ).findAny();
  }

  @Override
  public int update(Ticket ticket) {
    return jdbcTemplate.update(
        "update tickets set is_available = ? where id = ?",
        ticket.getIsAvailable(), ticket.getId()
    );
  }
}

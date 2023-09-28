package com.lexxkit.stmmicroservices.ticketpurchase.repository.impl;

import com.lexxkit.stmmicroservices.ticketpurchase.model.Ticket;
import com.lexxkit.stmmicroservices.ticketpurchase.repository.TicketRepository;
import com.lexxkit.stmmicroservices.ticketpurchase.util.Page;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TicketRepositoryImpl implements TicketRepository {

  private static final String BASE_FIND_ALL_SELECT_QUERY = "select "
      + "t.*, r.id as r_id, r.start_point, r.end_point, c.id as c_id, c.title from tickets t "
      + "left join routes r on t.route_id = r.id "
      + "left join carriers c on r.carrier_id = c.id "
      + "where t.is_available = true AND t.date_time > NOW()";
  private static final String DATE_WHERE_FILTER = "AND t.date_time = :dateTime";
  private static final String POINT_WHERE_FILTER = "AND (LOWER(r.start_point) LIKE LOWER(:startEndPoint)"
      + " OR LOWER(r.end_point) LIKE LOWER(:startEndPoint))";
  private static final String CARRIER_TITLE_WHERE_FILTER = "AND LOWER(c.title) LIKE LOWER(:title)";
  private static final String ORDER_LIMIT_OFFSET = "order by t.id limit :limit offset :offset";


  private final NamedParameterJdbcTemplate npjTemplate;
  private final JdbcTemplate jdbcTemplate;
  private final RowMapper<Ticket> rowMapper;

  /**
   * Find all tickets that is available and with dateTime greater than now.
   *
   * @return List of available tickets.
   */
  @Override
  public List<Ticket> findAllAvailable(Page page, LocalDateTime filterDateTime,
      String filterStartOrEndPoint, String filterCarrierTitle) {
    StringBuilder finalSqlQuery = new StringBuilder(BASE_FIND_ALL_SELECT_QUERY).append(" ");
    MapSqlParameterSource parameterSource = new MapSqlParameterSource();
    if (filterDateTime != null) {
      finalSqlQuery.append(DATE_WHERE_FILTER).append(" ");
      parameterSource.addValue("dateTime", filterDateTime);
    }
    if (filterStartOrEndPoint != null && !filterStartOrEndPoint.isBlank()) {
      finalSqlQuery.append(POINT_WHERE_FILTER).append(" ");
      parameterSource.addValue("startEndPoint", "%" + filterStartOrEndPoint + "%");
    }
    if (filterCarrierTitle != null && !filterCarrierTitle.isBlank()) {
      finalSqlQuery.append(CARRIER_TITLE_WHERE_FILTER).append(" ");
      parameterSource.addValue("title", "%" + filterCarrierTitle + "%");
    }
    finalSqlQuery.append(ORDER_LIMIT_OFFSET);
    parameterSource.addValue("limit", page.getLimit());
    parameterSource.addValue("offset", page.getOffset());
    return npjTemplate.query(finalSqlQuery.toString(), parameterSource, rowMapper);
  }
//  "select t.*, r.id as r_id, r.start_point, r.end_point, c.id as c_id, c.title from tickets t "
//      + "left join routes r on t.route_id = r.id "
//      + "left join carriers c on r.carrier_id = c.id "
//      + "where t.is_available = true AND t.date_time > NOW() "
//      + "order by t.id limit ? offset ?"
//  jdbcTemplate.query( finalSqlQuery.toString(),
//  rowMapper, page.getLimit(), page.getOffset()
//      );

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
        "update tickets set is_available = ?, user_id = ? where id = ?",
        ticket.getIsAvailable(), ticket.getUser().getId(), ticket.getId()
    );
  }

  @Override
  public List<Ticket> findTicketsForUser(long id) {
    return jdbcTemplate.query(
        "select t.*, r.id as r_id, r.start_point, r.end_point, c.id as c_id, c.title from tickets t "
            + "left join routes r on t.route_id = r.id "
            + "left join carriers c on r.carrier_id = c.id "
            + "where user_id = ? order by t.date_time ASC",
        rowMapper, id
    );
  }
}

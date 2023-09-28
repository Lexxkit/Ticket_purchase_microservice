package com.lexxkit.stmmicroservices.ticketpurchase.repository.impl;

import static com.lexxkit.stmmicroservices.ticketpurchase.constant.TicketSqlQueries.*;

import com.lexxkit.stmmicroservices.ticketpurchase.model.Ticket;
import com.lexxkit.stmmicroservices.ticketpurchase.repository.TicketRepository;
import com.lexxkit.stmmicroservices.ticketpurchase.util.Page;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TicketRepositoryImpl implements TicketRepository {

  private final NamedParameterJdbcTemplate npjTemplate;
  private final RowMapper<Ticket> rowMapper;

  /**
   * Find all tickets that is available and with dateTime greater than now.
   *
   * @return List of available tickets.
   */
  @Override
  public List<Ticket> findAllAvailable(Page page, LocalDateTime filterDateTime,
      String filterStartOrEndPoint, String filterCarrierTitle) {

    StringBuilder finalSqlQuery = new StringBuilder(SELECT_JOIN_TABLES_BASE_QUERY)
        .append(" ").append(AVAILABLE_WHERE_FILTER).append(" ");
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
    parameterSource.addValue("limit", page.getLimit())
        .addValue("offset", page.getOffset());

    return npjTemplate.query(finalSqlQuery.toString(), parameterSource, rowMapper);
  }

  /**
   * Find {@link Ticket} by its id.
   *
   * @param id of ticket
   * @return optional of search result
   */
  @Override
  public Optional<Ticket> findById(long id) {
    return npjTemplate.queryForStream(
        SELECT_TICKET_BY_ID,
        new MapSqlParameterSource("id", id),
        new BeanPropertyRowMapper<>(Ticket.class)
    ).findFirst();
  }

  @Override
  public int update(Ticket ticket) {
    MapSqlParameterSource parameterSource = new MapSqlParameterSource();
    parameterSource
        .addValue("id", ticket.getId())
        .addValue("user_id", ticket.getUser().getId())
        .addValue("is_available", ticket.getIsAvailable());

    return npjTemplate.update(UPDATE_TICKET_QUERY, parameterSource);
  }

  @Override
  public List<Ticket> findTicketsForUser(long id) {
    StringBuilder finalSqlQuery = new StringBuilder(SELECT_JOIN_TABLES_BASE_QUERY)
        .append(" ").append(USER_WHERE_FILTER);

    return npjTemplate.query(
        finalSqlQuery.toString(),
        new MapSqlParameterSource("user_id", id),
        rowMapper
    );
  }
}

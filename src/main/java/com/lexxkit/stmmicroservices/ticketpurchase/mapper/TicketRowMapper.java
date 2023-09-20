package com.lexxkit.stmmicroservices.ticketpurchase.mapper;

import com.lexxkit.stmmicroservices.ticketpurchase.model.Carrier;
import com.lexxkit.stmmicroservices.ticketpurchase.model.Route;
import com.lexxkit.stmmicroservices.ticketpurchase.model.Ticket;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TicketRowMapper implements RowMapper<Ticket> {

  @Override
  public Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {
    Ticket ticket = new Ticket();
    Route route = new Route();
    Carrier carrier = new Carrier();

    carrier.setId(rs.getLong("c_id"));
    carrier.setTitle(rs.getString("title"));

    route.setId(rs.getLong("r_id"));
    route.setStartPoint(rs.getString("start_point"));
    route.setEndPoint(rs.getString("end_point"));
    route.setCarrier(carrier);

    ticket.setId(rs.getLong("id"));
    ticket.setPrice(rs.getBigDecimal("price"));
    ticket.setSeatNumber(rs.getInt("seat_number"));
    ticket.setIsAvailable(rs.getBoolean("is_available"));
    ticket.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
    ticket.setRoute(route);

    log.info(carrier.toString());
    log.info(route.toString());
    log.info(ticket.toString());
    return ticket;
  }
}

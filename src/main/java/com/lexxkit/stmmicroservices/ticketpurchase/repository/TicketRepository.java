package com.lexxkit.stmmicroservices.ticketpurchase.repository;

import com.lexxkit.stmmicroservices.ticketpurchase.model.Ticket;
import com.lexxkit.stmmicroservices.ticketpurchase.util.Page;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TicketRepository {

  List<Ticket> findAllAvailable(Page page, LocalDateTime filterDateTime,
      String filterStartOrEndPoint, String filterCarrierTitle);

  Optional<Ticket> findById(long id);

  int update(Ticket ticket);

  List<Ticket> findTicketsForUser(long id);
}

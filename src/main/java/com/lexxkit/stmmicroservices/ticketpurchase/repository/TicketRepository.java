package com.lexxkit.stmmicroservices.ticketpurchase.repository;

import com.lexxkit.stmmicroservices.ticketpurchase.model.Ticket;
import java.util.List;
import java.util.Optional;

public interface TicketRepository {

  List<Ticket> findAllAvailable();

  Optional<Ticket> findById(long id);
}

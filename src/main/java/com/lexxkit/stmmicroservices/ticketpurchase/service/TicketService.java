package com.lexxkit.stmmicroservices.ticketpurchase.service;

import com.lexxkit.stmmicroservices.ticketpurchase.dto.TicketDto;
import com.lexxkit.stmmicroservices.ticketpurchase.exception.TicketNotAvailableException;
import com.lexxkit.stmmicroservices.ticketpurchase.exception.TicketNotFoundException;
import com.lexxkit.stmmicroservices.ticketpurchase.mapper.TicketMapper;
import com.lexxkit.stmmicroservices.ticketpurchase.model.Ticket;
import com.lexxkit.stmmicroservices.ticketpurchase.repository.TicketRepository;
import com.lexxkit.stmmicroservices.ticketpurchase.util.Page;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {

  private final TicketRepository ticketRepository;
  private final TicketMapper ticketMapper;

  public List<TicketDto> getAllAvailableTickets(Page page) {
    return ticketMapper.toDtoList(ticketRepository.findAllAvailable(page));
  }

  public TicketDto getTicketById(long id) {
    log.info(ticketRepository.findById(id).toString());
    return ticketMapper.toDto(ticketRepository.findById(id).orElseThrow(
        () -> new TicketNotFoundException(String.format("There is no ticket with id = %d.", id))
    ));
  }

  public TicketDto buyTicket(long id) {
    Ticket ticket = ticketRepository.findById(id).orElseThrow(TicketNotFoundException::new);
    if (!ticket.getIsAvailable() || ticket.getDateTime().isBefore(LocalDateTime.now())) {
      log.info("Ticket with id={} is not available!", ticket.getId());
      throw new TicketNotAvailableException(String.format("Ticket with id = %d is not available!",ticket.getId()));
    }
    ticket.setIsAvailable(false);
    //todo: Add User-Ticket connection, update tickets table (and users table???)
    if (ticketRepository.update(ticket) == 0) {
      throw new TicketNotFoundException(String.format("There is no ticket with id = %d.", id));
    }
    return ticketMapper.toDto(ticket);
  }
}

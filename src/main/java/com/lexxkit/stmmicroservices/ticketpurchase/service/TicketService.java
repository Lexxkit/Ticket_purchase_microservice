package com.lexxkit.stmmicroservices.ticketpurchase.service;

import com.lexxkit.stmmicroservices.ticketpurchase.dto.TicketDto;
import com.lexxkit.stmmicroservices.ticketpurchase.exception.TicketNotAvailableException;
import com.lexxkit.stmmicroservices.ticketpurchase.exception.TicketNotFoundException;
import com.lexxkit.stmmicroservices.ticketpurchase.exception.UserNotFoundException;
import com.lexxkit.stmmicroservices.ticketpurchase.mapper.TicketMapper;
import com.lexxkit.stmmicroservices.ticketpurchase.model.Ticket;
import com.lexxkit.stmmicroservices.ticketpurchase.model.User;
import com.lexxkit.stmmicroservices.ticketpurchase.repository.TicketRepository;
import com.lexxkit.stmmicroservices.ticketpurchase.repository.UserRepository;
import com.lexxkit.stmmicroservices.ticketpurchase.util.Page;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {

  private final UserRepository userRepository;
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

  @Transactional
  public TicketDto buyTicket(long id, Authentication authentication) {
    Ticket ticket = ticketRepository.findById(id).orElseThrow(TicketNotFoundException::new);
    if (!ticket.getIsAvailable() || ticket.getDateTime().isBefore(LocalDateTime.now())) {
      log.info("Ticket with id={} is not available!", ticket.getId());
      throw new TicketNotAvailableException(String.format("Ticket with id = %d is not available!",ticket.getId()));
    }
    User user = userRepository.findUserByLogin(authentication.getName())
        .orElseThrow(UserNotFoundException::new);

    ticket.setIsAvailable(false);
    ticket.setUser(user);

    if (ticketRepository.update(ticket) == 0) {
      throw new TicketNotFoundException(String.format("There is no ticket with id = %d.", id));
    }
    return ticketMapper.toDto(ticket);
  }
}

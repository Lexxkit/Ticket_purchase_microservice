package com.lexxkit.stmmicroservices.ticketpurchase.service;

import com.lexxkit.stmmicroservices.ticketpurchase.dto.TicketDto;
import com.lexxkit.stmmicroservices.ticketpurchase.exception.TicketNotFoundException;
import com.lexxkit.stmmicroservices.ticketpurchase.mapper.TicketMapper;
import com.lexxkit.stmmicroservices.ticketpurchase.repository.TicketRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketService {

  private final TicketRepository ticketRepository;
  private final TicketMapper ticketMapper;

  public List<TicketDto> getAllAvailableTickets() {
    return ticketMapper.toDtoList(ticketRepository.findAllAvailable());
  }

  public TicketDto getTicketById(long id) {
    return ticketMapper.toDto(ticketRepository.findById(id).orElseThrow(TicketNotFoundException::new));
  }
}

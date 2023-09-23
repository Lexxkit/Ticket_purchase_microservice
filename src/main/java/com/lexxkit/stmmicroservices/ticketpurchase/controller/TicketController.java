package com.lexxkit.stmmicroservices.ticketpurchase.controller;

import com.lexxkit.stmmicroservices.ticketpurchase.dto.TicketDto;
import com.lexxkit.stmmicroservices.ticketpurchase.service.TicketService;
import com.lexxkit.stmmicroservices.ticketpurchase.util.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Tag(name = "Tickets API", description = "Find and purchase tickets")
public class TicketController {

  private final TicketService ticketService;

  @Operation(
      summary = "Get a list of all tickets",
      description = "Returns all available tickets",
      responses = {
          @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
          @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
      }
  )
  @GetMapping
  public List<TicketDto> getAllTickets(@RequestParam(value = "page", defaultValue = "1") long page,
                                       @RequestParam(value = "size", defaultValue = "10") long size) {
    return ticketService.getAllAvailableTickets(Page.of(page, size));
  }

  @GetMapping("/{id}")
  public TicketDto getTicketById(@PathVariable long id) {
    return ticketService.getTicketById(id);
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/{ticket_id}/purchase")
  public TicketDto buyTicket(@PathVariable(name = "ticket_id") long id,
                              Authentication authentication) {
    return ticketService.buyTicket(id, authentication);
  }
}

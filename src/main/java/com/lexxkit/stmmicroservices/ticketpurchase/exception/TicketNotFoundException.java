package com.lexxkit.stmmicroservices.ticketpurchase.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TicketNotFoundException extends RuntimeException {

  public TicketNotFoundException() {
    super();
  }

  public TicketNotFoundException(String message) {
    super(message);
  }
}

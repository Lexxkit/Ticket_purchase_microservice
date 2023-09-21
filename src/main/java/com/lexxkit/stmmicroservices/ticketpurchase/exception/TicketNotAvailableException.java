package com.lexxkit.stmmicroservices.ticketpurchase.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TicketNotAvailableException extends RuntimeException {

  public TicketNotAvailableException() {
    super();
  }

  public TicketNotAvailableException(String message) {
    super(message);
  }
}

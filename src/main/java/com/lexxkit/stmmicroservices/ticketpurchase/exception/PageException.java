package com.lexxkit.stmmicroservices.ticketpurchase.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PageException extends RuntimeException {

  public PageException() {
    super();
  }

  public PageException(String message) {
    super(message);
  }
}

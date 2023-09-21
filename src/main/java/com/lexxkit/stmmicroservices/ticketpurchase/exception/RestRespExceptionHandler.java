package com.lexxkit.stmmicroservices.ticketpurchase.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class RestRespExceptionHandler {

  @ExceptionHandler(value = { PageException.class, TicketNotAvailableException.class })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorMessage handleBadRequestException(RuntimeException ex, WebRequest req) {
    return new ErrorMessage(
        HttpStatus.BAD_REQUEST.value(),
        ex.getMessage(),
        req.getDescription(false)
    );
  }

  record ErrorMessage(int statusCode, String message, String description) {

  }
}

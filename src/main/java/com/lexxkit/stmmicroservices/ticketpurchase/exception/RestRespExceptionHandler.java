package com.lexxkit.stmmicroservices.ticketpurchase.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class RestRespExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = { PageException.class })
  protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
    String respBody = ex.getMessage();
    log.error("There is an exception: " + ex.getMessage());
    return handleExceptionInternal(ex, respBody,
        new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }
}

package com.lexxkit.stmmicroservices.ticketpurchase.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class RestRespExceptionHandler extends ResponseEntityExceptionHandler {

}

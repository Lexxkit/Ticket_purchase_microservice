package com.lexxkit.stmmicroservices.ticketpurchase.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Ticket {
  private long id;
  private LocalDateTime dateTime;
  private int seatNumber;
  private BigDecimal price;
  private Route route;
  private Boolean isAvailable;
  private User user;
}

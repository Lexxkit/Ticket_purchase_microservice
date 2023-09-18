package com.lexxkit.stmmicroservices.ticketpurchase.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TicketDto {
  private long id;
  private LocalDateTime dateTime;
  private int seatNumber;
  private BigDecimal price;
//  private String carrierTitle;
}

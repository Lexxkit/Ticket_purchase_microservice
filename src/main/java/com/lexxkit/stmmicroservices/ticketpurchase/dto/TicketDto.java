package com.lexxkit.stmmicroservices.ticketpurchase.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TicketDto {
  private long id;
  @NotBlank
  private LocalDateTime dateTime;
  @Min(value = 0, message = "Seat number cannot be negative.")
  private int seatNumber;
  @Min(value = 0, message = "Price cannot be negative.")
  private BigDecimal price;
//  private String carrierTitle;
}

package com.lexxkit.stmmicroservices.ticketpurchase.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FilterCriteriaDto {
  private LocalDateTime dateTime;
  private String startOrEndPoint;
  private String carrierTitle;
}

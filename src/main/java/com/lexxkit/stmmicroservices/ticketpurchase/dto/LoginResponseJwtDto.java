package com.lexxkit.stmmicroservices.ticketpurchase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseJwtDto {

  private final String type = "Bearer";
  private long userId;
  private String accessToken;
  private String refreshToken;

}

package com.lexxkit.stmmicroservices.ticketpurchase.dto;

import lombok.Data;

@Data
public class LoginResponseJwtDto {

  private final String type = "Bearer";
  private long userId;
  private String accessToken;
  private String refreshToken;

}

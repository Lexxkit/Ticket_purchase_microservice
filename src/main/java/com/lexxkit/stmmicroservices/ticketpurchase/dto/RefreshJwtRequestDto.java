package com.lexxkit.stmmicroservices.ticketpurchase.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshJwtRequestDto {

  @NotBlank(message = "Token should not be blank!")
  private String refreshToken;
}

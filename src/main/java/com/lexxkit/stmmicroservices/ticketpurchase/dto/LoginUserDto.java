package com.lexxkit.stmmicroservices.ticketpurchase.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginUserDto {
  @NotBlank
  private String login;
  @NotBlank
  private String password;
}

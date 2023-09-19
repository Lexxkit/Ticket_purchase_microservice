package com.lexxkit.stmmicroservices.ticketpurchase.dto;

import lombok.Data;

@Data
public class RegisterUserDto {
  private String login;
  private String password;
  private String name;
  private String surname;
  private String patronymicName;
}

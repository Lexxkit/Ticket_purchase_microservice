package com.lexxkit.stmmicroservices.ticketpurchase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
  private long id;
  private String login;
  private String passwordHash;
  private String name;
  private String surname;
  private String patronymicName;
}

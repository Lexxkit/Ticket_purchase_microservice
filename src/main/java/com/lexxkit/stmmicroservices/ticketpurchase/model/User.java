package com.lexxkit.stmmicroservices.ticketpurchase.model;

import lombok.Data;

@Data
public class User {
  private long id;
  private String login;
  private String passwordHash;
  private String name;
  private String surname;
  private String patronymicName;
}

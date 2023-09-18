package com.lexxkit.stmmicroservices.ticketpurchase.model;

import lombok.Data;

@Data
public class Route {
  private long id;
  private String startPoint;
  private String endPoint;
  private int durationInMinutes;
  private Carrier carrier;
}

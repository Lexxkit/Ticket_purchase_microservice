package com.lexxkit.stmmicroservices.ticketpurchase.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDto {
  @NotBlank(message = "Login cannot be blank.")
  private String login;
  @NotBlank(message = "Password cannot be blank.")
  @Size(min = 3, message = "The password must be at least 3 characters long.")
  private String password;
  @NotBlank(message = "Name cannot be blank.")
  private String name;
  @NotBlank(message = "Surname cannot be blank.")
  private String surname;
  @NotBlank(message = "Patronymic name cannot be blank.")
  private String patronymicName;
}

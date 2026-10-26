package com.portfolio.my_portfolio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
  private Long id;

  @NotBlank(message = "El usuario no puede estar vacío")
  @Size(max = 50, message = "El usuario no puede superar los 50 caracteres")
  private String username;

  @NotBlank(message = "La contraseña no puede estar vacía")
  @Size(max = 255, message = "La contraseña no puede superar los 255 caracteres")
  private String password;

  private boolean enabled;
}

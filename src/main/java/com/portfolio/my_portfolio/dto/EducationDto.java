package com.portfolio.my_portfolio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
public class EducationDto {
  private Long id;

  @NotBlank(message = "El título no puede estar vacío")
  @Size(max = 255, message = "El título no puede superar los 255 caracteres")
  private String degree;

  @NotBlank(message = "La institución no puede estar vacía")
  @Size(max = 255, message = "La institución no puede superar los 255 caracteres")
  private String institution;

  @NotNull(message = "La fecha de inicio no puede ser nula")
  @PastOrPresent(message = "La fecha de inicio no puede ser futura")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate startDate;

  @PastOrPresent(message = "La fecha de fin no puede ser futura")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate endDate;

  private String description;

  @NotNull(message = "El id de la información personal no puede ser nulo")
  private Long personalInfoId;
}

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
public class ExperienceDto {
  private Long id;

  @NotBlank(message = "El puesto de trabajo no puede estar vacío")
  @Size(max = 255, message = "El puesto de trabajo no puede superar los 255 caracteres")
  private String jobTitle;

  @NotBlank(message = "El nombre de la empresa no puede estar vacío")
  @Size(max = 255, message = "El nombre de la empresa no puede superar los 255 caracteres")
  private String companyName;

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

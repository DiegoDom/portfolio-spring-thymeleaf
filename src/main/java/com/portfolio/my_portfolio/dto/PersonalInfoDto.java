package com.portfolio.my_portfolio.dto;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonalInfoDto {
  private Long id;

  @NotBlank(message = "El nombre no puede estar vacío")
  @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
  private String firstName;

  @NotBlank(message = "Los apellidos no pueden estar vacíos")
  @Size(max = 100, message = "Los apellidos no pueden superar los 100 caracteres")
  private String lastName;

  @NotBlank(message = "El título profesional no puede estar vacío")
  @Size(max = 255, message = "El título profesional no puede superar los 255 caracteres")
  private String title;

  @NotBlank(message = "La descripción no puede estar vacía")
  private String profileDescription;

  @Size(max = 500, message = "La URL de la imagen de perfil no puede superar los 500 caracteres")
  private String profileImageUrl;

  @Min(value = 0, message = "Los años de experiencia no pueden ser negativos")
  private Integer yearsOfExperience;

  @NotBlank(message = "El correo electrónico no puede estar vacío")
  @Email(message = "El correo electrónico no es válido")
  @Size(max = 255, message = "El correo electrónico no puede superar los 255 caracteres")
  private String email;

  @NotBlank(message = "El teléfono no puede estar vacío")
  @Size(max = 50, message = "El teléfono no puede superar los 50 caracteres")
  private String phone;

  @URL(message = "La URL de LinkedIn no es válida")
  @Size(max = 500, message = "La URL de LinkedIn no puede superar los 500 caracteres")
  private String linkedinUrl;

  @URL(message = "La URL de GitHub no es válida")
  @Size(max = 500, message = "La URL de GitHub no puede superar los 500 caracteres")
  private String githubUrl;
}

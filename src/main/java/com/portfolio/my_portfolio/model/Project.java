package com.portfolio.my_portfolio.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    private Long id;

    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 255, message = "El título no puede superar los 255 caracteres")
    private String title;

    @NotBlank(message = "La descripción no puede estar vacía")
    private String description;

    @Size(max = 500, message = "La URL de la imagen no puede superar los 500 caracteres")
    private String imageUrl;

    @Size(max = 500, message = "La URL del proyecto no puede superar los 500 caracteres")
    private String projectUrl;

    @NotNull(message = "El id de la información personal no puede ser nulo")
    private Long personalInfoId;
}

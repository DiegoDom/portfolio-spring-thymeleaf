package com.portfolio.my_portfolio.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Skill {
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String name;

    @Min(value = 0, message = "El nivel no puede ser menor que 0")
    @Max(value = 100, message = "El nivel no puede ser mayor que 100")
    private Integer levelPercentage;

    @Size(max = 100, message = "La clase del icono no puede superar los 100 caracteres")
    private String iconClass;

    @NotNull(message = "El id de la información personal no puede ser nulo")
    private Long personalInfoId;
}

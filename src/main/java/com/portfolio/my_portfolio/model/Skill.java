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

    @NotBlank(message = "The name cannot be empty")
    @Size(max = 100, message = "The name cannot exceed 100 characters")
    private String name;

    @Min(value = 0, message = "The level percentage cannot be less than zero")
    @Max(value = 100, message = "The level percentage cannot be greater than 100")
    private Integer levelPercentage;

    @Size(max = 100, message = "The icon class cannot exceed 100 characters")
    private String iconClass;

    @NotNull(message = "The personal info id cannot be null")
    private Long personalInfoId;
}

package com.portfolio.my_portfolio.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Education {
    private Long id;

    @NotBlank(message = "The degree cannot be empty")
    @Size(max = 255, message = "The degree cannot exceed 255 characters")
    private String degree;

    @NotBlank(message = "The institution cannot be empty")
    @Size(max = 255, message = "The institution cannot exceed 255 characters")
    private String institution;

    @NotNull(message = "The start date cannot be null")
    @PastOrPresent(message = "The start date cannot be a future date")
    private LocalDate startDate;

    @PastOrPresent(message = "The end date cannot be a future date")
    private LocalDate endDate;

    private String description;

    @NotNull(message = "The personal info id cannot be null")
    private Long personalInfoId;
}

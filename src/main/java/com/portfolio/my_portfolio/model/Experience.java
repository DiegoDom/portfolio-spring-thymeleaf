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
public class Experience {
    private Long id;

    @NotBlank(message = "The job title cannot be empty")
    @Size(max = 255, message = "The job title cannot exceed 255 characters")
    private String jobTitle;

    @NotBlank(message = "The company name cannot be empty")
    @Size(max = 255, message = "The company name cannot exceed 255 characters")
    private String companyName;

    @NotNull(message = "The start date cannot be null")
    @PastOrPresent(message = "The start date cannot be a future date")
    private LocalDate startDate;

    @PastOrPresent(message = "The end date cannot be a future date")
    private LocalDate endDate;

    private String description;

    @NotNull(message = "The personal info id cannot be null")
    private Long personalInfoId;
}

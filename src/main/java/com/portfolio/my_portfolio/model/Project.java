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

    @NotBlank(message = "The title cannot be empty")
    @Size(max = 255, message = "The title cannot exceed 255 characters")
    private String title;

    @NotBlank(message = "The description cannot be empty")
    private String description;

    @Size(max = 500, message = "The image URL cannot exceed 500 characters")
    private String imageUrl;

    @Size(max = 500, message = "The project URL cannot exceed 500 characters")
    private String projectUrl;

    @NotNull(message = "The personal info id cannot be null")
    private Long personalInfoId;
}

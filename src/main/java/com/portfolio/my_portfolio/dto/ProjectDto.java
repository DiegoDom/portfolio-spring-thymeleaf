package com.portfolio.my_portfolio.dto;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectDto {
  private Long id;

  @NotBlank(message = "The title cannot be empty")
  @Size(max = 255, message = "The title cannot exceed 255 characters")
  private String title;

  @NotBlank(message = "The description cannot be empty")
  private String description;

  @URL(message = "The image URL cannot exceed 500 characters")
  private String imageUrl;

  @URL(message = "The project URL cannot exceed 500 characters")
  private String projectUrl;
}

package com.portfolio.my_portfolio.model;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalInfo {
    private Long id;

    @NotBlank(message = "The first name cannot be empty")
    @Size(max = 100, message = "The first name cannot exceed 100 characters")
    private String firstName;

    @NotBlank(message = "The last name cannot be empty")
    @Size(max = 100, message = "The last name cannot exceed 100 characters")
    private String lastName;

    @NotBlank(message = "The title cannot be empty")
    @Size(max = 255, message = "The title cannot exceed 255 characters")
    private String title;

    @NotBlank(message = "The description cannot be empty")
    private String profileDescription;

    @Size(max = 500, message = "The profile image URL cannot exceed 500 characters")
    private String profileImageUrl;

    @Min(value = 0, message = "The years of experience cannot be less than zero")
    private Integer yearsOfExperience;

    @NotBlank(message = "The email cannot be empty")
    @Email(message = "The email is not valid")
    @Size(max = 255, message = "The email cannot exceed 255 characters")
    private String email;

    @NotBlank(message = "The phone cannot be empty")
    @Size(max = 50, message = "The phone cannot exceed 50 characters")
    private String phone;

    @URL(message = "The LinkedIn URL is not valid")
    @Size(max = 500, message = "The LinkedIn URL cannot exceed 500 characters")
    private String linkedinUrl;

    @URL(message = "The GitHub URL is not valid")
    @Size(max = 500, message = "The GitHub URL cannot exceed 500 characters")
    private String githubUrl;
}

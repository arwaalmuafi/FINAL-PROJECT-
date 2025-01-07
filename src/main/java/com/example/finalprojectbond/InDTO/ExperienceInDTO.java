package com.example.finalprojectbond.InDTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Setter
@Getter
public class ExperienceInDTO {

    @Size(max = 120, message = "Title must be at most 120 characters")
    @NotEmpty(message = "Title cannot be empty")
    private String title;

    @Size(max = 500, message = "Description must be at most 500 characters")
    @NotEmpty(message = "Description cannot be empty")
    private String description;

    @Size(max = 30, message = "City must not exceed 30 characters")
    @NotEmpty(message = "City cannot be empty")
    private String city;

    @NotNull(message = "Start date cannot be null")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    @Future(message = "End date must be in the future")
    private LocalDate endDate;

    @Size(max = 15, message = "Difficulty must not exceed 15 characters")
    @NotEmpty(message = "Difficulty cannot be empty")
    @Pattern(regexp = "^(Beginner|Intermediate|Advanced|Expert)$")
    private String difficulty;

    @Pattern(regexp = "^(MALE|FEMALE|FAMILY)$")
    private String audienceType;

    public ExperienceInDTO(String title, String description, String city, LocalDate startDate, LocalDate endDate, String difficulty, String audienceType) {
        this.title = title;
        this.description = description;
        this.city = city;
        this.startDate = startDate;
        this.endDate = endDate;
        this.difficulty = difficulty;
        this.audienceType = audienceType;
    }
}

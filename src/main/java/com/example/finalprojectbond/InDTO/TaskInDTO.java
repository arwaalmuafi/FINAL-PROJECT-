package com.example.finalprojectbond.InDTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class TaskInDTO {
    @Size(max = 70,message = "The length of the title must be at most 70 characters")
    @NotEmpty(message = "Title cannot be null")
    private String title;

    @Size(max = 255,message = "The length of the description must be at most 255 characters")
    @NotEmpty(message = "Description cannot be null")
    private String description;



}

package com.example.finalprojectbond.OutDTO;

import com.example.finalprojectbond.Model.Explorer;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class TaskOutDTO {
    private String title;

    private String description;

    private String status;

}

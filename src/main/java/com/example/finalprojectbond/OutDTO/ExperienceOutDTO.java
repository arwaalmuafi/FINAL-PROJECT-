package com.example.finalprojectbond.OutDTO;

import com.example.finalprojectbond.Model.ExperiencePhoto;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class ExperienceOutDTO {

    private String title;
    private String description;
    private String city;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private String difficulty;
    private String audienceType;
    private List<ExperiencePhotoOutDTO> photos;
    private List<TagOutDTO> tags;

    public ExperienceOutDTO(String title, String description, String city, @NotEmpty(message = "Status cannot be empty") @Pattern(regexp = "^(Accept Application|Fully Booked|Confirming|Task Assignment|In Progress|Active|Completed|Canceled)$") String status, LocalDate startDate, LocalDate endDate, String difficulty, @Pattern(regexp = "^(MALE|FEMALE|FAMILY)$") String audienceType) {

    }

    public ExperienceOutDTO(String s, String s1, String s2) {

    }
}

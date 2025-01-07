package com.example.finalprojectbond.OutDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Setter
@Getter
public class ExplorerApplicationOutDTO {

//    private ExperienceSearchOutDTO experience;
    private String ExperienceTitle;

    private LocalDate ExperienceStartDate;

    private LocalDate ExperienceEndDate;

    private String applicationStatus;


}

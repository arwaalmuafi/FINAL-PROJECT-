package com.example.finalprojectbond.OutDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Setter
@Getter
public class ReviewExplorerOutDTO {

    private String comment;

    private Integer rating;

    private LocalDateTime createdAt;

}

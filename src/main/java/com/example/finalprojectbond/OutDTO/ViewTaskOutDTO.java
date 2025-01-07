package com.example.finalprojectbond.OutDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class ViewTaskOutDTO {
    private String nameExplorer;

    private String title;

    private String description;

    private String status;

    private String photoURL;
}

package com.example.finalprojectbond.OutDTO;

import com.example.finalprojectbond.Model.Explorer;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class ApplicationStatusOutDTO {
private String description;

    private String tools;

    private String status;

    private Boolean meetingZone;

    private ExplorerStatusOutDTO explorerStatusOutDTO;
}

package com.example.finalprojectbond.OutDTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class ExplorerOutDTO {

    private String name;
    private Integer age;
    private String city;
    private String healthStatus;
    private String email;
    private String gender;
    private String phoneNumber;
    private String photoURL;
    private double rating;

    // Constructor that maps from Explorer
    public ExplorerOutDTO(String name, Integer age, String city, String healthStatus, String email, String gender, String phoneNumber, String photoURL, Double rating) {
        this.name = name;
        this.age = age;
        this.city = city;
        this.healthStatus = healthStatus;
        this.email = email;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.photoURL = photoURL;
        this.rating = rating;
    }

    // Getters and setters...
    public String getName() {
        return name;
    }

}

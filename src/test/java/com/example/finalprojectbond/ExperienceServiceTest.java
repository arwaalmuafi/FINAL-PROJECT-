package com.example.finalprojectbond;

import com.example.finalprojectbond.Api.ApiException;
import com.example.finalprojectbond.InDTO.ExperienceInDTO;
import com.example.finalprojectbond.Model.Experience;
import com.example.finalprojectbond.Model.MyUser;
import com.example.finalprojectbond.OutDTO.ExperienceOutDTO;
import com.example.finalprojectbond.OutDTO.ExperienceSearchOutDTO;
import com.example.finalprojectbond.Repository.AuthRepository;
import com.example.finalprojectbond.Repository.ExperienceRepository;
import com.example.finalprojectbond.Repository.OrganizerRepository;
import com.example.finalprojectbond.Service.ExperienceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ExperienceServiceTest {

    @Mock
    private ExperienceRepository experienceRepository;

    @Mock
    OrganizerRepository organizerRepository;

    @Mock
    private AuthRepository authRepository;

    @InjectMocks
    private ExperienceService experienceService;

    private MyUser testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new MyUser();
        testUser.setId(1);
        testUser.setName("John Doe");
    }




    @Test
    void testFindAll_InvalidUser_ThrowsApiException() {
        // Arrange
        when(authRepository.findMyUserById(1)).thenReturn(null);

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> experienceService.findAll(1));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testCreateExperience_OrganizerNotFound_ThrowsApiException() {
        // Arrange
        when(organizerRepository.findOrganizerById(1)).thenReturn(null);
        ExperienceInDTO experienceInDTO = new ExperienceInDTO("Test Experience", "Description", "City", null, null, null, "Male");

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> experienceService.createExperience(1, experienceInDTO));
        assertEquals("Organizer not found", exception.getMessage());
    }
}


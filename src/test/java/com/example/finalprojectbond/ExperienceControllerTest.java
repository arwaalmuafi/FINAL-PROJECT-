package com.example.finalprojectbond;

import com.example.finalprojectbond.Api.ApiResponse;
import com.example.finalprojectbond.Controller.ExperienceController;
import com.example.finalprojectbond.InDTO.ExperienceInDTO;
import com.example.finalprojectbond.Model.MyUser;
import com.example.finalprojectbond.OutDTO.ExperienceOutDTO;
import com.example.finalprojectbond.Service.ExperienceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = ExperienceController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class ExperienceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ExperienceService experienceService;

    @InjectMocks
    private ExperienceController experienceController;

    public ExperienceControllerTest() {
        mockMvc = MockMvcBuilders.standaloneSetup(experienceController).build();
    }

    @Test
    void testGetAllExperiences() throws Exception {
        Integer userId = 1;
        ExperienceOutDTO experienceOutDTO1 = new ExperienceOutDTO("Experience 1", "City 1", "Description 1");
        ExperienceOutDTO experienceOutDTO2 = new ExperienceOutDTO("Experience 2", "City 2", "Description 2");

        when(experienceService.findAll(userId)).thenReturn(List.of(experienceOutDTO1, experienceOutDTO2));

        ResponseEntity<List<ExperienceOutDTO>> response = experienceController.getAllExperiences(new MyUser());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("Experience 1", response.getBody().get(0).getTitle());
        assertEquals("Experience 2", response.getBody().get(1).getTitle());
    }

    // Test for createExperience()
    @Test
    void testCreateExperience() throws Exception {
        ExperienceInDTO experienceInDTO = new ExperienceInDTO("New Experience", "New City", "New Description", null, null, "Advance", "Male");
        Integer userId = 1;

        doNothing().when(experienceService).createExperience(userId, experienceInDTO);

        ResponseEntity<ApiResponse> response = experienceController.createExperience(new MyUser(), experienceInDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Experience created successfully", response.getBody().getMessage());
    }

    // Test for updateExperience()
    @Test
    void testUpdateExperience() throws Exception {
        Integer experienceId = 1;
        Integer userId = 1;
        ExperienceInDTO experienceInDTO = new ExperienceInDTO("Updated Experience", "Updated City", "Updated Description", null, null, "Advance", "Male");

        doNothing().when(experienceService).updateExperience(userId, experienceInDTO, experienceId);

        ResponseEntity<ApiResponse> response = experienceController.updateExperience(new MyUser(), experienceInDTO, experienceId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Experience updated successfully", response.getBody().getMessage());
    }
}
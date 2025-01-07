package com.example.finalprojectbond;

import com.example.finalprojectbond.Model.*;
import com.example.finalprojectbond.Repository.ExperienceRepository;
import com.example.finalprojectbond.Repository.ExplorerRepository;
import com.example.finalprojectbond.Repository.OrganizerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ExperienceRepositoryTest {

    @Autowired
    private ExplorerRepository explorerRepository;
    @Autowired
    private OrganizerRepository organizerRepository;
    @Autowired
    private ExperienceRepository experienceRepository;

    @Test
    void testFindExplorersByExperienceId() {

        MyUser myUser1 = new MyUser();
        myUser1.setUsername("explorer1");
        myUser1.setName("Explorer One");
        myUser1.setPassword("password1");
        myUser1.setEmail("explorer1@gmail.com");
        myUser1.setAge(30);
        myUser1.setGender("Male");
        myUser1.setCity("Jeddah");
        myUser1.setHealthStatus("Healthy");
        myUser1.setPhoneNumber("0581234567");
        myUser1.setPhotoURL("www.photo1.com");
        myUser1.setRole("EXPLORER");

        Explorer explorer1 = new Explorer();
        explorer1.setMyUser(myUser1);


        MyUser myUser2 = new MyUser();
        myUser2.setUsername("explorer2");
        myUser2.setName("Explorer Two");
        myUser2.setPassword("password2");
        myUser2.setEmail("explorer2@gmail.com");
        myUser2.setAge(25);
        myUser2.setGender("Female");
        myUser2.setCity("Riyadh");
        myUser2.setHealthStatus("Fit");
        myUser2.setPhoneNumber("0587654321");
        myUser2.setPhotoURL("www.photo2.com");
        myUser2.setRole("EXPLORER");

        Explorer explorer2 = new Explorer();
        explorer2.setMyUser(myUser2);


        explorerRepository.save(explorer1);
        explorerRepository.save(explorer2);


        Experience experience = new Experience();
        experience.setTitle("Safari Adventure");
        experience.setDescription("A thrilling safari experience.");
        experience.setCity("Nairobi");
        experience.setStatus("Accept Application");
        experience.setStartDate(LocalDate.of(2025, 4, 10));
        experience.setEndDate(LocalDate.of(2025, 4, 20));
        experience.setDifficulty("Hard");
        experience.setAudienceType("FAMILY");


        experience.getExplorers().add(explorer1);
        experience.getExplorers().add(explorer2);


        explorer1.getExperiences().add(experience);
        explorer2.getExperiences().add(experience);


        experienceRepository.save(experience);


        List<Explorer> explorers = experienceRepository.findExplorersByExperienceId(experience.getId());


        assertThat(explorers).hasSize(2);
        assertThat(explorers).extracting("myUser.username")
                .containsExactlyInAnyOrder("explorer1", "explorer2");
        assertThat(explorers).extracting("myUser.email")
                .containsExactlyInAnyOrder("explorer1@gmail.com", "explorer2@gmail.com");
    }


    @Test
    void testFindExperienceById() {

        Experience experience = new Experience();
        experience.setTitle("Desert Safari");
        experience.setDescription("A unique desert safari adventure.");
        experience.setCity("Dubai");
        experience.setStatus("Accept Application");
        experience.setStartDate(LocalDate.of(2025, 5, 1));
        experience.setEndDate(LocalDate.of(2025, 5, 5));
        experience.setDifficulty("Moderate");
        experience.setAudienceType("FAMILY"); // Changed from "ALL" to "FAMILY"


        Experience savedExperience = experienceRepository.save(experience);


        Experience retrievedExperience = experienceRepository.findExperienceById(savedExperience.getId());


        assertThat(retrievedExperience).isNotNull();
        assertThat(retrievedExperience.getId()).isEqualTo(savedExperience.getId());
        assertThat(retrievedExperience.getTitle()).isEqualTo("Desert Safari");
        assertThat(retrievedExperience.getDescription()).isEqualTo("A unique desert safari adventure.");
        assertThat(retrievedExperience.getCity()).isEqualTo("Dubai");
        assertThat(retrievedExperience.getStartDate()).isEqualTo(LocalDate.of(2025, 5, 1));
        assertThat(retrievedExperience.getEndDate()).isEqualTo(LocalDate.of(2025, 5, 5));
        assertThat(retrievedExperience.getDifficulty()).isEqualTo("Moderate");
        assertThat(retrievedExperience.getAudienceType()).isEqualTo("FAMILY");
    }

    @Test
    void testFindAllByOrganizer() {

        MyUser myUser = new MyUser();
        myUser.setUsername("organizerUser");
        myUser.setName("Organizer One");
        myUser.setPassword("securePassword");
        myUser.setEmail("organizer1@example.com");
        myUser.setAge(40);
        myUser.setGender("Female");
        myUser.setCity("Cairo");
        myUser.setHealthStatus("Healthy");
        myUser.setPhoneNumber("0505303076");
        myUser.setPhotoURL("www.organizerphoto.com");
        myUser.setRole("ADMIN");


        Organizer organizer = new Organizer();
        organizer.setMyUser(myUser);
        organizer.setUserProfileSummary("i love surfing the waves");
        organizer.setLicenseSerialNumber("L12233454545");


        Experience experience1 = new Experience();
        experience1.setTitle("Mountain Climbing Adventure");
        experience1.setDescription("Exciting experience for climbers.");
        experience1.setAudienceType("FAMILY");
        experience1.setOrganizer(organizer); // Assign the organizer to the experience
        experience1.setCity("Cairo");
        experience1.setStatus("Accept Application");
        experience1.setStartDate(LocalDate.of(2025, 5, 1));
        experience1.setEndDate(LocalDate.of(2025, 5, 5));
        experience1.setDifficulty("Moderate");

        Experience experience2 = new Experience();
        experience2.setTitle("Cultural Exploration");
        experience2.setDescription("Explore cultural sites.");
        experience2.setAudienceType("FAMILY");
        experience2.setOrganizer(organizer);
        experience2.setCity("Cairo");
        experience2.setStatus("Accept Application"); // Corrected to experience2
        experience2.setStartDate(LocalDate.of(2025, 5, 1));
        experience2.setEndDate(LocalDate.of(2025, 5, 5));
        experience2.setDifficulty("Moderate");


        organizerRepository.save(organizer);
        experienceRepository.save(experience1);
        experienceRepository.save(experience2);


        List<Experience> experiences = experienceRepository.findAllByOrganizer(organizer);


        assertThat(experiences).isNotNull();
        assertThat(experiences.size()).isEqualTo(2);
        assertThat(experiences).extracting(Experience::getTitle)
                .containsExactlyInAnyOrder("Mountain Climbing Adventure", "Cultural Exploration");
    }
}



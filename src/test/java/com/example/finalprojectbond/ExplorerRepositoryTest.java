package com.example.finalprojectbond;

import com.example.finalprojectbond.Model.*;
import com.example.finalprojectbond.Repository.AuthRepository;
import com.example.finalprojectbond.Repository.ExperienceRepository;
import com.example.finalprojectbond.Repository.ExplorerRepository;
import com.example.finalprojectbond.Repository.OrganizerRepository;
import org.junit.jupiter.api.BeforeEach;
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

//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ExplorerRepositoryTest {

    @Autowired
    private ExplorerRepository explorerRepository;
    @Autowired
    private OrganizerRepository organizerRepository;
    @Autowired
    private ExperienceRepository experienceRepository;


//    Experience experience;
    Organizer organizer;
//    Experience experience1;
    Explorer explorer;
    MyUser user;

    @Test
    void setUp() {

        MyUser myUser = new MyUser();
        myUser.setUsername("myUser");
        myUser.setName("myUser");
        myUser.setPassword("myPassword");
        myUser.setEmail("myUser@gmail.com");
        myUser.setAge(53);
        myUser.setGender("Male");
        myUser.setCity("Jeddah");
        myUser.setHealthStatus("hamekeke");
        myUser.setPhoneNumber("0581110421");
        myUser.setPhotoURL("www.MyPhoto.com");
        myUser.setRole("ORGANIZER");


        Explorer explorer = new Explorer();
        explorer.setMyUser(myUser);


        Experience experience = new Experience();
        experience.setTitle("Mountain Adventure");
        experience.setDescription("An exciting mountain adventure.");
        experience.setCity("Denver");
        experience.setStatus("Accept Application");
        experience.setStartDate(LocalDate.of(2025, 2, 1));
        experience.setEndDate(LocalDate.of(2025, 2, 7));
        experience.setDifficulty("Moderate");
        experience.setAudienceType("FAMILY");


        Experience experience2 = new Experience();
        experience2.setTitle("Beach Exploration");
        experience2.setDescription("Relaxing exploration of sandy beaches.");
        experience2.setCity("Miami");
        experience2.setStatus("Accept Application");
        experience2.setStartDate(LocalDate.of(2025, 3, 15));
        experience2.setEndDate(LocalDate.of(2025, 3, 20));
        experience2.setDifficulty("Easy");
        experience2.setAudienceType("MALE");


        experienceRepository.save(experience);
        experienceRepository.save(experience2);


        explorer.setExperiences(Set.of(experience, experience2));


        explorerRepository.save(explorer);


        List<Experience> experiences = explorerRepository.findAllExperiencesByExplorerId(explorer.getId());



        assertThat(experiences).hasSize(2);
        assertThat(experiences).extracting("title")
                .containsExactlyInAnyOrder("Mountain Adventure", "Beach Exploration");
        assertThat(experiences).extracting("city")
                .containsExactlyInAnyOrder("Denver", "Miami");
    }

    @Test
    void testFindExplorerById() {

        MyUser myUser = new MyUser();
        myUser.setUsername("explorerUser");
        myUser.setName("Explorer One");
        myUser.setPassword("securePassword");
        myUser.setEmail("explorer1@example.com");
        myUser.setAge(30);
        myUser.setGender("Male");
        myUser.setCity("Cairo");
        myUser.setHealthStatus("Healthy");
        myUser.setPhoneNumber("0123456789");
        myUser.setPhotoURL("www.explorerphoto.com");
        myUser.setRole("EXPLORER");


        Explorer explorer = new Explorer();
        explorer.setMyUser(myUser);


        Explorer savedExplorer = explorerRepository.save(explorer);


        Explorer retrievedExplorer = explorerRepository.findExplorerById(savedExplorer.getId());


        assertThat(retrievedExplorer).isNotNull();
        assertThat(retrievedExplorer.getId()).isEqualTo(savedExplorer.getId());
        assertThat(retrievedExplorer.getMyUser().getUsername()).isEqualTo("explorerUser");
        assertThat(retrievedExplorer.getMyUser().getName()).isEqualTo("Explorer One");
        assertThat(retrievedExplorer.getMyUser().getEmail()).isEqualTo("explorer2@example.com");
        assertThat(retrievedExplorer.getMyUser().getAge()).isEqualTo(30);
        assertThat(retrievedExplorer.getMyUser().getGender()).isEqualTo("Male");
        assertThat(retrievedExplorer.getMyUser().getCity()).isEqualTo("Cairo");
        assertThat(retrievedExplorer.getMyUser().getHealthStatus()).isEqualTo("Healthy");
        assertThat(retrievedExplorer.getMyUser().getPhoneNumber()).isEqualTo("0123456789");
        assertThat(retrievedExplorer.getMyUser().getPhotoURL()).isEqualTo("www.explorerphoto.com");
        assertThat(retrievedExplorer.getMyUser().getRole()).isEqualTo("EXPLORER");
    }

    }






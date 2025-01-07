package com.example.finalprojectbond.Repository;

import com.example.finalprojectbond.Model.Application;
import com.example.finalprojectbond.Model.Experience;
import com.example.finalprojectbond.Model.Explorer;
import com.example.finalprojectbond.Model.MeetingZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {
    Application findApplicationById (Integer id);
    List<Application> findAllByStatusAndExperience(String status, Experience experience);
    List<Application> findAllByExperience(Experience experience);

    List<Application> findAllByExperienceTitleContainingIgnoreCaseAndExplorer(String title, Explorer explorer);

    Application findApplicationByExperienceAndExplorer(Experience experience, Explorer explorer);

    List<Application> findAllByExperienceAndIsMeetingZoneAndStatus(Experience experience, Boolean isMeetingZone, String status);
    List<Application> findAllByExperienceAndStatus(Experience experience, String status);
    List<Application> findAllByIsMeetingZoneAndExperience(Boolean isMeetingZone, Experience experience);
}

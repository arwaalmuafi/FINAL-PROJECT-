package com.example.finalprojectbond.Service;

import com.example.finalprojectbond.Api.ApiException;
import com.example.finalprojectbond.InDTO.ExperienceInDTO;
import com.example.finalprojectbond.InDTO.OrganizerInDTO;
import com.example.finalprojectbond.Model.*;
import com.example.finalprojectbond.OutDTO.ExperienceOutDTO;
import com.example.finalprojectbond.OutDTO.ExperienceSearchOutDTO;
import com.example.finalprojectbond.OutDTO.OrganizerFilterOutDTO;
import com.example.finalprojectbond.OutDTO.OrganizerOutDTO;
import com.example.finalprojectbond.Repository.ApplicationRepository;
import com.example.finalprojectbond.Repository.AuthRepository;
import com.example.finalprojectbond.Repository.ExperienceRepository;
import com.example.finalprojectbond.Repository.OrganizerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@EnableScheduling
@Service
@RequiredArgsConstructor
public class OrganizerService {
    private final OrganizerRepository organizerRepository;
    private final AuthRepository authRepository;
    private final ExperienceRepository experienceRepository;
    private final ExperienceService experienceService;
    private final ApplicationRepository applicationRepository;



    public OrganizerOutDTO getMyOrganizer(Integer myUserId){
        Organizer organizer = organizerRepository.findOrganizerById(myUserId);
        MyUser user = authRepository.findMyUserById(myUserId);
        if (organizer == null || user == null) {
            throw new ApiException("Organizer not found");
        }
        OrganizerOutDTO organizerOutDTO = new OrganizerOutDTO(user.getUsername(),user.getName(), user.getAge(), user.getCity(),user.getHealthStatus(),user.getEmail(),user.getGender(),user.getRole(),user.getPhoneNumber(),user.getPhotoURL(),organizer.getUserProfileSummary(),organizer.getNumberOfExperience(),organizer.getLicenseSerialNumber(),user.getRating(),organizer.getIsApproved());
        return organizerOutDTO;
    }


    public void registerOrganizer(OrganizerInDTO organizerInDTO) {
        //String hashPassword = new BCryptPasswordEncoder().encode(organizerInDTO.getPassword());
//        MyUser myUser = new MyUser(organizerInDTO.getUsername(),organizerInDTO.getPassword(),organizerInDTO.getName(),organizerInDTO.getAge(),organizerInDTO.getCity(), organizerInDTO.getHealthStatus(),organizerInDTO.getEmail(),organizerInDTO.getGender(),"ORGANIZER",organizerInDTO.getPhoneNumber(),organizerInDTO.getPhotoURL());
        MyUser myUser = new MyUser();
        myUser.setUsername(organizerInDTO.getUsername());
        String hashPassword = new BCryptPasswordEncoder().encode(organizerInDTO.getPassword());
        myUser.setPassword(hashPassword);
        myUser.setEmail(organizerInDTO.getEmail());
        myUser.setGender(organizerInDTO.getGender());
        myUser.setName(organizerInDTO.getName());
        myUser.setPhoneNumber(organizerInDTO.getPhoneNumber());
        myUser.setPhotoURL(organizerInDTO.getPhotoURL());
        myUser.setAge(organizerInDTO.getAge());
        myUser.setCity(organizerInDTO.getCity());
        myUser.setHealthStatus(organizerInDTO.getHealthStatus());
        myUser.setRole("ORGANIZER");
//        myUser.setRole("ADMIN");
        authRepository.save(myUser);

        Organizer organizer = new Organizer();
        organizer.setMyUser(myUser);
        organizer.setUserProfileSummary(organizerInDTO.getUserProfileSummary());
        organizer.setLicenseSerialNumber(organizerInDTO.getLicenseSerialNumber());
        organizerRepository.save(organizer);

    }

    public void updateOrganizer(Integer myUserId,OrganizerInDTO organizerInDTO) {
        MyUser myUser = authRepository.findMyUserById(myUserId);
        Organizer organizer = organizerRepository.findOrganizerById(myUserId);
        if (organizer == null || myUser == null) {
            throw new ApiException("Organizer not found");
        }
        myUser.setUsername(organizerInDTO.getUsername());
        String hashPassword = new BCryptPasswordEncoder().encode(organizerInDTO.getPassword());
        myUser.setPassword(hashPassword);
        myUser.setName(organizerInDTO.getName());
        myUser.setAge(organizerInDTO.getAge());
        myUser.setCity(organizerInDTO.getCity());
        myUser.setHealthStatus(organizerInDTO.getHealthStatus());
        myUser.setEmail(organizerInDTO.getEmail());
        myUser.setGender(organizerInDTO.getGender());
        myUser.setPhoneNumber(organizerInDTO.getPhoneNumber());
        myUser.setPhotoURL(organizerInDTO.getPhotoURL());
        authRepository.save(myUser);
        organizer.setLicenseSerialNumber(organizerInDTO.getLicenseSerialNumber());
        organizer.setUserProfileSummary(organizerInDTO.getUserProfileSummary());
        organizerRepository.save(organizer);
    }

    public void deleteMyOrganizer(Integer myUserId) {
        MyUser myUser = authRepository.findMyUserById(myUserId);
        Organizer organizer = organizerRepository.findOrganizerById(myUserId);
        if (myUser == null || organizer == null) {
            throw new ApiException("Organizer not found");
        }
        myUser.setOrganizer(null);
        organizerRepository.delete(organizer);
        authRepository.delete(myUser);
    }

    public List<OrganizerFilterOutDTO> getOrganizerByRatingAsc(){
        List<MyUser> users = authRepository.findAllByRoleOrderByRatingAsc("ORGANIZER");
        if (users == null ) {
            throw new ApiException("Organizer not found");
        }
        List<OrganizerFilterOutDTO> organizerFilterOutDTO=new ArrayList<>();
        for (MyUser user : users) {
            organizerFilterOutDTO.add(new OrganizerFilterOutDTO(user.getPhotoURL(), user.getUsername(),user.getName(), user.getCity(),user.getOrganizer().getUserProfileSummary(), user.getRating()));
        }
        return organizerFilterOutDTO;
    }

    public List<OrganizerFilterOutDTO> getOrganizerByRatingDesc(){
        List<MyUser> users = authRepository.findAllByRoleOrderByRatingDesc("ORGANIZER");
        if (users == null ) {
            throw new ApiException("Organizer not found");
        }
        List<OrganizerFilterOutDTO> organizerFilterOutDTO=new ArrayList<>();
        for (MyUser user : users) {
            organizerFilterOutDTO.add(new OrganizerFilterOutDTO(user.getPhotoURL(), user.getUsername(),user.getName(), user.getCity(),user.getOrganizer().getUserProfileSummary(), user.getRating()));
        }
        return organizerFilterOutDTO;
    }

    public List<OrganizerFilterOutDTO> getOrganizerByCity(String city){
        List<MyUser> users = authRepository.findAllByCityAndRole(city,"ORGANIZER");
        if (users == null ) {
            throw new ApiException("Organizer not found");
        }
        List<OrganizerFilterOutDTO> organizerFilterOutDTO=new ArrayList<>();
        for (MyUser user : users) {
            organizerFilterOutDTO.add(new OrganizerFilterOutDTO(user.getPhotoURL(), user.getUsername(),user.getName(), user.getCity(),user.getOrganizer().getUserProfileSummary(), user.getRating()));
        }
        return organizerFilterOutDTO;
    }

    public List<ExperienceOutDTO> getAllOrganizerExperiences(Integer myUserId){
        Organizer organizer = organizerRepository.findOrganizerById(myUserId);
        if (organizer == null) {
            throw new ApiException("Organizer not found");
        }
        List<Experience> experiences = experienceRepository.findAllByOrganizer(organizer);
//        return changeExperienceToOutDTO(experiences);
        return experienceService.convertToOutDTO(experiences);
    }

//    public List<ExperienceOutDTO> changeExperienceToOutDTO(List<Experience> experiences){
//        List<ExperienceOutDTO> experienceOutDTOS = new ArrayList<>();
//        for (Experience experience : experiences) {
//            ExperienceOutDTO experienceOutDTO = new ExperienceOutDTO(experience.getTitle(),experience.getDescription(),experience.getCity(),experience.getStatus(),experience.getStartDate(),experience.getEndDate(),experience.getCreatedAt(),experience.getDifficulty());
//            experienceOutDTOS.add(experienceOutDTO);
//        }
//        return experienceOutDTOS;
//    }


    public List<ExperienceSearchOutDTO> searchExperienceByTitle(Integer myUserId,String title){
        Organizer organizer = organizerRepository.findOrganizerById(myUserId);
        if (organizer == null) {
            throw new ApiException("Organizer not found");
        }
        List<Experience> experiences1 = experienceRepository.findAllByTitleContainingIgnoreCaseAndOrganizer(title,organizer);
        return changeExperienceToSearchOutDTO(experiences1);
    }

    public List<ExperienceSearchOutDTO> changeExperienceToSearchOutDTO(List<Experience> experiences){
        List<ExperienceSearchOutDTO> experienceOutDTOS = new ArrayList<>();
        for (Experience experience : experiences) {
            ExperienceSearchOutDTO experienceOutDTO = new ExperienceSearchOutDTO(experience.getTitle(),experience.getStartDate(),experience.getEndDate(),experience.getDescription(),experience.getStatus());
            experienceOutDTOS.add(experienceOutDTO);
        }
        return experienceOutDTOS;
    }



    @Scheduled(fixedRate = 60000)
    public void confirmMeetingZoneForExperience(){
        List<Experience> experiences = experienceRepository.findAll();
        for(Experience experience : experiences) {
            List<Application> applications = applicationRepository.findAllByExperienceAndIsMeetingZoneAndStatus(experience, true, "Accepted");
            List<Application> applications1 = applicationRepository.findAllByExperienceAndStatus(experience, "Accepted");
            if (!experience.getStatus().equalsIgnoreCase("Confirming")){
                continue;
            }
            if (experience.getMeetingZone() == null) {
                continue;
            }
            if (applications.size() == applications1.size()) {
                experience.setStatus("Task Assignment");
                experienceRepository.save(experience);
            }
        }


    }



}

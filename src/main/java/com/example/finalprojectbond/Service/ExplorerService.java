package com.example.finalprojectbond.Service;

import com.example.finalprojectbond.Api.ApiException;
import com.example.finalprojectbond.InDTO.ExplorerInDTO;
import com.example.finalprojectbond.Model.Application;
import com.example.finalprojectbond.Model.Experience;
import com.example.finalprojectbond.Model.Explorer;
import com.example.finalprojectbond.Model.MyUser;
import com.example.finalprojectbond.OutDTO.BriefExplorerOutDTO;
import com.example.finalprojectbond.OutDTO.ExperienceSearchOutDTO;
import com.example.finalprojectbond.OutDTO.ExplorerOutDTO;
import com.example.finalprojectbond.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExplorerService {

    private final AuthRepository authRepository;
    private final ExplorerRepository explorerRepository;
    private final ReviewExplorerRepository reviewExplorerRepository;
    private final ExperienceRepository experienceRepository;
    private final ApplicationRepository applicationRepository;



    // GET All Explorers
    public List<ExplorerOutDTO> getAllExplorers(Integer userId) {
        MyUser myUser = authRepository.findMyUserById(userId);
        List<Explorer> explorers = explorerRepository.findAll();
        return convertToExplorerDTO(explorers);

    }

    // Register new Explorer
    public void registerExplorer(ExplorerInDTO explorerInDTO) {
        MyUser myUser = new MyUser();
        myUser.setUsername(explorerInDTO.getUsername());
        myUser.setName(explorerInDTO.getName());
        String hashPassword = new BCryptPasswordEncoder().encode(explorerInDTO.getPassword());
        myUser.setPassword(hashPassword);
        myUser.setAge(explorerInDTO.getAge());
        myUser.setCity(explorerInDTO.getCity());
        myUser.setHealthStatus(explorerInDTO.getHealthStatus());
        myUser.setEmail(explorerInDTO.getEmail());
        myUser.setGender(explorerInDTO.getGender());
        myUser.setPhoneNumber(explorerInDTO.getPhoneNumber());
        myUser.setPhotoURL(explorerInDTO.getPhotoURL());
        myUser.setRole("EXPLORER");
        authRepository.save(myUser);
        // ==================================
        Explorer explorer = new Explorer();
        explorer.setMyUser(myUser);
        explorerRepository.save(explorer);
    }

    // Update existing Explorer
    public void updateExplorer(Integer myUserId,ExplorerInDTO explorerInDTO) {
        Explorer explorer= explorerRepository.findExplorerById(myUserId);
        MyUser myUser = authRepository.findMyUserById(myUserId);
        if( explorer == null || myUser == null) {
            throw new ApiException("explorer was not found");
        }
        myUser.setUsername(explorerInDTO.getUsername());
        myUser.setName(explorerInDTO.getName());
        String hashPassword = new BCryptPasswordEncoder().encode(explorerInDTO.getPassword());
        myUser.setPassword(hashPassword);
        myUser.setAge(explorerInDTO.getAge());
        myUser.setCity(explorerInDTO.getCity());
        myUser.setHealthStatus(explorerInDTO.getHealthStatus());
        myUser.setEmail(explorerInDTO.getEmail());
        myUser.setGender(explorerInDTO.getGender());
        myUser.setPhoneNumber(explorerInDTO.getPhoneNumber());
        myUser.setPhotoURL(explorerInDTO.getPhotoURL());
        myUser.setRole("EXPLORER");
        authRepository.save(myUser);
        explorer.setMyUser(myUser);
        explorerRepository.save(explorer);
    }

    // DELETE Explorer
    public void deleteExplorer(Integer myUserId) {
        Explorer explorer = explorerRepository.findExplorerById(myUserId);
        MyUser myUser = authRepository.findMyUserById(myUserId);
        if( explorer == null || myUser == null) {
            throw new ApiException("explorer was not found");
        }
        explorer.setMyUser(null);
        myUser.setExplorer(null);
        explorerRepository.delete(explorer);
        authRepository.delete(myUser);
    }

    // GET the brief information of the explorer
    public List<BriefExplorerOutDTO> getBriefExplorer(List <Explorer> explorers) {

        List<BriefExplorerOutDTO> briefExplorerOutDTO = new ArrayList<>();
        for (Explorer explorer : explorers) {
            briefExplorerOutDTO.add( new BriefExplorerOutDTO(explorer.getMyUser().getName(),
                    explorer.getMyUser().getAge()
                    ,explorer.getMyUser().getHealthStatus()
                    ,explorer.getMyUser().getGender()
                    ,explorer.getMyUser().getPhotoURL()));
        }

        return briefExplorerOutDTO;
    }

    public List<ExplorerOutDTO> convertToExplorerDTO(List<Explorer> explorers) {
        List<ExplorerOutDTO> explorerDTOs = new ArrayList<>();
        for (Explorer explorer : explorers) {
            explorerDTOs.add(new ExplorerOutDTO(explorer.getMyUser().getName(),
                    explorer.getMyUser().getAge(),
                    explorer.getMyUser().getCity(),
                    explorer.getMyUser().getHealthStatus(),
                    explorer.getMyUser().getEmail(),
                    explorer.getMyUser().getGender(),
                    explorer.getMyUser().getPhoneNumber(),
                    explorer.getMyUser().getPhotoURL(),
                    explorer.getMyUser().getRating())
            );
        }
        return explorerDTOs;
    }


    public void confirmMeetingZone(Integer myUserId,Integer experienceId) {
        Explorer explorer = explorerRepository.findExplorerById(myUserId);
        if (explorer == null) {
            throw new ApiException("Explorer not found");
        }
        Experience experience = experienceRepository.findExperienceById(experienceId);
        if (experience == null) {
            throw new ApiException("Experience not found");
        }
        Application application = applicationRepository.findApplicationByExperienceAndExplorer(experience, explorer);
        if (application == null) {
            throw new ApiException("Application not found");
        }
        if (!application.getStatus().equalsIgnoreCase("Accepted")) {
            throw new ApiException("cannot confirm meeting zone because you have not been accepted into the experiment yet.");
        }
        if (experience.getMeetingZone() == null) {
            throw new ApiException("Meeting zone has not been set yet!");
        }
        if (!experience.getStatus().equalsIgnoreCase("Confirming")) {
            throw new ApiException("cannot confirm meeting zone because the experience does not accept confirming now");
        }

        application.setIsMeetingZone(true);
        applicationRepository.save(application);
    }
        // method to get experiences associated with the explorer
        public List<ExperienceSearchOutDTO> getParticipatedInExperiences (Integer myUserId){

            Explorer explorer = explorerRepository.findExplorerById(myUserId);
            if (explorer == null) {
                throw new ApiException("explorer was not found");
            }
            List<Experience> experiences = explorerRepository.findAllExperiencesByExplorerId(myUserId);
            List<ExperienceSearchOutDTO> experienceDTOs = new ArrayList<>();
            for (Experience experience : experiences) {
                experienceDTOs.add(new ExperienceSearchOutDTO(experience.getTitle(), experience.getStartDate(), experience.getEndDate(), experience.getDescription(), experience.getStatus()));
            }
            return experienceDTOs;
        }

}

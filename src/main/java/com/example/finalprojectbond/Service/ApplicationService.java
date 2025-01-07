package com.example.finalprojectbond.Service;

import com.example.finalprojectbond.Api.ApiException;
import com.example.finalprojectbond.InDTO.ApplicationInDTO;
import com.example.finalprojectbond.Model.*;
import com.example.finalprojectbond.OutDTO.*;
import com.example.finalprojectbond.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final ExplorerRepository explorerRepository;
    private final ExperienceRepository experienceRepository;
    private final OrganizerRepository organizerRepository;
    private final AuthRepository authRepository;
    private final ExperiencePhotoRepository experiencePhotoRepository;

//    public List<ApplicationOutDTO> getMyApplications(Integer explorerId) {
//        Explorer explorer = explorerRepository.findExplorerById(explorerId);
//        if (explorer == null) {
//            throw new ApiException("Explorer not found");
//        }
//        if (explorer.getApplications() == null) {
//            throw new ApiException("Explorer does not have any Applications ");
//        }
//        List<Application> applications = new ArrayList<>();
//        for (Application application : explorer.getApplications()) {
//            applications.add(application);
//        }
//        return changeApplicationsToOutDTO(applications);
//    }

        public List<ExplorerApplicationOutDTO> getMyApplications(Integer myUserId) {
        Explorer explorer = explorerRepository.findExplorerById(myUserId);
        if (explorer == null) {
            throw new ApiException("Explorer not found");
        }
        if (explorer.getApplications() == null) {
            throw new ApiException("Explorer does not have any Applications ");
        }
        List<Application> applications = new ArrayList<>();
        for (Application application : explorer.getApplications()) {
            applications.add(application);
        }
        return changeToExplorerApplicationOutDTO(applications);
    }

    public List<ApplicationOutDTO> changeApplicationsToOutDTO(List<Application> applications) {
        List<ApplicationOutDTO> applicationOutDTOs = new ArrayList<>();
        for (Application application : applications) {
            ApplicationOutDTO applicationOutDTO = new ApplicationOutDTO(application.getDescription(), application.getTools(), application.getStatus(), application.getIsMeetingZone());
            applicationOutDTOs.add(applicationOutDTO);
        }
        return applicationOutDTOs;
    }
    public ApplicationOutDTO changeApplicationsToOutDTO(Application application) {
        ApplicationOutDTO applicationOutDTO = new ApplicationOutDTO(application.getDescription(),application.getTools(),application.getStatus(),application.getIsMeetingZone());
        return applicationOutDTO;
    }

    public void createApplication(Integer myUserID,Integer experienceId,ApplicationInDTO applicationInDTO) {
        Explorer explorer = explorerRepository.findExplorerById(myUserID);
        if (explorer == null) {
            throw new ApiException("Explorer not found");
        }
        Experience experience = experienceRepository.findExperienceById(experienceId);
        if (experience == null) {
            throw new ApiException("Experience not found");
        }
        if (!experience.getStatus().equals("Accept Application")){
            throw new ApiException("Experience is not accepting applications anymore");
        }
        if (applicationRepository.findApplicationByExperienceAndExplorer(experience, explorer) == null) {
            Application application = new Application();
            application.setDescription(applicationInDTO.getDescription());
            application.setTools(applicationInDTO.getTools());
            application.setExperience(experience);
            application.setExplorer(explorer);
            application.setStatus("Pending");
            applicationRepository.save(application);
        }else {
            throw new ApiException("Application already exit");
        }
    }

    public void updateApplication(Integer myUserID,Integer experienceId,Integer applicationId,ApplicationInDTO applicationInDTO) {
        Explorer explorer = explorerRepository.findExplorerById(myUserID);
        if (explorer == null) {
            throw new ApiException("Explorer not found");
        }
        Experience experience = experienceRepository.findExperienceById(experienceId);
        if (experience == null) {
            throw new ApiException("Experience not found");
        }
        Application application = applicationRepository.findApplicationById(applicationId);
        if (application == null) {
            throw new ApiException("Application not found");
        }
        if (!application.getExperience().equals(experience)) {
            throw new ApiException("Explorer does not have this Application");
        }
        if (!application.getExplorer().equals(explorer)) {
            throw new ApiException("Explorer does not have this application");
        }
        application.setDescription(applicationInDTO.getDescription());
        application.setTools(applicationInDTO.getTools());
        applicationRepository.save(application);
    }

    public void cancelApplication(Integer myUserID,Integer experienceId,Integer applicationId) {
        Explorer explorer = explorerRepository.findExplorerById(myUserID);
        if (explorer == null) {
            throw new ApiException("Explorer not found");
        }
        Experience experience = experienceRepository.findExperienceById(experienceId);
        if (experience == null) {
            throw new ApiException("Experience not found");
        }
        Application application = applicationRepository.findApplicationById(applicationId);
        if (application == null) {
            throw new ApiException("Application not found");
        }
        if (!application.getExperience().equals(experience)) {
            throw new ApiException("experience does not have this Application");
        }
        if(!application.getExplorer().equals(explorer)) {
            throw new ApiException("Explorer does not have this Application");
        }
        if (application.getStatus().equals("Rejected") || application.getStatus().equals("Accepted")) {
            throw new ApiException("Application is already rejected/accepted");
        }
        applicationRepository.delete(application);

    }

    public void rejectApplication(Integer myUserID,Integer explorerId,Integer applicationId) {
        Organizer organizer = organizerRepository.findOrganizerById(myUserID);
        if (organizer == null){
            throw new ApiException("Organizer not found");
        }
        Explorer explorer = explorerRepository.findExplorerById(explorerId);
        if (explorer == null) {
            throw new ApiException("Explorer not found");
        }
        Application application = applicationRepository.findApplicationById(applicationId);
        if (application == null) {
            throw new ApiException("Application not found");
        }
        Experience experience = application.getExperience();
        if (experience == null) {
            throw new ApiException("Experience not found");
        }

        if (!experience.getOrganizer().equals(organizer)) {
            throw new ApiException("Organizer does not have this experience");
        }
        if (!experience.getStatus().equalsIgnoreCase("Accept Application")) {
            throw new ApiException("the status of experience should be Accept Application");
        }
        if (!application.getExplorer().equals(explorer)) {
            throw new ApiException("Explorer does not have this Application");
        }
        if (application.getStatus().equals("Rejected") || application.getStatus().equals("Accepted")) {
            throw new ApiException("Application is already rejected/accepted");
        }
        application.setStatus("Rejected");
        applicationRepository.save(application);

    }

    public void acceptApplication(Integer myUserID,Integer explorerId,Integer applicationId) {
        Organizer organizer = organizerRepository.findOrganizerById(myUserID);
        if (organizer == null){
            throw new ApiException("Organizer not found");
        }
        Explorer explorer = explorerRepository.findExplorerById(explorerId);
        if (explorer == null) {
            throw new ApiException("Explorer not found");
        }
        Application application = applicationRepository.findApplicationById(applicationId);
        if (application == null) {
            throw new ApiException("Application not found");
        }
        Experience experience = application.getExperience();
        if (experience == null) {
            throw new ApiException("Experience not found");
        }

        if (!experience.getOrganizer().equals(organizer)) {
            throw new ApiException("Organizer does not have this experience");
        }

        if (!experience.getStatus().equalsIgnoreCase("Accept Application")) {
            throw new ApiException("the status of experience should be Accept Application");
        }

        if (!application.getExplorer().equals(explorer)) {
            throw new ApiException("Explorer does not have this Application");
        }

        if (application.getStatus().equals("Rejected") || application.getStatus().equals("Accepted")) {
            throw new ApiException("Application is already rejected/accepted");
        }

        if (experience.getAudienceType().equalsIgnoreCase("FAMILY")) {
            application.setStatus("Accepted");
            experience.getExplorers().add(explorer);
            explorer.getExperiences().add(experience);
            experienceRepository.save(experience);
            explorerRepository.save(explorer);
            applicationRepository.save(application);
            experience.getExplorers().add(explorer);
            explorer.getExperiences().add(experience);
            experienceRepository.save(experience);
            explorerRepository.save(explorer);

        } else if (application.getExplorer().getMyUser().getGender().equalsIgnoreCase(experience.getAudienceType())) {
            application.setStatus("Accepted");
            experience.getExplorers().add(explorer);
            explorer.getExperiences().add(experience);
            experienceRepository.save(experience);
            explorerRepository.save(explorer);
            applicationRepository.save(application);
            experience.getExplorers().add(explorer);
            explorer.getExperiences().add(experience);
            experienceRepository.save(experience);
            explorerRepository.save(explorer);
        }else {
            throw new ApiException("Cannot accept this explorer because the experience is for "+experience.getAudienceType()+" only");
        }

    }

    public List<ExperienceApplicationOutDTO> getApplicationsByExperience(Integer myUserID,Integer experienceId) {
        Organizer organizer = organizerRepository.findOrganizerById(myUserID);
        if (organizer == null){
            throw new ApiException("Organizer not found");
        }
        Experience experience = experienceRepository.findExperienceById(experienceId);
        if (experience == null){
            throw new ApiException("Experience not found");
        }
        if (experience.getOrganizer().equals(organizer)) {
//            return changeApplicationsToOutDTO(applicationRepository.findAllByExperience(experience));
            return convertToExperienceApplicationsOutDTO (applicationRepository.findAllByExperience(experience));
        }else {
            throw new ApiException("Organizer does not have this Experience");
        }
    }
/*
    public List<ApplicationStatusOutDTO> getCompletedApplications(Integer organizerId,Integer experienceId) {
        Organizer organizer = organizerRepository.findOrganizerById(organizerId);
        if (organizer == null){
            throw new ApiException("Organizer not found");
        }
        Experience experience = experienceRepository.findExperienceById(experienceId);
        if (experience == null){
            throw new ApiException("Experience not found");
        }
        if (organizer.getExperiences().contains(experience)) {
            return changeApplicationsToOutDTO(applicationRepository.findAllByStatusAndExperience("Completed", experience));
        }else {
            throw new ApiException("Organizer does not have this Experience");
        }
    }
 */

    public List<ApplicationStatusOutDTO> getCompletedApplications(Integer myUserID, Integer experienceId) {
        Organizer organizer = organizerRepository.findOrganizerById(myUserID);
        if (organizer == null){
            throw new ApiException("Organizer not found");
        }
        Experience experience = experienceRepository.findExperienceById(experienceId);
        if (experience == null){
            throw new ApiException("Experience not found");
        }
        List<Application> applications = applicationRepository.findAllByStatusAndExperience("Completed", experience);
        Set<Application> applicationSet = new HashSet<Application>(applications);
        List<Explorer> explorers = explorerRepository.findAllByApplications(applicationSet);
        List<ExplorerStatusOutDTO> explorerStatusOutDTOS = new ArrayList<>();
        for (Explorer explorer : explorers) {
            MyUser user = authRepository.findMyUserById(explorer.getId());
            ExplorerStatusOutDTO explorerStatusOutDTO= new ExplorerStatusOutDTO(user.getName(),user.getAge(),user.getGender(),user.getHealthStatus());
            explorerStatusOutDTOS.add(explorerStatusOutDTO);
        }
        if (experience.getOrganizer().equals(organizer)) {
            return changeApplicationsToStatusOutDTO(applications,explorerStatusOutDTOS);
        }else {
            throw new ApiException("Organizer does not have this Experience");
        }
    }

    public List<ApplicationStatusOutDTO> getPendingApplications(Integer myUserID, Integer experienceId) {
        Organizer organizer = organizerRepository.findOrganizerById(myUserID);
        if (organizer == null){
            throw new ApiException("Organizer not found");
        }
        Experience experience = experienceRepository.findExperienceById(experienceId);
        if (experience == null){
            throw new ApiException("Experience not found");
        }
        List<Application> applications = applicationRepository.findAllByStatusAndExperience("Pending", experience);
        Set<Application> applicationSet = new HashSet<Application>(applications);
        List<Explorer> explorers = explorerRepository.findAllByApplications(applicationSet);
        List<ExplorerStatusOutDTO> explorerStatusOutDTOS = new ArrayList<>();
        for (Explorer explorer : explorers) {
            MyUser user = authRepository.findMyUserById(explorer.getId());
            ExplorerStatusOutDTO explorerStatusOutDTO= new ExplorerStatusOutDTO(user.getName(),user.getAge(),user.getGender(),user.getHealthStatus());
            explorerStatusOutDTOS.add(explorerStatusOutDTO);
        }
        if (experience.getOrganizer().equals(organizer)) {
            return changeApplicationsToStatusOutDTO(applications,explorerStatusOutDTOS);
        }else {
            throw new ApiException("Organizer does not have this Experience");
        }
    }

    public List<ApplicationStatusOutDTO> changeApplicationsToStatusOutDTO(List<Application> applications,List<ExplorerStatusOutDTO> explorerStatusOutDTOS) {
        List<ApplicationStatusOutDTO> applicationStatusOutDTOS = new ArrayList<>();
        for (int i = 0; i < applications.size(); i++) {
            applicationStatusOutDTOS.add(new ApplicationStatusOutDTO(applications.get(i).getDescription(),applications.get(i).getTools(),applications.get(i).getStatus(),applications.get(i).getIsMeetingZone(),explorerStatusOutDTOS.get(i)));

        }
        return applicationStatusOutDTOS;
    }

    public List<ExperienceSearchOutDTO> searchApplicationsByExperienceTitle(Integer myUserID, String title) {
        Explorer explorer = explorerRepository.findExplorerById(myUserID);
        if (explorer == null){
            throw new ApiException("Explorer not found");
        }

        List<Experience> experiences = new ArrayList<>();
        for (Experience experience: explorer.getExperiences()){
            experiences.add(experience);
        }
        List<Application> applications = applicationRepository.findAllByExperienceTitleContainingIgnoreCaseAndExplorer(title,explorer);
        List<Experience> experienceList = new ArrayList<>();
        for (int i = 0; i < applications.size(); i++) {
            if (applications.get(i).getExperience().getId() == experiences.get(i).getId()){
                experienceList.add(experiences.get(i));
            }
        }
        return changeExperienceToSearchOutDTO(experienceList);

    }

    public List<ExperienceSearchOutDTO> changeExperienceToSearchOutDTO(List<Experience> experiences){
        List<ExperienceSearchOutDTO> experienceOutDTOS = new ArrayList<>();
        for (Experience experience : experiences) {
            ExperienceSearchOutDTO experienceOutDTO = new ExperienceSearchOutDTO(experience.getTitle(),experience.getStartDate(),experience.getEndDate(),experience.getDescription(),experience.getStatus());
            experienceOutDTOS.add(experienceOutDTO);
        }
        return experienceOutDTOS;
    }

    public List<ExplorerApplicationOutDTO> changeToExplorerApplicationOutDTO (List<Application> applications){
        List<ExplorerApplicationOutDTO> explorerApplicationOutDTOS = new ArrayList<>();
        for (Application application : applications) {
            explorerApplicationOutDTOS.add(new ExplorerApplicationOutDTO(application.getExperience().getTitle(),
                    application.getExperience().getStartDate(),
                    application.getExperience().getEndDate(),application.getStatus()));
        }
        return explorerApplicationOutDTOS;
    }

    public List<ExperienceApplicationOutDTO> convertToExperienceApplicationsOutDTO (List<Application> applications){
        List<ExperienceApplicationOutDTO> experienceApplicationOutDTOS = new ArrayList<>();
        for (Application application : applications) {
            experienceApplicationOutDTOS.add(new ExperienceApplicationOutDTO(new BriefExplorerOutDTO(application.getExplorer().getMyUser().getName(),
                    application.getExplorer().getMyUser().getAge(),application.getExplorer().getMyUser().getHealthStatus(),
                    application.getExplorer().getMyUser().getGender(),application.getExplorer().getMyUser().getPhotoURL()) , changeApplicationsToOutDTO(application)));
        }
        return experienceApplicationOutDTOS;
    }
}



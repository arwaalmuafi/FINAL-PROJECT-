package com.example.finalprojectbond.Service;

import com.example.finalprojectbond.Api.ApiException;
import com.example.finalprojectbond.InDTO.ExperienceInDTO;
import com.example.finalprojectbond.Model.*;
import com.example.finalprojectbond.OutDTO.*;
import com.example.finalprojectbond.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final ExperiencePhotoRepository experiencePhotoRepository;
    private final OrganizerRepository organizerRepository;
    private final MeetingZoneRepository meetingZoneRepository;
    private final ExplorerRepository explorerRepository;
    private final ExplorerService explorerService;
    private final NotificationRepository notificationRepository;
    private final ApplicationRepository applicationRepository;
    private final AuthRepository authRepository;
    private final NotificationService notificationService;

    // method to get all Experiences
    public List<ExperienceOutDTO> findAll(Integer myUserId) {
        MyUser user = authRepository.findMyUserById(myUserId);
        if (user== null){
            throw new ApiException("User not found");
        }
        return convertToOutDTO(experienceRepository.findAll());
    }

    // Create Experience
    public void createExperience(Integer organizerId,ExperienceInDTO experienceInDTO) {
        Organizer organizer = organizerRepository.findOrganizerById(organizerId);
        if (organizer == null) {
            throw new ApiException("Organizer not found");
        }
        if (!organizer.getIsApproved()){
            throw new ApiException("You cannot create an experience because you are not approved!");
        }
        // check if two are in the same date
        List<Experience> overlappingExperiences = experienceRepository.findByOrganizerIdAndStatusNotInAndDatesOverlap(
                organizerId,
                List.of("Completed", "Canceled"),
                experienceInDTO.getStartDate(),
                experienceInDTO.getEndDate()
        );

        if (!overlappingExperiences.isEmpty()) {
            throw new ApiException("You already have another experience with overlapping dates.");
        }
        Experience experience = new Experience();
        experience.setTitle(experienceInDTO.getTitle());
        experience.setDescription(experienceInDTO.getDescription());
        experience.setCity(experienceInDTO.getCity());
        experience.setDifficulty(experienceInDTO.getDifficulty());
        experience.setStartDate(experienceInDTO.getStartDate());
        experience.setEndDate(experienceInDTO.getEndDate());
        experience.setAudienceType(experienceInDTO.getAudienceType());
        experience.setOrganizer(organizer);
        organizer.setNumberOfExperience(organizer.getNumberOfExperience()+1);
        experienceRepository.save(experience);

    }

    // update experience
    public void updateExperience(Integer myUserId, ExperienceInDTO experienceInDTO, Integer experienceId) {
        Experience experience = experienceRepository.findExperienceById(experienceId);
        Organizer organizer = organizerRepository.findOrganizerById(myUserId);
        if (experience == null) {
            throw new ApiException("Experience not found");
        }
        if (organizer == null) {
            throw new ApiException("Organizer not found");
        }
        if (!experience.getOrganizer().equals(organizer)) {
            throw new ApiException("Organizer not allowed to change status");
        }
        experience.setTitle(experienceInDTO.getTitle());
        experience.setDescription(experienceInDTO.getDescription());
        experience.setCity(experienceInDTO.getCity());
        experience.setDifficulty(experienceInDTO.getDifficulty());
        experience.setStartDate(experienceInDTO.getStartDate());
        experience.setEndDate(experienceInDTO.getEndDate());
        experience.setAudienceType(experienceInDTO.getAudienceType());
        experienceRepository.save(experience);
    }

    // Delete Experience
    public void deleteExperience(Integer myUserId,Integer experienceId) {
        MeetingZone meetingZone = meetingZoneRepository.findMeetingZoneById(experienceId);
        Experience experience = experienceRepository.findExperienceById(experienceId);
        Organizer organizer = organizerRepository.findOrganizerById(myUserId);
        if (experience == null) {
            throw new ApiException("Experience not found");
        }
        if (!experience.getOrganizer().equals(organizer)) {
            throw new ApiException("Organizer not allowed to change status");
        }
        if (meetingZone != null) {
            experience.setMeetingZone(null);
            meetingZone.setExperience(null);
        }
        experienceRepository.delete(experience);
    }

    public List<ExperienceOutDTO> convertToOutDTO(List<Experience> experienceList) {
        List<ExperienceOutDTO> experienceOutDTOList = new ArrayList<>();
        for (Experience experience : experienceList) {
            List<TagOutDTO> tagsOutDTO = experience.getTags().stream()
                    .map(tag -> new TagOutDTO(tag.getName()))
                    .collect(Collectors.toList());

            experienceOutDTOList.add(new ExperienceOutDTO(experience.getTitle(),
                    experience.getDescription(),
                    experience.getCity(),
                    experience.getStatus(),
                    experience.getStartDate(),
                    experience.getEndDate(),
                    experience.getDifficulty(),
                    experience.getAudienceType(),
                    convertPhotosToDTO(experiencePhotoRepository.findByExperienceId(experience.getId())),
                    tagsOutDTO

            ));
        }
        return experienceOutDTOList;
    }

    public List<ExperiencePhotoOutDTO> convertPhotosToDTO(List<ExperiencePhoto> experiencePhotoList) {
        List<ExperiencePhotoOutDTO> experiencePhotoOutDTOS = new ArrayList<>();
        for (ExperiencePhoto experiencePhoto : experiencePhotoList) {
            experiencePhotoOutDTOS.add(new ExperiencePhotoOutDTO(experiencePhoto.getPhotoUrl()));
        }
        return experiencePhotoOutDTOS;
    }

    public ExperienceOutDTO convertOneToOutDTO(Experience experience) {
        ExperienceOutDTO experienceOutDTO = new ExperienceOutDTO();

            List<TagOutDTO> tagsOutDTO = experience.getTags().stream()
                    .map(tag -> new TagOutDTO(tag.getName()))
                    .collect(Collectors.toList());

            experienceOutDTO.setTitle(experience.getTitle());
            experienceOutDTO.setDescription(experience.getDescription());
            experienceOutDTO.setCity(experience.getCity());
            experienceOutDTO.setDifficulty(experience.getDifficulty());
            experienceOutDTO.setStartDate(experience.getStartDate());
            experienceOutDTO.setEndDate(experience.getEndDate());
            experienceOutDTO.setAudienceType(experience.getAudienceType());
            experienceOutDTO.setStatus(experience.getStatus());
            experienceOutDTO.setAudienceType(experience.getAudienceType());
            experienceOutDTO.setTags(tagsOutDTO);
            experienceOutDTO.setPhotos(convertPhotosToDTO(experiencePhotoRepository.findByExperienceId(experience.getId())));
            return experienceOutDTO;
    }

    // method to change the status of experience to fully booked
    public void changeStatusToFullyBooked(Integer myUserId, Integer experienceId) {
        Organizer organizer = organizerRepository.findOrganizerById(myUserId);
        if (organizer == null) {
            throw new ApiException("Organizer not found");
        }
        Experience experience = experienceRepository.findExperienceById(experienceId);
        if (experience == null) {
            throw new ApiException("Experience not found");
        }

        if (!experience.getOrganizer().equals(organizer)) {
            throw new ApiException("Organizer not allowed to change status");
        }
        if (!experience.getStatus().equals("Accept Application")){
            throw new ApiException("Experience not allowed to change status");
        }

        experience.setStatus("Fully Booked");
        experienceRepository.save(experience);
    }

    // Method to remove explorer from experience
    public void RemoveExplorerFromExperience(Integer organizerId, Integer explorerId, Integer experienceId) {
        Organizer organizer = organizerRepository.findOrganizerById(organizerId);
        if (organizer == null) {
            throw new ApiException("Organizer not found");
        }
        Explorer explorer = explorerRepository.findExplorerById(explorerId);
        if (explorer == null) {
            throw new ApiException("Explorer not found");
        }
        Experience experience = experienceRepository.findExperienceById(experienceId);
        if (experience == null) {
            throw new ApiException("Experience not found");
        }
        if (!experience.getOrganizer().equals(organizer)) {
            throw new ApiException("Organizer not allowed to remove explorer because this experience is not his");
        }

        if (!experienceRepository.existsByExperienceIdAndExplorerId(experienceId, explorerId)) {
            throw new ApiException("Explorer is not a part of this experience");
        }

        List<Explorer> explorers = experienceRepository.findExplorersByExperienceId(experienceId);

        Set<Explorer> updatedExplorers = explorers.stream()
                .filter(explorerEx -> !explorer.getId().equals(explorerId))
                .collect(Collectors.toSet()); // Converted to a Set here


        experience.setExplorers(updatedExplorers);
        experienceRepository.save(experience);
        explorer.getExperiences().remove(experience);
        explorerRepository.save(explorer);
        Application application = applicationRepository.findApplicationByExperienceAndExplorer(experience, explorer);
        application.setStatus("Rejected");
        applicationRepository.save(application);
        if (experience.getExplorers().isEmpty()) {
            experience.setStatus("Canceled");
        }
        experienceRepository.save(experience);
    }


 public void removeAllNotConfirmedExplorersFromExperience(Integer organizerId, Integer experienceId) {
        Organizer organizer = organizerRepository.findOrganizerById(organizerId);
        if (organizer == null) {
            throw new ApiException("Organizer not found");
        }
        Experience experience = experienceRepository.findExperienceById(experienceId);
        if (experience == null) {
            throw new ApiException("Experience not found");
        }
        if (!experience.getOrganizer().equals(organizer)) {
            throw new ApiException("Organizer not allowed to remove anyone because this experience is not his");
        }

        List<Application> applications = applicationRepository.findAllByIsMeetingZoneAndExperience(false, experience);
        Set<Application> applications1 = new HashSet<>(applications);
        List<Explorer> explorers = explorerRepository.findAllByApplications(applications1);
        for (Explorer explorer : explorers) {
            RemoveExplorerFromExperience(organizerId,explorer.getId(), experienceId);
            experienceRepository.save(experience);
            Application application = applicationRepository.findApplicationByExperienceAndExplorer(experience, explorer);
            applicationRepository.save(application);
        }
//        Set<Explorer> explorerSet = new HashSet<>(explorers);
//        experience.setExplorers(explorerSet);
//        experienceRepository.save(experience);
//        if (!experience.getStatus().equalsIgnoreCase("Confirming")){
//            throw new ApiException("cannot remove Explorer in "+experience.getStatus()+" status");
//        }
        if (!experience.getExplorers().isEmpty()) {
            experience.setStatus("Task Assignment");
        }
//        else {
//            experience.setStatus("Canceled");
//        }
        experienceRepository.save(experience);
    }

// method to get all explorers of an experience that did not confirm the meeting zone
    public List<BriefExplorerOutDTO> getALlNonConfirmedExplorers (Integer organizerId, Integer experienceId) {
        Organizer organizer = organizerRepository.findOrganizerById(organizerId);
        if (organizer == null) {
            throw new ApiException("Organizer not found");
        }
        Experience experience = experienceRepository.findExperienceById(experienceId);
        if (experience == null) {
            throw new ApiException("Experience not found");
        }
        if (!experience.getOrganizer().equals(organizer)) {
            throw new ApiException("Organizer not allowed to remove anyone because this experience is not his");
        }

        List<Application> applications = applicationRepository.findAllByExperienceAndIsMeetingZoneAndStatus(experience,false,"Accepted");
        Set<Application> applications1 = new HashSet<>(applications);
        List<Explorer> explorers = explorerRepository.findAllByApplications(applications1);
        List<Explorer> confirmedExplorers = new ArrayList<>();
        return explorerService.getBriefExplorer(explorers);
    }

    // method to get All accepted explorers
    public List<BriefExplorerOutDTO> getAllAcceptedExplorers (Integer myUserId, Integer experienceId) {
        // ========================================================
        Organizer organizer = organizerRepository.findOrganizerById(myUserId);
        if (organizer == null) {
            throw new ApiException("Organizer not found");
        }
        Experience experience = experienceRepository.findExperienceById(experienceId);
        if (experience == null) {
            throw new ApiException("Experience not found");
        }
        if (!experience.getOrganizer().equals(organizer)) {
            throw new ApiException("Organizer not allowed to get accepted explorers because this experience is not his");
        }

        // ===========================================================================


        return explorerService.getBriefExplorer(experienceRepository.findAcceptedExplorersByExperienceId(experienceId));
    }

    // method to make the experience complete
    public void changeStatusToCompleted (Integer myUserId, Integer experienceId) {
        Organizer organizer = organizerRepository.findOrganizerById(myUserId);
        if (organizer == null) {
            throw new ApiException("Organizer not found");
        }
        Experience experience = experienceRepository.findExperienceById(experienceId);
        if (experience == null) {
            throw new ApiException("Experience not found");
        }
        if (!experience.getOrganizer().equals(organizer)) {
            throw new ApiException("this Organizer is not allowed to change status of the experience");
        }
        if (!experience.getStatus().equals("Active")){
             throw new ApiException("Experience not allowed to change status, status now is :"+experience.getStatus());
        }
        if (experience.getEndDate().isAfter(LocalDate.now())) {
            System.out.println("you can not complete experience before endpoint");
        }
        experience.setStatus("Completed");
        experienceRepository.save(experience);
    }

    // method to change the status of the experience to canceled and create notification
 public void cancelExperience(Integer organizerId, Integer experienceId) {
        Organizer organizer = organizerRepository.findOrganizerById(organizerId);
        if (organizer == null) {
            throw new ApiException("Organizer not found");
        }
        Experience experience = experienceRepository.findExperienceById(experienceId);
        if (experience == null) {
            throw new ApiException("Experience not found");
        }
        if (!experience.getOrganizer().equals(organizer)) {
            throw new ApiException("this Organizer is not allowed to cancel experience");
        }
        if (experience.getStatus().equals("Completed")){
            throw new ApiException("Experience is already completed");
        }
        if (experience.getStatus().equalsIgnoreCase("Active")) {
            throw new ApiException("can not cancel because the experience is Active");
        }
        experience.setStatus("Canceled");
        experienceRepository.save(experience);

        List<Explorer> explorers = experienceRepository.findExplorersByExperienceId(experienceId);

        for (Explorer explorer : explorers) {
            notificationService.notifyForCancelExperience( organizer.getId(), explorer.getId(), experience.getId());
            /*
            Notification notification = new Notification();
            notification.setExperience(experience);
            notification.setExplorer(explorer);
            notification.setMessage("Hello "+ explorer.getMyUser().getName()+" ,  \n" +
                    "The experience { "+experience.getTitle()+" } has been canceled. We apologize for the inconvenience.");

            notificationRepository.save(notification);
             */
        }
    }


    // method to search through experiences title
    public List<ExperienceSearchOutDTO> searchExperienceByTitle (String title ) {
        List<Experience> experiences = experienceRepository.findAllByTitleContainingIgnoreCase(title);
        List<ExperienceSearchOutDTO> experienceSearchOutDTOs = new ArrayList<>();
        for (Experience experience : experiences) {
            experienceSearchOutDTOs.add(new ExperienceSearchOutDTO(experience.getTitle(),experience.getStartDate(),experience.getEndDate(),experience.getDescription(),experience.getStatus()));
        }
        return experienceSearchOutDTOs;
    }

    // method to filter through experiences by city
    public List<ExperienceSearchOutDTO> filterByCity (String city ) {
        List<Experience> experiences = experienceRepository.findAllByCityIgnoreCase(city);
        List<ExperienceSearchOutDTO> experienceSearchOutDTOs = new ArrayList<>();
        for (Experience experience : experiences) {
            experienceSearchOutDTOs.add(new ExperienceSearchOutDTO(experience.getTitle(),experience.getStartDate(),experience.getEndDate(),experience.getDescription(),experience.getStatus()));
        }
        return experienceSearchOutDTOs;
    }

    // method to get more information of an experience
    public ExperienceOutDTO getMoreInfo (Integer myUserId) {
        Experience experience = experienceRepository.findExperienceById(myUserId);
        if (experience == null) {
            throw new ApiException("Experience not found");
        }
        ExperienceOutDTO experienceOutDTO = convertOneToOutDTO(experience);
        return experienceOutDTO;
    }

    // method to get experiences by tag
    public List<ExperienceSearchOutDTO> getExperiencesByTagName(String tagName) {
        List<Experience> experiences = experienceRepository.findExperiencesByTagName(tagName);
        if (experiences.isEmpty()) {
            throw new ApiException("No experiences found for the given tag");
        }
        List<ExperienceSearchOutDTO> experienceSearchOutDTOs = new ArrayList<>();
        for (Experience experience : experiences) {
            experienceSearchOutDTOs.add(new ExperienceSearchOutDTO(experience.getTitle(),experience.getStartDate(),experience.getEndDate(),experience.getDescription(),experience.getStatus()));
        }
        return experienceSearchOutDTOs;
    }

    public void changeStatusToInProgress (Integer organizerId, Integer experienceId) {
        Organizer organizer = organizerRepository.findOrganizerById(organizerId);
        if (organizer == null) {
            throw new ApiException("Organizer not found");
        }
        Experience experience = experienceRepository.findExperienceById(experienceId);
        if (experience == null) {
            throw new ApiException("Experience not found");
        }
        if (!experience.getOrganizer().equals(organizer)) {
            throw new ApiException("this Organizer is not allowed to change status of the experience");
        }
        if (!experience.getStatus().equals("Task Assignment")){
            throw new ApiException("Experience not allowed to change status");
        }

        experience.setStatus("In Progress");
        experienceRepository.save(experience);
    }
}

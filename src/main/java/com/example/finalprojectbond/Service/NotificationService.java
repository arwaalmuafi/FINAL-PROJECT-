package com.example.finalprojectbond.Service;

import com.example.finalprojectbond.Api.ApiException;
import com.example.finalprojectbond.InDTO.NotificationInDTO;
import com.example.finalprojectbond.Model.*;
import com.example.finalprojectbond.OutDTO.NotificationOutDTO;
import com.example.finalprojectbond.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final OrganizerRepository organizerRepository;
    private final ExplorerRepository explorerRepository;
    private final ExperienceRepository experienceRepository;
    private final ApplicationRepository applicationRepository;
    private final AuthRepository authRepository;
    private final EmailService emailService;

    public void notificationOneExplorer(Integer organizerId,Integer explorerId,Integer experienceId) {
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
        Application application = applicationRepository.findApplicationByExperienceAndExplorer(experience, explorer);
        if (application == null) {
            throw new ApiException("Application not found");
        }
        if (!experience.getOrganizer().equals(organizer)) {
            throw new ApiException("Organizer does not have this experience");
        }
        if (!experience.getExplorers().contains(explorer)) {
            throw new ApiException("Explorers does not have this experience");
        }

        //
        if (!experience.getStatus().equalsIgnoreCase("Confirming")) {
            throw new ApiException("the status of experience should be Confirming");
        }

        if (!application.getStatus().equalsIgnoreCase("Accepted")) {
            throw new ApiException("the status of application should be Accepted");
        }

        if (!application.getIsMeetingZone() == false){
            throw new ApiException("the meeting zone is Confirmed");
        }

        //
        if (experience.getMeetingZone() == null) {
            throw new ApiException("Meeting zone has not been set yet!");
        }

        Notification notification = new Notification("Action Needed: Confirm Your Meeting Zone","Organizer of: "+experience.getTitle());
        explorer.getNotifications().add(notification);
        notification.setExplorer(explorer);
        notification.setExperience(experience);
        notificationRepository.save(notification);
        notifyExplorerToConfirmMeetingZone(organizerId,explorerId,experienceId);
    }

public void notifyForCancelExperience(Integer organizerId,Integer explorerId,Integer experienceId) {
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
        Application application = applicationRepository.findApplicationByExperienceAndExplorer(experience, explorer);
        if (application == null) {
            throw new ApiException("Application not found");
        }
        if (!experience.getOrganizer().equals(organizer)) {
            throw new ApiException("Organizer does not have this experience");
        }
        if (!experience.getExplorers().contains(explorer)) {
            throw new ApiException("Explorers does not have this experience");
        }


        Notification notification = new Notification("Hello "+ explorer.getMyUser().getName()+" ,  \n" +
                "The experience { "+experience.getTitle()+" } has been canceled. We apologize for the inconvenience.","Organizer of: "+experience.getTitle());
        explorer.getNotifications().add(notification);
        notification.setExplorer(explorer);
        notification.setExperience(experience);
        notificationRepository.save(notification);
        notifyByEmailForCancelExperience(organizerId,explorerId,experienceId);
    }
    public List<NotificationOutDTO> getMyNotifications(Integer explorerId) {
        Explorer explorer = explorerRepository.findExplorerById(explorerId);
        if (explorer == null) {
            throw new ApiException("Explorer not found");
        }
        List<Notification> notifications = notificationRepository.findAllByExplorer(explorer);
        return changeNotificationToOutDTO(notifications);
    }

    public List<NotificationOutDTO> changeNotificationToOutDTO(List<Notification> notifications){
        List<NotificationOutDTO> notificationOutDTOs = new ArrayList<>();
        for (Notification notification : notifications) {
            notificationOutDTOs.add(new NotificationOutDTO(notification.getMessage(),notification.getTitle(),notification.getNotification_createAt(),notification.getExperience().getTitle()));
        }
        return notificationOutDTOs;
    }


    public void updateNotification(Integer organizerId ,Integer explorerId,Integer experienceId,NotificationInDTO notificationInDTO) {
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
        Application application = applicationRepository.findApplicationByExperienceAndExplorer(experience, explorer);
        if (application == null) {
            throw new ApiException("Application not found");
        }
        if (!experience.getOrganizer().equals(organizer)) {
            throw new ApiException("Organizer does not have this experience");
        }
        if (!experience.getExplorers().contains(explorer)) {
            throw new ApiException("Explorers does not have this experience");
        }

        if (!experience.getStatus().equalsIgnoreCase("Confirming")) {
            throw new ApiException("the status of experience should be Confirming");
        }

        if (!application.getStatus().equalsIgnoreCase("Accepted")) {
            throw new ApiException("the status of application should be Accepted");
        }

        if (!application.getIsMeetingZone() == false){
            throw new ApiException("the meeting zone is Confirmed");
        }


        Notification notification = notificationRepository.findNotificationByExplorerAndExperience(explorer,experience);
        if (notification == null) {
            throw new ApiException("Notification not found");
        }
        notification.setMessage(notificationInDTO.getMessage());
        notification.setTitle(notificationInDTO.getTitle());
        notificationRepository.save(notification);
    }

    public void deleteNotification(Integer organizerId ,Integer explorerId,Integer experienceId) {
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
        Application application = applicationRepository.findApplicationByExperienceAndExplorer(experience, explorer);
        if (application == null) {
            throw new ApiException("Application not found");
        }
        if (!experience.getOrganizer().equals(organizer)) {
            throw new ApiException("Organizer does not have this experience");
        }
        if (!experience.getExplorers().contains(explorer)) {
            throw new ApiException("Explorers does not have this experience");
        }

        if (!experience.getStatus().equalsIgnoreCase("Confirming")) {
            throw new ApiException("the status of experience should be Confirming");
        }

        if (!application.getStatus().equalsIgnoreCase("Accepted")) {
            throw new ApiException("the status of application should be Accepted");
        }


        Notification notification = notificationRepository.findNotificationByExplorerAndExperience(explorer,experience);
        if (notification == null) {
            throw new ApiException("Notification not found");
        }
        notificationRepository.delete(notification);
    }


    public void notifyExplorerToConfirmMeetingZone(Integer organizerId, Integer explorerId, Integer experienceId) {

        Organizer organizer = organizerRepository.findOrganizerById(organizerId);
        if (organizer == null) {
            throw new ApiException("Organizer not found");
        }

        Experience experience = experienceRepository.findExperienceById(experienceId);
        if (experience == null) {
            throw new ApiException("Experience not found");
        }

        Explorer explorer = explorerRepository.findExplorerById(explorerId);
        if (explorer == null) {
            throw new ApiException("Explorer not found");
        }

        Application application = applicationRepository.findApplicationByExperienceAndExplorer(experience, explorer);
        if (application == null) {
            throw new ApiException("Application not found");
        }

        if (!experience.getOrganizer().getId().equals(organizerId)) {
            throw new ApiException("Organizer does not own this experience");
        }

        if (!experience.getExplorers().contains(explorer)) {
            throw new ApiException("Explorer is not part of this experience");
        }

        //
        if (!application.getStatus().equalsIgnoreCase("Accepted")) {
            throw new ApiException("the status of application should be Accepted");
        }

        //
        if (!application.getIsMeetingZone() == false){
            throw new ApiException("the meeting zone is Confirmed");
        }

        //
        if (experience.getMeetingZone() == null) {
            throw new ApiException("Meeting zone has not been set yet!");
        }

        MyUser user = authRepository.findMyUserById(explorerId);
        if (user == null) {
            throw new ApiException("User not found");
        }
        String explorerEmail = user.getEmail();

        String subject = "Reminder: Confirm Your Meeting Zone for " + experience.getTitle();
        String text = "Dear " + user.getName() + ",\n\n" +
                "You are registered for the experience: '" + experience.getTitle() + "'.\n" +
                "Please confirm the meeting zone details as soon as possible.\n\n" +
                "Thank you for your participation!\n\n"+
                "Thank you,\n "+
                "Bond Team.";

        emailService.sendEmail(explorerEmail, subject, text);
    }

 public void notifyByEmailForCancelExperience(Integer organizerId, Integer explorerId, Integer experienceId) {

        Organizer organizer = organizerRepository.findOrganizerById(organizerId);
        if (organizer == null) {
            throw new ApiException("Organizer not found");
        }

        Experience experience = experienceRepository.findExperienceById(experienceId);
        if (experience == null) {
            throw new ApiException("Experience not found");
        }

        Explorer explorer = explorerRepository.findExplorerById(explorerId);
        if (explorer == null) {
            throw new ApiException("Explorer not found");
        }

        Application application = applicationRepository.findApplicationByExperienceAndExplorer(experience, explorer);
        if (application == null) {
            throw new ApiException("Application not found");
        }

        if (!experience.getOrganizer().getId().equals(organizerId)) {
            throw new ApiException("Organizer does not own this experience");
        }


        //
        //if (!application.getStatus().equalsIgnoreCase("Accepted")) {
        //    throw new ApiException("the status of application should be Accepted");
        //}

        MyUser user = authRepository.findMyUserById(explorerId);
        if (user == null) {
            throw new ApiException("User not found");
        }
        String explorerEmail = user.getEmail();

        String subject = "Important Update: '" + experience.getTitle() + "' Has Been Cancelled";
        String text = "Hi " + user.getName() + ",\n\n" +
                "We’re really sorry to let you know that the experience: '" + experience.getTitle() + "' has been cancelled.\n" +
                "We know this might be disappointing, and we truly apologize for any inconvenience.\n\n" +
                "If you have any questions or need help with anything, feel free to reach out.\n\n" +
                "Thanks for understanding!\n\n" +
                "Best,\n" +
                "The Bond Team";

        emailService.sendEmail(explorerEmail, subject, text);
    }
    public void notifyAllExplorerToConfirmMeetingZone(Integer organizerId,Integer experienceId){
        Organizer organizer = organizerRepository.findOrganizerById(organizerId);
        if (organizer == null) {
            throw new ApiException("Organizer not found");
        }

        Experience experience = experienceRepository.findExperienceById(experienceId);
        if (experience == null) {
            throw new ApiException("Experience not found");
        }

        if (!experience.getOrganizer().equals(organizer)) {
            throw new ApiException("Organizer does not own this experience");
        }

        if (!experience.getStatus().equalsIgnoreCase("Confirming")) {
            throw new ApiException("the status of experience should be Confirming");
        }

        if (experience.getMeetingZone() == null) {
            throw new ApiException("Meeting zone has not been set yet!");
        }

        List<Application> applications = applicationRepository.findAllByExperienceAndIsMeetingZoneAndStatus(experience,false,"Accepted");

        for (Application application:applications){
            Explorer explorer = application.getExplorer();
            notificationOneExplorer(organizerId,explorer.getId(),experienceId);
            notifyExplorerToConfirmMeetingZone(organizerId, explorer.getId(),experienceId);
        }
    }

}

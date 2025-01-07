package com.example.finalprojectbond.Service;

import com.example.finalprojectbond.Api.ApiException;
import com.example.finalprojectbond.InDTO.MeetingZoneInDTO;
import com.example.finalprojectbond.Model.Experience;
import com.example.finalprojectbond.Model.Explorer;
import com.example.finalprojectbond.Model.MeetingZone;
import com.example.finalprojectbond.Model.Organizer;
import com.example.finalprojectbond.OutDTO.MeetingZoneOutDTO;
import com.example.finalprojectbond.Repository.ExperienceRepository;
import com.example.finalprojectbond.Repository.ExplorerRepository;
import com.example.finalprojectbond.Repository.MeetingZoneRepository;
import com.example.finalprojectbond.Repository.OrganizerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingZoneService {
    private final MeetingZoneRepository meetingZoneRepository;
    private final OrganizerRepository organizerRepository;
    private final ExperienceRepository experienceRepository;
    private final ExplorerRepository explorerRepository;

    public MeetingZoneOutDTO getMyMeetingZone(Integer myUserId,Integer experienceId){
        Organizer organizer = organizerRepository.findOrganizerById(myUserId);
        MeetingZone meetingZone = meetingZoneRepository.findMeetingZoneByExperienceId(experienceId);
        if (organizer == null) {
            throw new ApiException("Organizer not found");
        }
        if (meetingZone == null) {
            throw new ApiException("Meeting Zone not found");
        }
        MeetingZoneOutDTO meetingZoneOutDTO = new MeetingZoneOutDTO(meetingZone.getLatitude(),meetingZone.getLongitude(),meetingZone.getLandMark());
        return meetingZoneOutDTO;
    }

    public void createMeetingZone(Integer myUserId,MeetingZoneInDTO meetingZoneInDTO) {
        Experience experience = experienceRepository.findExperienceById(meetingZoneInDTO.getExperienceId());
        if(experience==null) {
            throw new ApiException("Experience not found");
        }
        MeetingZone meetingZone = new MeetingZone(null,meetingZoneInDTO.getLatitude(),meetingZoneInDTO.getLongitude(),meetingZoneInDTO.getLandMark(),experience);
        Organizer organizer =organizerRepository.findOrganizerById(myUserId);
        if(organizer==null) {
            throw new ApiException("Organizer not found");
        }
        if (!experience.getStatus().equalsIgnoreCase("Fully Booked")){
            throw new ApiException("Experience is not Fully Booked");
        }
        if (experience.getOrganizer().getId() == organizer.getId()) {
            experience.setStatus("Confirming");
            experienceRepository.save(experience);
            meetingZoneRepository.save(meetingZone);
        }else {
            throw new ApiException("Organizer does not have this experience");
        }
    }

    public void updateMeetingZone(Integer myUserId,Integer experienceId,MeetingZoneInDTO meetingZoneInDTO){
        Organizer organizer =organizerRepository.findOrganizerById(myUserId);
        if(organizer==null) {
            throw new ApiException("Organizer not found");
        }
        Experience experience = experienceRepository.findExperienceById(experienceId);
        if(experience==null) {
            throw new ApiException("Experience not found");
        }
        MeetingZone meetingZone1 = meetingZoneRepository.findMeetingZoneById(experience.getMeetingZone().getId());
        if(meetingZone1==null) {
            throw new ApiException("MeetingZone not found");
        }
        if (experience.getOrganizer().getId() == organizer.getId()) {
            if (experience.getStatus().equalsIgnoreCase("Fully Booked")) {
                meetingZone1.setLatitude(meetingZoneInDTO.getLatitude());
                meetingZone1.setLongitude(meetingZoneInDTO.getLongitude());
                meetingZone1.setLandMark(meetingZoneInDTO.getLandMark());
                meetingZoneRepository.save(meetingZone1);
            }else {
                throw new ApiException("Experience status is not Fully Booked");
            }
        }else {
            throw new ApiException("Organizer does not have this experience");
        }
    }

    public void deleteMeetingZone(Integer myUserId,Integer experienceId) {
        Organizer organizer = organizerRepository.findOrganizerById(myUserId);
        Experience experience = experienceRepository.findExperienceById(experienceId);
        MeetingZone meetingZone = meetingZoneRepository.findMeetingZoneById(experience.getMeetingZone().getId());
        if(organizer==null) {
            throw new ApiException("Organizer not found");
        }
        if(experience==null) {
            throw new ApiException("Experience not found");
        }
        if(meetingZone==null) {
            throw new ApiException("MeetingZone not found");
        }
        if (experience.getOrganizer().getId() == organizer.getId()) {
            meetingZone.setExperience(null);
            meetingZoneRepository.delete(meetingZone);
        }else {
            throw new ApiException("Organizer does not have this experience");
        }
    }

    public MeetingZoneOutDTO getMeetingZoneForExplorerExperience(Integer myUserId,Integer experienceId){
        Explorer explorer = explorerRepository.findExplorerById(myUserId);
        MeetingZone meetingZone = meetingZoneRepository.findMeetingZoneByExperienceId(experienceId);
        Experience experience = experienceRepository.findExperienceById(experienceId);
        if (explorer == null) {
            throw new ApiException("Explorer not found");
        }
        if (meetingZone == null) {
            throw new ApiException("Meeting Zone not found");
        }
        if (explorer.getExperiences().contains(experience)) {
            MeetingZoneOutDTO meetingZoneOutDTO = new MeetingZoneOutDTO(meetingZone.getLatitude(),meetingZone.getLongitude(),meetingZone.getLandMark());
            return meetingZoneOutDTO;
        }else {
            throw new ApiException("Explorer does not have this experience");
        }

    }
}

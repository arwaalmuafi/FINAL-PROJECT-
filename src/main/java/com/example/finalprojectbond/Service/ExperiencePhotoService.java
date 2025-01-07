package com.example.finalprojectbond.Service;

import com.example.finalprojectbond.Api.ApiException;
import com.example.finalprojectbond.InDTO.ExperiencePhotoInDTO;
import com.example.finalprojectbond.Model.Experience;
import com.example.finalprojectbond.Model.ExperiencePhoto;
import com.example.finalprojectbond.Model.Organizer;
import com.example.finalprojectbond.OutDTO.ExperiencePhotoOutDTO;
import com.example.finalprojectbond.Repository.ExperiencePhotoRepository;
import com.example.finalprojectbond.Repository.ExperienceRepository;
import com.example.finalprojectbond.Repository.OrganizerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExperiencePhotoService {

    private final ExperiencePhotoRepository experiencePhotoRepository;
    private final ExperienceRepository experienceRepository;
    private final OrganizerRepository organizerRepository;
    private final FirebaseStorageService firebaseStorageService;

    // Method to get all experiences photos
    public List<ExperiencePhotoOutDTO> getAllExperiencePhotos() {
        return convertExperiencePhotoToExperiencePhotoOutDTO(experiencePhotoRepository.findAll());
    }

    // method to add photo to an experience
    public void addExperiencePhoto(Integer myUserId,ExperiencePhotoInDTO experiencePhotoInDTO) {
        Organizer organizer = organizerRepository.findOrganizerById(myUserId);
        if (organizer == null) {
            throw new ApiException("Organizer not found");
        }
        Experience experience = experienceRepository.findExperienceById(experiencePhotoInDTO.getExperienceId());
        if (experience == null) {
            throw new ApiException("Experience not found");
        }

        if (!experience.getOrganizer().equals(organizer)) {
            throw new ApiException("You are not the organizer for this experience!");
        }
        ExperiencePhoto experiencePhoto = new ExperiencePhoto();
        experiencePhoto.setPhotoUrl(experiencePhotoInDTO.getPhotoUrl());
        experiencePhoto.setExperience(experience);
        experiencePhotoRepository.save(experiencePhoto);
    }

    // delete experience photo
    public void deleteExperiencePhoto(Integer experiencePhotoId) {
        ExperiencePhoto experiencePhoto = experiencePhotoRepository.findExperiencePhotoById(experiencePhotoId);
        if (experiencePhoto == null) {
            throw new ApiException("Experience photo not found");
        }
        firebaseStorageService.deleteFile(experiencePhoto.getPhotoUrl());
        experiencePhotoRepository.delete(experiencePhoto);
    }

    public List<ExperiencePhotoOutDTO> getExperiencePhotosByExperience(Integer experienceId) {
        Experience experience = experienceRepository.findExperienceById(experienceId);
        if (experience == null) {
            throw new ApiException("Experience not found");
        }
        return convertExperiencePhotoToExperiencePhotoOutDTO(experiencePhotoRepository.findExperiencePhotoByExperienceId(experienceId));
    }

    public List<ExperiencePhotoOutDTO> convertExperiencePhotoToExperiencePhotoOutDTO(List<ExperiencePhoto> experiencePhotos) {
        List<ExperiencePhotoOutDTO> experiencePhotoOutDTOS = new ArrayList<>();
        for (ExperiencePhoto experiencePhoto : experiencePhotos) {
            experiencePhotoOutDTOS.add(new ExperiencePhotoOutDTO(experiencePhoto.getPhotoUrl()));
        }
        return experiencePhotoOutDTOS;
    }
}

package com.example.finalprojectbond.Controller;

import com.example.finalprojectbond.Api.ApiResponse;
import com.example.finalprojectbond.InDTO.ExperiencePhotoInDTO;
import com.example.finalprojectbond.Model.ExperiencePhoto;
import com.example.finalprojectbond.Model.MyUser;
import com.example.finalprojectbond.OutDTO.ExperiencePhotoOutDTO;
import com.example.finalprojectbond.Service.ExperiencePhotoService;
import com.example.finalprojectbond.Service.FirebaseStorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/experience-photo")
@RequiredArgsConstructor
public class ExperiencePhotoController {

    private final ExperiencePhotoService experiencePhotoService;
    private final FirebaseStorageService firebaseStorageService;

    @GetMapping("/get-all-experience-photo")
    public ResponseEntity<List<ExperiencePhotoOutDTO>> findAll() {
        return ResponseEntity.status(200).body(experiencePhotoService.getAllExperiencePhotos());
    }

    //Ahmed
    // ENDPOINT TO ADD PHOTOS TO AN EXPERIENCE (SEND IT TO FIREBASE)
    @PostMapping("/add-photo")
    public ResponseEntity<ApiResponse> addExperiencePhoto(@AuthenticationPrincipal MyUser myUser, @RequestParam("file") MultipartFile file,
                                                          @RequestParam("experienceId") Integer experienceId) throws IOException {
        String fileUrl = firebaseStorageService.uploadFile(file);
        ExperiencePhotoInDTO dto = new ExperiencePhotoInDTO(experienceId, fileUrl);
        experiencePhotoService.addExperiencePhoto(myUser.getId(),dto);
        return ResponseEntity.status(200).body(new ApiResponse("Successfully added experience photo"));
    }

    //Ahmed
    // ------------------  ------------------
    @DeleteMapping("/delete-photo/experience-{experienceId}")
    public ResponseEntity<ApiResponse> deleteExperiencePhoto(@PathVariable Integer experienceId) {
        experiencePhotoService.deleteExperiencePhoto(experienceId);
        return ResponseEntity.status(200).body(new ApiResponse("Successfully deleted photo"));
    }

    //Ahmed
    // ENDPOINT TO GET ALL PHOTOS OF AN EXPERIENCE
    @GetMapping("/get-experience-photos/experience-{experienceId}")
    public ResponseEntity<List<ExperiencePhotoOutDTO>> getAllExperiencePhotos(@PathVariable Integer experienceId) {
        return ResponseEntity.status(200).body(experiencePhotoService.getExperiencePhotosByExperience(experienceId));
    }



}

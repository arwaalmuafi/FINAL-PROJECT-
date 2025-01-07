package com.example.finalprojectbond.Controller;

import com.example.finalprojectbond.Api.ApiResponse;
import com.example.finalprojectbond.InDTO.ExperienceInDTO;
import com.example.finalprojectbond.Model.MyUser;
import com.example.finalprojectbond.OutDTO.BriefExplorerOutDTO;
import com.example.finalprojectbond.OutDTO.ExperienceOutDTO;
import com.example.finalprojectbond.OutDTO.ExperienceSearchOutDTO;
import com.example.finalprojectbond.Service.ExperienceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/experience")
@RequiredArgsConstructor
public class ExperienceController {

    private final ExperienceService experienceService;

    @GetMapping("/get-all-experience")
    public ResponseEntity<List<ExperienceOutDTO>> getAllExperiences(@AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(experienceService.findAll(myUser.getId()));
    }

    @PostMapping("/create-experience")
    public ResponseEntity<ApiResponse> createExperience(@AuthenticationPrincipal MyUser myUser,@RequestBody @Valid ExperienceInDTO experienceInDTO) {
        experienceService.createExperience(myUser.getId(),experienceInDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Experience created successfully"));
    }

    @PutMapping("/update/experience-{experienceId}")
    public ResponseEntity<ApiResponse> updateExperience(@AuthenticationPrincipal MyUser myUser, @RequestBody @Valid ExperienceInDTO experienceInDTO,@PathVariable Integer experienceId) {
        experienceService.updateExperience(myUser.getId(),experienceInDTO,experienceId);
        return ResponseEntity.status(200).body(new ApiResponse("Experience updated successfully"));
    }

    @DeleteMapping("/delete/experience-{experienceId}")
    public ResponseEntity<ApiResponse> deleteExperience(@AuthenticationPrincipal MyUser myUser, @PathVariable Integer experienceId) {
        experienceService.deleteExperience(myUser.getId(),experienceId);
        return ResponseEntity.status(200).body(new ApiResponse("Experience deleted successfully"));
    }

    //Ahmed
    // ENDPOINT TO FULLY BOOK AN EXPERIENCE
    @PutMapping("/fully-booked/experience-{experienceId}")
    public ResponseEntity<ApiResponse> fullyBookedExperience(@AuthenticationPrincipal MyUser myUser ,@PathVariable Integer experienceId) {
        experienceService.changeStatusToFullyBooked(myUser.getId(),experienceId);
        return ResponseEntity.status(200).body(new ApiResponse("Experience is now Fully Booked"));
    }

    //Ahmed
    // ENDPOINT TO REMOVE EXPLORER FROM EXPERIENCE
    @DeleteMapping("/remove-explorer/explorer-{explorerId}/experience-{experienceId}")
    public ResponseEntity<ApiResponse> removeExplorerFromExperience(@AuthenticationPrincipal MyUser myUser,@PathVariable Integer explorerId,@PathVariable Integer experienceId) {
        experienceService.RemoveExplorerFromExperience(myUser.getId(),explorerId,experienceId);
        return ResponseEntity.status(200).body(new ApiResponse("Explorer is now Removed"));
    }

    //Ahmed
    // ENDPOINT TO GET ALL THE EXPLORER WHO DID NOT CONFIRM THE MEETING ZONE
    @GetMapping("/get-non-confirmed/experience-{experienceId}")
    public ResponseEntity<List<BriefExplorerOutDTO>> getAllNonConfirmedExplorersInExperience(@AuthenticationPrincipal MyUser myUser, @PathVariable Integer experienceId) {
        return ResponseEntity.status(200).body(experienceService.getALlNonConfirmedExplorers(myUser.getId(),experienceId));
    }

    //Ahmed
    // ENDPOINT TO REMOVE ALL EXPLORER WHO DID NOT CONFIRM THE MEETING ZONE
    @DeleteMapping("/remove-non-confirmed/experience-{experienceId}")
    public ResponseEntity<ApiResponse> removeAllNonConfirmedExplorersFromExperience(@AuthenticationPrincipal MyUser myUser,@PathVariable Integer experienceId) {
        experienceService.removeAllNotConfirmedExplorersFromExperience(myUser.getId(),experienceId);
        return ResponseEntity.status(200).body(new ApiResponse("All Explorers who did not confirm meeting zone are removed"));
    }


    //Ahmed
    // ENDPOINT TO GET ALL THE ACCEPTED EXPLORERS IN AN EXPERIENCE
    @GetMapping("/get-accepted/experience-{experienceId}")
    public ResponseEntity<List<BriefExplorerOutDTO>> getAllAcceptedExplorersInExperience(@AuthenticationPrincipal MyUser myUser, @PathVariable Integer experienceId) {
        return ResponseEntity.status(200).body(experienceService.getAllAcceptedExplorers(myUser.getId(),experienceId));
    }

    //Ahmed
    //ENDPOINT TO MAKE THE STATUS OF THE EXPERIENCE COMPLETE
    @PutMapping("/complete/experience-{experienceId}")
    public ResponseEntity<ApiResponse> completeExperience(@AuthenticationPrincipal MyUser myUser,@PathVariable Integer experienceId) {
        experienceService.changeStatusToCompleted(myUser.getId(),experienceId);
        return ResponseEntity.status(200).body(new ApiResponse("Experience is now completed"));
    }

    //Ahmed
    //ENDPOINT TO CANCEL AN EXPERIENCE
    @PutMapping("/cancel/experience-{experienceId}")
    public ResponseEntity<ApiResponse> cancelExperience(@AuthenticationPrincipal MyUser myUser,@PathVariable Integer experienceId) {
        experienceService.cancelExperience(myUser.getId(),experienceId);
        return ResponseEntity.status(200).body(new ApiResponse("Experience is now cancel"));
    }

    //Ahmed
    // ENDPOINT TO SEARCH EXPERIENCE BY TITLE
    @GetMapping("/search-experience-by/title-{title}")
    public ResponseEntity<List<ExperienceSearchOutDTO>> searchExperienceByTitle(@PathVariable String title) {
        return ResponseEntity.status(200).body(experienceService.searchExperienceByTitle(title));
    }

    //Ahmed
    // ENDPOINT TO GET ALL EXPERIENCE BY CITY
    @GetMapping("/filter-experience-by/city-{city}")
    public ResponseEntity<List<ExperienceSearchOutDTO>> filterByCity(@PathVariable String city) {
        return ResponseEntity.status(200).body(experienceService.filterByCity(city));
    }

    //Ahmed
    // ENDPOINT TO GET MORE INFO OF AN EXPERIENCE
    @GetMapping("/get-experience-more-info/experience-{experienceId}")
    public ResponseEntity<ExperienceOutDTO> getMoreInfo(@PathVariable Integer experienceId) {
        return ResponseEntity.status(200).body(experienceService.getMoreInfo(experienceId));
    }

    //Ahmed
    // ENDPOINT TO GET ALL EXPERIENCES BY TAG NAMES
    @GetMapping("/get-experience-by/tag-{tagName}")
    public ResponseEntity<List<ExperienceSearchOutDTO>> getExperiencesByTagName(@PathVariable String tagName) {
        return ResponseEntity.status(200).body(experienceService.getExperiencesByTagName(tagName));
    }

    //Ahmed
    @PutMapping("/change-status-to-in-progress/{experienceId}")
    public ResponseEntity changeStatusToInProgress (@AuthenticationPrincipal MyUser myUser,@PathVariable Integer experienceId) {
        experienceService.changeStatusToInProgress(myUser.getId(),experienceId);
        return ResponseEntity.status(200).body(new ApiResponse("Experience is now in-progress"));
    }
}

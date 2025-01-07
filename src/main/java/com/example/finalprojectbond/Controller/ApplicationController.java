package com.example.finalprojectbond.Controller;

import com.example.finalprojectbond.Api.ApiResponse;
import com.example.finalprojectbond.InDTO.ApplicationInDTO;
import com.example.finalprojectbond.Model.MyUser;
import com.example.finalprojectbond.OutDTO.ApplicationOutDTO;
import com.example.finalprojectbond.OutDTO.ExplorerApplicationOutDTO;
import com.example.finalprojectbond.Service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/application")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @GetMapping("/get-my-applications")
    public ResponseEntity<List<ExplorerApplicationOutDTO>> getMyApplications(@AuthenticationPrincipal MyUser myUser ) {
        return ResponseEntity.status(200).body(applicationService.getMyApplications(myUser.getId()));
    }

    @PostMapping("/create-application/experience-{experienceId}")
    public ResponseEntity<ApiResponse> createApplication(@AuthenticationPrincipal MyUser myUser, @PathVariable Integer experienceId, @RequestBody @Valid ApplicationInDTO applicationInDTO){
        applicationService.createApplication(myUser.getId(),experienceId,applicationInDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Application created"));
    }

    @PutMapping("/update-application/experience-{experienceId}/application-{applicationId}")
    public ResponseEntity updateApplication(@AuthenticationPrincipal MyUser myUser,@PathVariable Integer experienceId,@PathVariable Integer applicationId,@RequestBody @Valid ApplicationInDTO applicationInDTO){
        applicationService.updateApplication(myUser.getId(),experienceId,applicationId,applicationInDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Application updated"));
    }

    @DeleteMapping("/cancel-application/experience-{experienceId}/application-{applicationId}")
    public ResponseEntity<ApiResponse>  cancelApplication(@AuthenticationPrincipal MyUser myUser,@PathVariable Integer experienceId,@PathVariable Integer applicationId){
        applicationService.cancelApplication(myUser.getId(),experienceId,applicationId);
        return ResponseEntity.status(200).body(new ApiResponse("Application cancelled"));
    }
    //Hashim
    @PutMapping("/reject-application/explorer-{explorerId}/application-{applicationId}")
    public ResponseEntity<ApiResponse>  rejectApplication(@AuthenticationPrincipal MyUser myUser,@PathVariable Integer explorerId,@PathVariable Integer applicationId) {
        applicationService.rejectApplication(myUser.getId(),explorerId,applicationId);
        return ResponseEntity.status(200).body(new ApiResponse("Application rejected"));
    }
    //Hashim
    @PutMapping("/accept-application/explorer-{explorerId}/application-{applicationId}")
    public ResponseEntity<ApiResponse>  acceptApplication(@AuthenticationPrincipal MyUser myUser,@PathVariable Integer explorerId,@PathVariable Integer applicationId) {
        applicationService.acceptApplication(myUser.getId(),explorerId,applicationId);
        return ResponseEntity.status(200).body(new ApiResponse("Application accepted"));
    }
    //Hashim
    @GetMapping("/get-applications-by-experience/experience-{experienceId}")
    public ResponseEntity getApplicationsByExperience(@AuthenticationPrincipal MyUser myUser ,@PathVariable Integer experienceId) {
        return ResponseEntity.status(200).body(applicationService.getApplicationsByExperience(myUser.getId(),experienceId));
    }
    //Hashim
    @GetMapping("/get-completed-applications/experience-{experienceId}")
    public ResponseEntity getCompletedApplications(@AuthenticationPrincipal MyUser myUser,@PathVariable Integer experienceId) {
        return ResponseEntity.status(200).body(applicationService.getCompletedApplications(myUser.getId(),experienceId));
    }
    //Hashim
    @GetMapping("/get-pending-applications/experience-{experienceId}")
    public ResponseEntity getPendingApplications(@AuthenticationPrincipal MyUser myUser,@PathVariable Integer experienceId) {
        return ResponseEntity.status(200).body(applicationService.getPendingApplications(myUser.getId(),experienceId));
    }
    //Hashim
    @GetMapping("/search-applications-by-experience-title/{title}")
    public ResponseEntity searchApplicationsByExperienceTitle(@AuthenticationPrincipal MyUser myUser,@PathVariable String title){
        return ResponseEntity.status(200).body(applicationService.searchApplicationsByExperienceTitle(myUser.getId(),title));
    }
}

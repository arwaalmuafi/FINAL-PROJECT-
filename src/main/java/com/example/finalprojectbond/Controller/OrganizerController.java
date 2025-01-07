package com.example.finalprojectbond.Controller;

import com.example.finalprojectbond.Api.ApiResponse;
import com.example.finalprojectbond.InDTO.OrganizerInDTO;
import com.example.finalprojectbond.Model.MyUser;
import com.example.finalprojectbond.Service.OrganizerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/organizer")
@RequiredArgsConstructor
public class OrganizerController {
    private final OrganizerService organizerService;

    @GetMapping("/get-my-organizer")
    public ResponseEntity getMyOrganizer(@AuthenticationPrincipal MyUser myUser){
        return ResponseEntity.status(200).body(organizerService.getMyOrganizer(myUser.getId()));
    }

    @PostMapping("/register-organizer")
    public ResponseEntity registerOrganizer(@RequestBody  OrganizerInDTO organizerInDTO) {
        organizerService.registerOrganizer(organizerInDTO);
        return ResponseEntity.status(200).body(new ApiResponse("organizer registered"));
    }

    @PutMapping("/update-my-organizer")
    public ResponseEntity updateOrganizer(@AuthenticationPrincipal MyUser myUser,@RequestBody @Valid OrganizerInDTO organizerInDTO) {
        organizerService.updateOrganizer(myUser.getId(),organizerInDTO);
        return ResponseEntity.status(200).body(new ApiResponse("organizer updated"));
    }

    @DeleteMapping("/delete-my-organizer")
    public ResponseEntity deleteMyOrganizer(@AuthenticationPrincipal MyUser myUser) {
        organizerService.deleteMyOrganizer(myUser.getId());
        return ResponseEntity.status(200).body(new ApiResponse("organizer deleted"));
    }
    //Hashim
    @GetMapping("/get-organizer-by-rating-asc")
    public ResponseEntity getOrganizerByRatingAsc(){
        return ResponseEntity.status(200).body(organizerService.getOrganizerByRatingAsc());
    }
    //Hashim
    @GetMapping("/get-organizer-by-rating-desc")
    public ResponseEntity getOrganizerByRatingDesc(){
        return ResponseEntity.status(200).body(organizerService.getOrganizerByRatingDesc());
    }

    //Hashim
    @GetMapping("/get-organizer-by-city/city-{city}")
    public ResponseEntity getOrganizerByCity(@PathVariable String city){
        return ResponseEntity.status(200).body(organizerService.getOrganizerByCity(city));
    }
    //Hashim
    @GetMapping("/get-all-organizer-experiences")
    public ResponseEntity getAllOrganizerExperiences(@AuthenticationPrincipal MyUser myUser){
        return ResponseEntity.status(200).body(organizerService.getAllOrganizerExperiences(myUser.getId()));
    }
    //Hashim
    @GetMapping("/search-experience-by-title/title-{title}") // this should only search thorugh the organizer experiences
    public ResponseEntity searchExperienceByTitle(@AuthenticationPrincipal MyUser myUser,@PathVariable String title){
        return ResponseEntity.status(200).body(organizerService.searchExperienceByTitle(myUser.getId(),title));
    }

}

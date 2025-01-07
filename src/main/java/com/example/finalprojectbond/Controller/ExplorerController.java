package com.example.finalprojectbond.Controller;

import com.example.finalprojectbond.Api.ApiResponse;
import com.example.finalprojectbond.InDTO.ExplorerInDTO;
import com.example.finalprojectbond.Model.MyUser;
import com.example.finalprojectbond.OutDTO.BriefExplorerOutDTO;
import com.example.finalprojectbond.OutDTO.ExperienceSearchOutDTO;
import com.example.finalprojectbond.OutDTO.ExplorerOutDTO;
import com.example.finalprojectbond.Service.ExplorerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/explorer")
@RequiredArgsConstructor
public class ExplorerController {

    private final ExplorerService explorerService;

    @GetMapping("/get-all-explorer")
    public ResponseEntity<List<ExplorerOutDTO>> getAllExplorers(@AuthenticationPrincipal MyUser user) {
        return ResponseEntity.status(200).body(explorerService.getAllExplorers(user.getId()));
    }

    @PostMapping("/register-explorer")
    public ResponseEntity<ApiResponse> registerExplorer(@RequestBody  ExplorerInDTO explorerInDTO) {
        explorerService.registerExplorer(explorerInDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Explorer registered successfully"));
    }

    @PutMapping("/update-explorer")
    public ResponseEntity<ApiResponse> updateExplorer(@AuthenticationPrincipal MyUser myUser,@RequestBody @Valid ExplorerInDTO explorerInDTO) {
        explorerService.updateExplorer(myUser.getId(),explorerInDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Explorer updated successfully"));
    }

    @DeleteMapping("/delete-explorer")
    public ResponseEntity<ApiResponse> deleteExplorer(@AuthenticationPrincipal MyUser myUser) {
        explorerService.deleteExplorer(myUser.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Explorer deleted successfully"));
    }

    //Hashim
    @PutMapping("/confirm-meeting-zone/experience-{experienceId}")
    public ResponseEntity confirmMeetingZone(@AuthenticationPrincipal MyUser myUser,@PathVariable Integer experienceId){
        explorerService.confirmMeetingZone(myUser.getId(),experienceId);
        return ResponseEntity.status(200).body(new ApiResponse("Explorer confirmed meeting zone successfully"));
    }

    //Ahmed
    // ENDPOINT TO GET ALL THE EXPERIENCES THE EXPLORER IS A PART OF
    @GetMapping("/get-my-experiences")
    public ResponseEntity<List<ExperienceSearchOutDTO>> getMyExperiences(@AuthenticationPrincipal MyUser myUser){
        return ResponseEntity.status(200).body(explorerService.getParticipatedInExperiences(myUser.getId()));
    }


}

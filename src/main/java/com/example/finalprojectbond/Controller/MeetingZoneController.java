package com.example.finalprojectbond.Controller;

import com.example.finalprojectbond.Api.ApiResponse;
import com.example.finalprojectbond.InDTO.MeetingZoneInDTO;
import com.example.finalprojectbond.Model.MeetingZone;
import com.example.finalprojectbond.Model.MyUser;
import com.example.finalprojectbond.Service.MeetingZoneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/meeting-zone")
@RequiredArgsConstructor
public class MeetingZoneController {
    private final MeetingZoneService meetingZoneService;

    @GetMapping("/get-my-meeting-zone/experience-{experienceId}")
    public ResponseEntity getMyMeetingZone(@AuthenticationPrincipal MyUser myUser, @PathVariable Integer experienceId){
        return ResponseEntity.status(200).body(meetingZoneService.getMyMeetingZone(myUser.getId(),experienceId));
    }

    @PostMapping("/create-meeting-zone")
    public ResponseEntity createMeetingZone(@AuthenticationPrincipal MyUser myUser,@RequestBody @Valid MeetingZoneInDTO meetingZoneInDTO){
        meetingZoneService.createMeetingZone(myUser.getId(),meetingZoneInDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Meeting Zone created"));
    }

    @PutMapping("/update-meeting-zone/experience-{experienceId}")
    public ResponseEntity updateMeetingZone(@AuthenticationPrincipal MyUser myUser,@PathVariable Integer experienceId,@RequestBody @Valid MeetingZoneInDTO meetingZoneInDTO){
        meetingZoneService.updateMeetingZone(myUser.getId(),experienceId,meetingZoneInDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Meeting Zone updated"));
    }

    @DeleteMapping("/delete-meeting-zone/experience-{experienceId}")
    public ResponseEntity deleteMeetingZone(@AuthenticationPrincipal MyUser myUser,@PathVariable Integer experienceId) {
        meetingZoneService.deleteMeetingZone(myUser.getId(),experienceId);
        return ResponseEntity.status(200).body(new ApiResponse("Meeting Zone deleted"));
    }

    @GetMapping("/get-meeting-zone-for-explorer-experience/experience-{experienceId}")
    public ResponseEntity getMeetingZoneForExplorerExperience(@AuthenticationPrincipal MyUser myUser,@PathVariable Integer experienceId){
        return ResponseEntity.status(200).body(meetingZoneService.getMeetingZoneForExplorerExperience(myUser.getId(),experienceId));
    }
}

package com.example.finalprojectbond.Controller;

import com.example.finalprojectbond.Api.ApiResponse;
import com.example.finalprojectbond.InDTO.NotificationInDTO;
import com.example.finalprojectbond.Model.MyUser;
import com.example.finalprojectbond.Service.NotificationService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;


    //Hashim
    @PostMapping("/notification-one-explorer/explorer-{explorerId}/experience-{experienceId}")
    public ResponseEntity notifiyOneExplorer(@AuthenticationPrincipal MyUser myUser, @PathVariable Integer explorerId, @PathVariable Integer experienceId) {
        notificationService.notificationOneExplorer(myUser.getId(),explorerId,experienceId);
        return ResponseEntity.status(200).body(new ApiResponse("notification one Explorer send"));
    }

    @GetMapping("/get-my-notifications")
    public ResponseEntity getMyNotifications(@AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(notificationService.getMyNotifications(myUser.getId()));
    }

    @PutMapping("/update-notification/explorer-{explorerId}/experience-{experienceId}")
    public ResponseEntity updateNotification(@AuthenticationPrincipal MyUser myUser,@PathVariable Integer explorerId,@PathVariable Integer experienceId,@RequestBody @Valid NotificationInDTO notificationInDTO) {
        notificationService.updateNotification(myUser.getId(),explorerId,experienceId,notificationInDTO);
        return ResponseEntity.status(200).body(new ApiResponse("notification update success"));
    }

    @DeleteMapping("/delete-notification/explorer-{explorerId}/experience-{experienceId}")
    public ResponseEntity deleteNotification(@AuthenticationPrincipal MyUser myUser,@PathVariable Integer explorerId,@PathVariable Integer experienceId) {
        notificationService.deleteNotification(myUser.getId(),explorerId,experienceId);
        return ResponseEntity.status(200).body(new ApiResponse("notification delete success"));
    }

    //Hashim
    @PostMapping("/notify-all-explorer-to-confirm-meeting-zone/experience-{experienceId}")
    public ResponseEntity notifyAllExplorerToConfirmMeetingZone(@AuthenticationPrincipal MyUser myUser,@PathVariable Integer experienceId){
        notificationService.notifyAllExplorerToConfirmMeetingZone(myUser.getId(),experienceId);
        return ResponseEntity.status(200).body(new ApiResponse("notification sent to all Explorer does not confirm meeting zone"));
    }
}

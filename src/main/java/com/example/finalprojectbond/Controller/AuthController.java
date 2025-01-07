package com.example.finalprojectbond.Controller;

import com.example.finalprojectbond.Api.ApiResponse;
import com.example.finalprojectbond.Model.MyUser;
import com.example.finalprojectbond.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    //Hashim
    @PutMapping("/approve-organizer/organizer-{organizerId}")
    public ResponseEntity approveOrganizer(@AuthenticationPrincipal MyUser myUser ,@PathVariable Integer organizerId){
        authService.approveOrganizer(myUser.getId(),organizerId);
        return ResponseEntity.status(200).body(new ApiResponse("Organizer Approved"));
    }
}

package com.example.finalprojectbond.Controller;

import com.example.finalprojectbond.Api.ApiResponse;
import com.example.finalprojectbond.Model.MyUser;
import com.example.finalprojectbond.Model.ReviewExplorer;
import com.example.finalprojectbond.OutDTO.ReviewExplorerOutDTO;
import com.example.finalprojectbond.Repository.ReviewExplorerRepository;
import com.example.finalprojectbond.Service.ReviewExperienceService;
import com.example.finalprojectbond.Service.ReviewExplorerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/review-explorer")
@RequiredArgsConstructor
public class ReviewExplorerController {

    private final ReviewExplorerService reviewExplorerService;

    //3
    @GetMapping("/get-all-reviews-explorer")
    public ResponseEntity getAllReviewsByExplorer(@AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(reviewExplorerService.getAllReviewsByExplorer(myUser.getId()));
    }

    @PostMapping("/create-review-explorer/explorer-{explorerId}/experience-{experienceId}")
    public ResponseEntity createReview(@AuthenticationPrincipal MyUser myUser, @PathVariable Integer explorerId,@PathVariable Integer experienceId, @RequestBody @Valid ReviewExplorer reviewExplorer) {
        reviewExplorerService.createReview(myUser.getId(), explorerId,experienceId, reviewExplorer);
        return ResponseEntity.status(200).body("Review added successfully");
    }

    @PutMapping("/update-review-explorer/review-{reviewId}")
    public ResponseEntity updateReview(@PathVariable Integer reviewId, @AuthenticationPrincipal MyUser myUser, @RequestBody @Valid ReviewExplorer reviewExplorer) {
        reviewExplorerService.updateReview(myUser.getId(), reviewId, reviewExplorer);
        return ResponseEntity.status(200).body("Review updated successfully");
    }

    @DeleteMapping("/delete-review-explorer/review-{reviewId}")
    public ResponseEntity deleteReview(@PathVariable Integer reviewId, @AuthenticationPrincipal MyUser myUser) {
        reviewExplorerService.deleteReview(myUser.getId(), reviewId);
        return ResponseEntity.status(200).body("Review deleted successfully");
    }

    //2
    //Arwa
    @GetMapping("/get-all-review-by-organizer")
    public ResponseEntity getReviewsByOrganizer(@AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(reviewExplorerService.getReviewsByOrganizer(myUser.getId()));
    }

    //4
    //Arwa
    @GetMapping("/get-explorer-reviews-asc")
    public ResponseEntity getOrganizerReviewsFilteredByHighToLow(@AuthenticationPrincipal MyUser myUser) { // NEEDS FIXING
        return ResponseEntity.status(200).body(reviewExplorerService.getExplorerReviewsFilteredByHighToLow(myUser.getId()));
    }

    //5
    //Arwa
    @GetMapping("/get-explorer-reviews-Desc")
    public ResponseEntity getOrganizerReviewsFilteredByLowToHigh(@AuthenticationPrincipal MyUser myUser) { // NEEDS FIXING
        return ResponseEntity.status(200).body(reviewExplorerService.getExplorerReviewsFilteredByLowToHigh(myUser.getId()));
    }

    //6
    //Arwa
   @GetMapping("/get-all-reviews-by-explorer")
   public ResponseEntity getReviewsByExplorer(@AuthenticationPrincipal MyUser myUser) {
       List<ReviewExplorerOutDTO> reviews = reviewExplorerService.getAllReviewsByExplorer(myUser.getId());
        return ResponseEntity.status(200).body(reviews);
    }



}




package com.example.finalprojectbond.Controller;

import com.example.finalprojectbond.Model.MyUser;
import com.example.finalprojectbond.Model.ReviewExperience;
import com.example.finalprojectbond.Service.ReviewExperienceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/review-experience")
@RequiredArgsConstructor
public class ReviewExperienceController {

    private final ReviewExperienceService reviewExperienceService;

    @GetMapping("/get-all-review-experience/experience-{experienceId}")
    public ResponseEntity getAllReviewsByExperience(@PathVariable Integer experienceId) {
        return ResponseEntity.status(200).body(reviewExperienceService.getReviewsByExperience(experienceId));
    }

    @PostMapping("/create-review-experience/experience-{experienceId}")
    public ResponseEntity createReview(@AuthenticationPrincipal MyUser myUser, @PathVariable Integer experienceId, @RequestBody @Valid ReviewExperience reviewExperience) {
        reviewExperienceService.createReview(myUser.getId(), experienceId, reviewExperience);
        return ResponseEntity.status(200).body("Review added successfully");
    }

    @PutMapping("/update-review-experience/review-{reviewId}")
    public ResponseEntity updateReview(@PathVariable Integer reviewId,@AuthenticationPrincipal MyUser myUser, @RequestBody @Valid ReviewExperience reviewExperience) {
        reviewExperienceService.updateReview(myUser.getId(), reviewId, reviewExperience);
        return ResponseEntity.status(200).body("Review updated successfully");
    }

    @DeleteMapping("/delete-review-experience/review-{reviewId}")
    public ResponseEntity deleteReview(@PathVariable Integer reviewId, @AuthenticationPrincipal MyUser myUser) {
        reviewExperienceService.deleteReview(myUser.getId(), reviewId);
        return ResponseEntity.status(200).body("Review deleted successfully");
    }
    //Arwa
    @GetMapping("/all-explorer-reviews-for-Experiences/explorer-{explorerId}")
    public ResponseEntity getExplorerReviewsFilteredByExperience(@PathVariable Integer explorerId) {
        return ResponseEntity.status(200).body(reviewExperienceService.getExplorerReviewsFilteredByExperience(explorerId));
    }
    //Arwa
    @GetMapping("/get-experience-reviews-filtered-by-date/experience-{experienceId}")
    public ResponseEntity getExperienceReviewsFilteredByDate(@PathVariable Integer experienceId) {
        return ResponseEntity.status(200).body(reviewExperienceService.getExperienceReviewsFilteredByDate(experienceId));
    }
    //Arwa
    @GetMapping("/get-explorer-reviews-filtered-by-low-to-high/explorer-{explorerId}")
    public ResponseEntity getExplorerReviewsFilteredByLowToHigh(@PathVariable Integer explorerId) {
        return ResponseEntity.status(200).body(reviewExperienceService.getExplorerReviewsFilteredByLowToHigh(explorerId));
    }
    //Arwa
    @GetMapping("/get-explorer-reviews-filtered-by-high-to-low/explorer-{explorerId}")
    public ResponseEntity getExplorerReviewsFilteredByHighToLow(@PathVariable Integer explorerId) {
        return ResponseEntity.status(200).body(reviewExperienceService.getExplorerReviewsFilteredByHighToLow(explorerId));
    }


}


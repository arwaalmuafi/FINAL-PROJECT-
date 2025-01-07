package com.example.finalprojectbond.Service;

import com.example.finalprojectbond.Api.ApiException;
import com.example.finalprojectbond.Model.Experience;
import com.example.finalprojectbond.Model.Explorer;
import com.example.finalprojectbond.Model.Organizer;
import com.example.finalprojectbond.Model.ReviewExperience;
import com.example.finalprojectbond.OutDTO.ReviewExperienceOutDTO;
import com.example.finalprojectbond.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewExperienceService {
    private final ReviewExperienceRepository reviewExperienceRepository;
    private final ExperienceRepository experienceRepository;
    private final OrganizerRepository organizerRepository;
    private final ExplorerRepository explorerRepository;

    public List<ReviewExperience> getAllReviews() {
        return reviewExperienceRepository.findAll();
    }

    public List<ReviewExperienceOutDTO> getReviewsByExperience(Integer experienceId) {
        Experience experience = experienceRepository.findExperienceById(experienceId);
        if (experience == null) {
            throw new ApiException("Experience not found");
        }

        List<ReviewExperience> reviews = reviewExperienceRepository.findAllByExperience(experience);
        if (reviews.isEmpty()) {
            throw new ApiException("No reviews found for this experience");
        }

        return convertReviewExperienceToOutDTO(reviews);
    }

    public List<ReviewExperienceOutDTO> convertReviewExperienceToOutDTO(List<ReviewExperience> experiences){
        List<ReviewExperienceOutDTO> reviewExperienceOutDTOS = new ArrayList<>();
        for (ReviewExperience reviewExperience:experiences){
            reviewExperienceOutDTOS.add(new ReviewExperienceOutDTO(reviewExperience.getComment(),reviewExperience.getRating(),reviewExperience.getCreatedAt()));
        }
        return reviewExperienceOutDTOS;
    }

    public void createReview(Integer explorerId, Integer experienceId, ReviewExperience reviewExperience) {
        Explorer explorer = explorerRepository.findExplorerById(explorerId);
        if (explorer == null) {
            throw new ApiException("Only explorers can create reviews");
        }

        Experience experience = experienceRepository.findExperienceById(experienceId);
        if (experience == null) {
            throw new ApiException("Experience not found");
        }

        if (!experience.getExplorers().contains(explorer)) {
            throw new ApiException("You can only review experiences you participated in");
        }

        if (reviewExperience.getComment() == null) {
            throw new ApiException("Review comment cannot be empty");
        }
        if (!experience.getStatus().equalsIgnoreCase("Completed")){
            throw new ApiException("can not review because Experience is not completed");
        }
        if(!(reviewExperienceRepository.findReviewExperienceByExplorerAndExperience(explorer, experience) == null)) {
            throw new ApiException("You can only review experiences one time");
        }

        reviewExperience.setExplorer(explorer);
        reviewExperience.setExperience(experience);
        reviewExperienceRepository.save(reviewExperience);
        List<Experience> experiences = experienceRepository.findAllByOrganizer(experience.getOrganizer());
        Integer countReviews = 0;
        Double ratingTotal = 0.0;
        //ratingTotal += reviewExperience.getRating();
        for (Experience experience1 : experiences) {
            List<ReviewExperience> reviews = reviewExperienceRepository.findAllByExperience(experience1);
            countReviews += reviews.size();
            for (ReviewExperience review : reviews) {
                ratingTotal += review.getRating();
            }
        }
        Double newRating = ratingTotal / countReviews;
        experience.getOrganizer().getMyUser().setRating(newRating);
        experienceRepository.save(experience);
    }

    public void updateReview(Integer explorerId, Integer reviewId, ReviewExperience reviewExperience) {
        // Validate Explorer
        Explorer explorer = explorerRepository.findExplorerById(explorerId);
        if (explorer == null) {
            throw new ApiException("You don't have permission to update reviews");
        }

        // Fetch and validate the Review
        ReviewExperience existingReview = reviewExperienceRepository.findReviewExperienceById(reviewId);
        if (existingReview == null || !existingReview.getExplorer().equals(explorer)) {
            throw new ApiException("Review not found or you are not authorized to update this review");
        }

        // Update the review fields
        existingReview.setComment(reviewExperience.getComment());
        existingReview.setRating(reviewExperience.getRating());

        // Save the updated review
        reviewExperienceRepository.save(existingReview);
    }

    public void deleteReview(Integer explorerId, Integer reviewId) {
        // Validate Explorer
        Explorer explorer = explorerRepository.findExplorerById(explorerId);
        if (explorer == null) {
            throw new ApiException("You don't have permission to delete reviews");
        }

        // Fetch and validate the Review
        ReviewExperience review = reviewExperienceRepository.findReviewExperienceById(reviewId);
        if (review == null || !review.getExplorer().equals(explorer)) {
            throw new ApiException("Review not found or you are not authorized to delete this review");
        }

        // Delete the review
        reviewExperienceRepository.delete(review);
    }

    public List<ReviewExperienceOutDTO> getExplorerReviewsFilteredByExperience(Integer explorerId) {
        // Validate Explorer
        Explorer explorer = explorerRepository.findExplorerById(explorerId);
        if (explorer == null) {
            throw new ApiException("Explorer not found");
        }

        // Fetch reviews for the explorer's experiences
        List<ReviewExperience> reviews = reviewExperienceRepository.findAllByExplorer(explorer);
        if (reviews.isEmpty()) {
            throw new ApiException("No reviews found for the explorer's experiences");
        }
        return convertReviewExperienceToOutDTO(reviews);
    }

    public List<ReviewExperienceOutDTO> getExperienceReviewsFilteredByDate(Integer experienceId) {
        // Validate Experience
        Experience experience = experienceRepository.findExperienceById(experienceId);
        if (experience == null) {
            throw new ApiException("Experience not found");
        }

        // Fetch reviews sorted by date
        List<ReviewExperience> reviews = reviewExperienceRepository.findByExperienceOrderByCreatedAtDesc(experience);
        if (reviews.isEmpty()) {
            throw new ApiException("No reviews found for this experience");
        }
        return convertReviewExperienceToOutDTO(reviews);
    }

    public List<ReviewExperienceOutDTO> getExplorerReviewsFilteredByLowToHigh(Integer explorerId) {
        // Validate Explorer
        Explorer explorer = explorerRepository.findExplorerById(explorerId);
        if (explorer == null) {
            throw new ApiException("Explorer not found");
        }

        // Fetch reviews sorted by rating (low to high)
        List<ReviewExperience> reviews = reviewExperienceRepository.findByExplorerOrderByRatingAsc(explorer);
        if (reviews.isEmpty()) {
            throw new ApiException("No reviews found for this explorer");
        }
        return convertReviewExperienceToOutDTO(reviews);
    }

    public List<ReviewExperienceOutDTO> getExplorerReviewsFilteredByHighToLow(Integer explorerId) {
        // Validate Explorer
        Explorer explorer = explorerRepository.findExplorerById(explorerId);
        if (explorer == null) {
            throw new ApiException("Explorer not found");
        }

        // Fetch reviews sorted by rating (high to low)
        List<ReviewExperience> reviews = reviewExperienceRepository.findByExplorerOrderByRatingDesc(explorer);
        if (reviews.isEmpty()) {
            throw new ApiException("No reviews found for this explorer");
        }
        return convertReviewExperienceToOutDTO(reviews);
    }
}



package com.example.finalprojectbond.Service;

import com.example.finalprojectbond.Api.ApiException;
import com.example.finalprojectbond.Model.*;
import com.example.finalprojectbond.OutDTO.ReviewExplorerOutDTO;
import com.example.finalprojectbond.Repository.ExperienceRepository;
import com.example.finalprojectbond.Repository.ExplorerRepository;
import com.example.finalprojectbond.Repository.OrganizerRepository;
import com.example.finalprojectbond.Repository.ReviewExplorerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewExplorerService {

        private final ReviewExplorerRepository reviewExplorerRepository;
        private final ExplorerRepository explorerRepository;
        private final OrganizerRepository organizerRepository;
        private final ExperienceRepository experienceRepository;

        public List<ReviewExplorerOutDTO> getAllReviews() {

            return convertReviewExplorerToOutDTO(reviewExplorerRepository.findAll());
        }

        public void createReview(Integer organizerId, Integer explorerId,Integer experienceId, ReviewExplorer reviewExplorer) {
            Organizer organizer = organizerRepository.findOrganizerById(organizerId);
            if (organizer == null) {
                throw new ApiException("You don't have permission to create reviews");
            }

            Explorer explorer = explorerRepository.findExplorerById(explorerId);
            if (explorer == null) {
                throw new ApiException("Explorer was not found");
            }
            Experience experience = experienceRepository.findExperienceById(experienceId);
            if (experience == null) {
                throw new ApiException("Experience was not found");
            }
            if (!experience.getStatus().equalsIgnoreCase("Completed")){
                throw new ApiException("can not review because Experience is not completed");
            }
            if (!experience.getExplorers().contains(explorer)) {
                throw new ApiException("explorer was not found in this experience");
            }
            if (!(reviewExplorerRepository.findReviewExplorerByOrganizerAndExplorer(organizer, explorer) == null)){
                throw new ApiException("You can only review explorer one time");
            }


            reviewExplorer.setOrganizer(organizer);
            reviewExplorer.setExplorer(explorer);
            reviewExplorerRepository.save(reviewExplorer);
            List<ReviewExplorer> reviewExplorers = reviewExplorerRepository.findAllByExplorer(explorer);
            Double ratingTotal = 0.0;
            //ratingTotal += reviewExplorer.getRating();
            for (ReviewExplorer reviewExplorer1 : reviewExplorers) {
                ratingTotal += reviewExplorer1.getRating();
            }
            Double newRating = ratingTotal / reviewExplorers.size();
            explorer.getMyUser().setRating(newRating);
            explorerRepository.save(explorer);
        }

        public void updateReview(Integer organizerId, Integer reviewId, ReviewExplorer reviewExplorer) {
            Organizer organizer = organizerRepository.findOrganizerById(organizerId);
            if (organizer == null) {
                throw new ApiException("You don't have permission to update reviews");
            }

            ReviewExplorer existingReview = reviewExplorerRepository.findReviewExplorerById(reviewId);
            if(existingReview==null){
                new ApiException("Review was not found");
            }
            existingReview.setComment(reviewExplorer.getComment());
            existingReview.setRating(reviewExplorer.getRating());

            reviewExplorerRepository.save(existingReview);
        }

        public void deleteReview(Integer explorerId, Integer reviewId) {
            Explorer explorer = explorerRepository.findExplorerById(explorerId);
            if (explorer == null) {
                throw new ApiException("You don't have permission to delete reviews");
            }
            ReviewExplorer review = reviewExplorerRepository.findReviewExplorerById(reviewId);
            if(review==null){
                new ApiException("Review was not found");
            }
            if (!review.getExplorer().equals(explorer)) {
                throw new ApiException("Explorer not write this review");
            }
            reviewExplorerRepository.delete(review);
        }


        //9
        public List<ReviewExplorerOutDTO> getReviewsByOrganizer(Integer organizerId) {
            Organizer organizer = organizerRepository.findOrganizerById(organizerId);
            if (organizer == null) {
                throw new ApiException("Organizer was not found");
            }

            List<ReviewExplorer> reviews = reviewExplorerRepository.findByOrganizer(organizer);
            if (reviews.isEmpty()) {
                throw new ApiException("No reviews found for this organizer");
            }

            return convertReviewExplorerToOutDTO(reviews);
        }

        public List<ReviewExplorerOutDTO> convertReviewExplorerToOutDTO(List<ReviewExplorer> reviewExplorers){
            List<ReviewExplorerOutDTO> reviewExplorerOutDTOS = new ArrayList<>();
            for (ReviewExplorer reviewExplorer: reviewExplorers){
                reviewExplorerOutDTOS.add(new ReviewExplorerOutDTO(reviewExplorer.getComment(),reviewExplorer.getRating(),reviewExplorer.getCreatedAt()));
            }
            return reviewExplorerOutDTOS;
        }

        // 10 Endpoint 34: Get all reviews for an explorer
        public List<ReviewExplorerOutDTO> getAllReviewsByExplorer(Integer explorerId) {
            Explorer explorer = explorerRepository.findExplorerById(explorerId);
            if (explorer == null) {
                throw new ApiException("Explorer not found");
            }
            return convertReviewExplorerToOutDTO(reviewExplorerRepository.findByExplorer(explorer));
        }

        public List<ReviewExplorerOutDTO> getExplorerReviewsFilteredByLowToHigh(Integer explorerId) {
            Explorer explorer = explorerRepository.findExplorerById(explorerId);
            if (explorer == null) {
                throw new ApiException("Explorer was not found");
            }
            List<ReviewExplorer> reviews = reviewExplorerRepository.findByExplorerOrderByRatingAsc(explorer);
            if (reviews.isEmpty()) {
                throw new ApiException("No reviews found for this explorer");
            }
            return convertReviewExplorerToOutDTO(reviews);
        }

        public List<ReviewExplorerOutDTO> getExplorerReviewsFilteredByHighToLow(Integer explorerId) {
            Explorer explorer = explorerRepository.findExplorerById(explorerId);
            if (explorer == null) {
                throw new ApiException("Explorer was not found");
            }
            List<ReviewExplorer> reviews = reviewExplorerRepository.findByExplorerOrderByRatingDesc(explorer);
            if (reviews.isEmpty()) {
                throw new ApiException("No reviews found for this explorer");
            }
            return convertReviewExplorerToOutDTO(reviews);
        }


}

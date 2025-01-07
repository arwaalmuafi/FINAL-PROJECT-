package com.example.finalprojectbond.Repository;

import com.example.finalprojectbond.Model.Experience;
import com.example.finalprojectbond.Model.Explorer;
import com.example.finalprojectbond.Model.Organizer;
import com.example.finalprojectbond.Model.ReviewExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewExperienceRepository extends JpaRepository<ReviewExperience, Integer> {

    ReviewExperience findReviewExperienceById(Integer Id);


    List<ReviewExperience> findAllByExperience(Experience experience);

    List<ReviewExperience> findByExperienceOrderByCreatedAtDesc(Experience experience);

    List<ReviewExperience> findByExplorerOrderByRatingDesc(Explorer explorer);

    List<ReviewExperience> findByExplorerOrderByRatingAsc(Explorer explorer);

    List<ReviewExperience> findAllByExplorer(Explorer explorer);
 ReviewExperience findReviewExperienceByExplorerAndExperience(Explorer explorer, Experience experience);
}

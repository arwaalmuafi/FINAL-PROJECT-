package com.example.finalprojectbond.Service;

import com.example.finalprojectbond.Api.ApiException;
import com.example.finalprojectbond.InDTO.TagInDTO;
import com.example.finalprojectbond.Model.Experience;
import com.example.finalprojectbond.Model.Organizer;
import com.example.finalprojectbond.Model.Tag;
import com.example.finalprojectbond.Repository.ExperienceRepository;
import com.example.finalprojectbond.Repository.OrganizerRepository;
import com.example.finalprojectbond.Repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final ExperienceRepository experienceRepository;
    private final OrganizerRepository organizerRepository;


    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Tag getTagById(Integer tagId) {
        Tag tag = tagRepository.findTagById(tagId);
        if (tag == null) {
            throw new ApiException("Tag with ID: " + tagId + " was not found");
        }
          return tag;
    }

    public void createTag(Tag tag) {
//       List<Tag> existingTag = tagRepository.findTagByName(tag.getName());
//        if (existingTag.isEmpty()) {
//            throw new ApiException("Tag with the same name already exists");
//        }
        tagRepository.save(tag);
    }


    public void updateTag(Integer tagId, Tag tag) {
        Tag existingTag = tagRepository.findTagById(tagId);
        if(existingTag==null){
            throw  new ApiException("Tag with ID: " + tagId + " was not found");
        }
        existingTag.setName(tag.getName());
        tagRepository.save(existingTag);
    }

    public void deleteTag(Integer tagId) {
        Tag tag = tagRepository.findTagById(tagId);
        if(tag==null){
            throw  new ApiException("Tag with ID: " + tagId + " was not found");
        }
        tagRepository.delete(tag);
    }

    //5
//    public void assignTagsToExperience(TagInDTO tagInDTO) {
//        Experience experience = experienceRepository.findExperienceById(tagInDTO.getExperienceId());
//        if(experience==null){
//            throw new ApiException("Experience not found");
//        }
//        List<Tag> tags = tagRepository.findAllById(tagInDTO.getTagIds());
//        if (tags.isEmpty()) {
//            throw new ApiException("No valid tags found for the provided IDs");
//        }
//        experience.getTags().addAll(tags);
//        experienceRepository.save(experience);
//    }

    public void assignTagsToExperience(Integer organizerId,TagInDTO tagInDTO) {
        Experience experience = experienceRepository.findExperienceById(tagInDTO.getExperienceId());
        Organizer organizer = organizerRepository.findOrganizerById(organizerId);
        if(experience==null){
            throw new ApiException("Experience not found");
        }
        if (organizer==null){
            throw new ApiException("Organizer not found");
        }
        if (!experience.getOrganizer().equals(organizer)){
            throw new ApiException("Organizer mismatch");
        }
        List<Tag> tags = tagRepository.findAllById(tagInDTO.getTagIds());

        if (tags.size() != tagInDTO.getTagIds().size()) {
            throw new ApiException("One or more provided tag IDs do not exist");
        }
        for (Tag tag : tags) {
            experience.getTags().add(tag);
            tag.getExperiences().add(experience);
            tagRepository.save(tag);
            experienceRepository.save(experience);
        }
    }
}


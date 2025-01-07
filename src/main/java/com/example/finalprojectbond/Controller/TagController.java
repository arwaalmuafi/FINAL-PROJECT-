package com.example.finalprojectbond.Controller;

import com.example.finalprojectbond.Api.ApiResponse;
import com.example.finalprojectbond.InDTO.TagInDTO;
import com.example.finalprojectbond.Model.MyUser;
import com.example.finalprojectbond.Model.Tag;
import com.example.finalprojectbond.Service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;


    @GetMapping("/get-all-tags")
    public ResponseEntity<List<Tag>> getAllTags() {
        return ResponseEntity.status(200).body(tagService.getAllTags());
    }
    //Arwa
    @GetMapping("/get-tag-by-id/tag-{tagId}")
    public ResponseEntity<Tag> getTagById(@PathVariable Integer tagId) {
        return ResponseEntity.status(200).body(tagService.getTagById(tagId));
    }

    @PostMapping("/create-tag")
    public ResponseEntity<ApiResponse> createTag(@RequestBody @Valid Tag tag) {
        tagService.createTag(tag);
        return ResponseEntity.status(200).body(new ApiResponse("Tag created successfully"));
    }

    @PutMapping("/update-tag/tag-{tagId}")
    public ResponseEntity<ApiResponse> updateTag(@PathVariable Integer tagId, @RequestBody @Valid Tag tag) {
        tagService.updateTag(tagId, tag);
        return ResponseEntity.status(200).body(new ApiResponse("Tag updated successfully"));
    }

    @DeleteMapping("/delete-tag/tag-{tagId}")
    public ResponseEntity<ApiResponse> deleteTag(@PathVariable Integer tagId) {
        tagService.deleteTag(tagId);
        return ResponseEntity.status(200).body(new ApiResponse("Tag deleted successfully"));
    }
    //1
    //Arwa
    @PostMapping("/assign-tags-to-experience")
    public ResponseEntity<ApiResponse> assignTagsToExperience(@AuthenticationPrincipal MyUser myUser, @RequestBody @Valid TagInDTO tagInDTO) {
        tagService.assignTagsToExperience(myUser.getId(),tagInDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Tags assigned to experience successfully"));
    }
}
package com.example.finalprojectbond.Controller;

import com.example.finalprojectbond.Api.ApiResponse;
import com.example.finalprojectbond.InDTO.TaskInDTO;
import com.example.finalprojectbond.Model.MyUser;
import com.example.finalprojectbond.Model.Task;
import com.example.finalprojectbond.OutDTO.ExplorerOutDTO;
import com.example.finalprojectbond.OutDTO.TaskOutDTO;
import com.example.finalprojectbond.Service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskController {


    private final TaskService taskService;

    //12
    @GetMapping("/get-all")
    public ResponseEntity getAllTasks() {
        return ResponseEntity.status(200).body(taskService.getAllTasks());
    }

    //13
    //Arwa
    @GetMapping("/get-task-by-explorer")
    public ResponseEntity getTasksByExplorer(@AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(taskService.getTasksByExplorer(myUser.getId()));
    }

    //7
    //Arwa
    @PostMapping("/create-task/explorer-{explorerId}/experience-{experienceId}")
    public ResponseEntity createTask(@AuthenticationPrincipal MyUser myUser,
                                     @PathVariable Integer explorerId,
                                     @PathVariable Integer experienceId,
                                     @RequestBody @Valid TaskInDTO taskInDTO) {
        taskService.createTask(myUser.getId(), explorerId,experienceId, taskInDTO);
        return ResponseEntity.status(200).body("Task created successfully");
    }

    @PutMapping("/update-task/explorer-{explorerId}/task-{taskId}")
    public ResponseEntity updateTask(@AuthenticationPrincipal MyUser myUser,
                                     @PathVariable Integer explorerId,
                                     @PathVariable Integer taskId,
                                     @RequestBody @Valid TaskInDTO taskDTO) {
        taskService.updateTask(myUser.getId(), explorerId, taskId, taskDTO);
        return ResponseEntity.status(200).body("Task updated successfully");
    }

    @DeleteMapping("/delete-task/explorer-{explorerId}/task-{taskId}")
    public ResponseEntity deleteTask(@AuthenticationPrincipal MyUser myUser,
                                     @PathVariable Integer explorerId,
                                     @PathVariable Integer taskId) {
        taskService.deleteTask(myUser.getId(), explorerId, taskId);
        return ResponseEntity.status(200).body("Task deleted successfully");
    }

    //8
//    @PutMapping("/change-status/organizer-{organizerId}/task-{taskId}/status-{status}")
//    public ResponseEntity changeTaskStatus(@PathVariable Integer organizerId,
//                                           @PathVariable Integer taskId,
//                                           @PathVariable String status) {
//
//        taskService.changeTaskStatus(organizerId, taskId,status);
//        return ResponseEntity.status(200).body("Task status updated successfully");
//    }

    //9
    //Arwa
    @GetMapping("/view-task-progress/experience-{experienceId}")
    public ResponseEntity viewTaskProgressForAllExplorers(@AuthenticationPrincipal MyUser myUser,@PathVariable Integer experienceId) {
        return ResponseEntity.status(200).body(taskService.viewTaskProgressForAllExplorers(myUser.getId(), experienceId));
    }

    //10
    //Arwa
    @GetMapping("/get-in-complete-tasks")
    public ResponseEntity getIncompleteTasksForExplorer(@AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(taskService.getIncompleteTasksForExplorer(myUser.getId()));
    }

    //11
    //Arwa
    @PutMapping("/mark-completed/task-{taskId}")
    public ResponseEntity<String> changeTaskStatusToCompleted(@AuthenticationPrincipal MyUser myUser,@PathVariable Integer taskId) {
        taskService.changeTaskStatusToCompleted(myUser.getId(),taskId);
        return ResponseEntity.status(200).body("Task marked as completed");
    }
    //Arwa
    @GetMapping("/get-task-by-experience/experience-{experienceId}")
    public ResponseEntity<List<TaskOutDTO>> getTasksByExperience (@AuthenticationPrincipal MyUser myUser,@PathVariable Integer experienceId) {
        return ResponseEntity.status(200).body(taskService.getTasksByExperience(myUser.getId(),experienceId));
    }
}




package com.example.finalprojectbond.Service;

import com.example.finalprojectbond.Api.ApiException;
import com.example.finalprojectbond.InDTO.TaskInDTO;
import com.example.finalprojectbond.Model.*;
import com.example.finalprojectbond.OutDTO.BriefExplorerOutDTO;
import com.example.finalprojectbond.OutDTO.ExplorerOutDTO;
import com.example.finalprojectbond.OutDTO.TaskOutDTO;
import com.example.finalprojectbond.OutDTO.ViewTaskOutDTO;
import com.example.finalprojectbond.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final AuthRepository authRepository;
    private final OrganizerRepository organizerRepository;
    private final ExplorerRepository explorerRepository;
    private final ExperienceRepository experienceRepository;


    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<TaskOutDTO> getTasksByExplorer(Integer myUserId) {
        Explorer explorer = explorerRepository.findExplorerById(myUserId);
        if (explorer == null) {
            throw new ApiException("Explorer was not found");
        }

        List<Task> tasks = taskRepository.findTasksByExplorer(explorer);
        if (tasks.isEmpty()) {
            throw new ApiException("This explorer has no tasks yet");
        }

        List<TaskOutDTO> taskDTOS = new ArrayList<>();
        for (Task task : tasks) {
            taskDTOS.add(new TaskOutDTO(task.getTitle(), task.getDescription(), task.getStatus()));
        }

        return taskDTOS;
    }

    //1
    public void createTask(Integer myUserId, Integer explorerId, Integer experienceId, TaskInDTO taskInDTO) {
        Organizer organizer = organizerRepository.findOrganizerById(myUserId);
        if (organizer == null) {
            throw new ApiException("Organizer not found");
        }
        Explorer explorer = explorerRepository.findExplorerById(explorerId);
        if (explorer == null) {
            throw new ApiException("Explorer was not found");
        }
        Experience experience = experienceRepository.findExperienceById(experienceId);
        if (experience == null) {
            throw new ApiException("Experience was not found");
        }
        if (!experience.getOrganizer().equals(organizer)) {
            throw new ApiException("Organizer does not own experience");
        }

        if (!explorer.getExperiences().contains(experience)) {
            throw new ApiException("Explorer does not register in this experience");
        }

        if (!experience.getStatus().equalsIgnoreCase("Task Assignment")) {
            throw new ApiException("the status of experience should be Task Assignment");
        }

        Task taskToCreate = new Task();
        taskToCreate.setTitle(taskInDTO.getTitle());
        taskToCreate.setDescription(taskInDTO.getDescription());
        taskToCreate.setExplorer(explorer);
        taskToCreate.setExperience(experience);
        taskRepository.save(taskToCreate);

        explorer.getTasks().add(taskToCreate);
        explorerRepository.save(explorer);
    }


    public void updateTask(Integer myUserId, Integer explorerId, Integer taskId, TaskInDTO taskInDTO) {

        Organizer organizer = organizerRepository.findOrganizerById(myUserId);
        if (organizer == null) {
            throw new ApiException("Organizer not found");
        }

        Explorer explorer = explorerRepository.findExplorerById(explorerId);
        if (explorer == null) {
            throw new ApiException("Explorer was not found");
        }
        Task task = taskRepository.findTaskByIdAndExplorer(taskId, explorer);
        if (task == null) {
            throw new ApiException("Task was not found");
        }

        if (task.getStatus().equalsIgnoreCase("Complete")) {
            throw new ApiException("Task is already completed");
        }

        task.setTitle(taskInDTO.getTitle());
        task.setDescription(taskInDTO.getDescription());
        taskRepository.save(task);
    }


    public void deleteTask(Integer myUserId, Integer explorerId, Integer taskId) {
        Organizer organizer = organizerRepository.findOrganizerById(myUserId);
        if (organizer == null) {
            throw new ApiException("Organizer not found");
        }

        Explorer explorer = explorerRepository.findExplorerById(explorerId);
        if (explorer == null) {
            throw new ApiException("Explorer was not found");
        }

        Task task = taskRepository.findTaskByIdAndExplorer(taskId, explorer);
        if (task == null) {
            throw new ApiException("Task was not found");
        }
        if (task.getStatus().equalsIgnoreCase("Complete")) {
            throw new ApiException("Task is already completed");
        }

        taskRepository.delete(task);
    }


    //3  -----------------------  NEEDS REVIEW ---------------------------------------------
    public List<ViewTaskOutDTO> viewTaskProgressForAllExplorers(Integer myUserId, Integer experienceId) {
        Organizer organizer = organizerRepository.findOrganizerById(myUserId);
        if (organizer == null) {
            throw new ApiException("You don't have permission to view task progress");
        }

        Experience experience = experienceRepository.findExperienceById(experienceId);
        if (experience == null) {
            throw new ApiException("Experience was not found");
        }

        if (!experience.getOrganizer().equals(organizer)) {
            throw new ApiException("Experience does not own experience");
        }

        List<Task> tasks = taskRepository.findAllByExperience(experience);
        if (tasks.isEmpty()) {
            throw new ApiException("No tasks found");
        }

        List<ViewTaskOutDTO> viewTaskOutDTOS = new ArrayList<>();
        for (Task task : tasks) {
            viewTaskOutDTOS.add(new ViewTaskOutDTO(task.getExplorer().getMyUser().getName(), task.getTitle(), task.getDescription(), task.getStatus(), task.getExplorer().getMyUser().getPhotoURL()));
        }

        return viewTaskOutDTOS;
    }

    // Endpoint 28: Get all "Incomplete" tasks for an explorer
    public List<TaskOutDTO> getIncompleteTasksForExplorer(Integer myUserId) {
        Explorer explorer = explorerRepository.findExplorerById(myUserId);
        if (explorer == null) {
            throw new ApiException("Explorer not found");
        }
        List<Task> tasks = taskRepository.findByExplorerAndStatus(explorer, "In-Complete");
        List<TaskOutDTO> taskOutDTOS = new ArrayList<>();
        for (Task task : tasks) {
            taskOutDTOS.add(new TaskOutDTO(task.getTitle(), task.getDescription(), task.getStatus()));
        }
        return taskOutDTOS;
    }

    // Endpoint 30: Change the status of a task to "Completed"
    public void changeTaskStatusToCompleted(Integer explorerId,Integer taskId) {
        Explorer explorer = explorerRepository.findExplorerById(explorerId);
        if (explorer == null) {
            throw new ApiException("Explorer was not found");
        }
        Task task = taskRepository.findTaskById(taskId);
        if (task == null) {
            throw new ApiException("Task not found");
        }
        if (!task.getExplorer().equals(explorer)) {
            throw new ApiException("Explorer does not own task");
        }
        if (task.getStatus().equalsIgnoreCase("Complete")) {
            throw new ApiException("Task is already completed");
        }
        if ("Complete".equalsIgnoreCase(task.getStatus())) {
            throw new ApiException("Task is already completed");
        }

        Experience experience = task.getExperience();
        if (!experience.getStatus().equalsIgnoreCase("In Progress")){
            throw new ApiException("the status of Experience should be In Progress");
        }

        task.setStatus("Complete");
        taskRepository.save(task);
		
        List<Task> tasks = taskRepository.findAllByExperienceAndStatus(experience,"Complete");
        List<Task> tasks1 = taskRepository.findAllByExperience(experience);
        if (tasks.size() == tasks1.size()) {
            experience.setStatus("Active");
            experienceRepository.save(experience);
        }
    }

    public List<TaskOutDTO> getTasksByExperience(Integer myUserId, Integer experienceId) {
        Organizer organizer = organizerRepository.findOrganizerById(myUserId);
        if (organizer == null) {
            throw new ApiException("organizer was not found");
        }
        Experience experience = experienceRepository.findExperienceById(experienceId);
        if (experience == null) {
            throw new ApiException("Experience was not found");
        }
        if (!experience.getOrganizer().equals(organizer)) {
            throw new ApiException("organizer does not own experience");
        }
        List<Task> tasks = taskRepository.findByExperienceId(experienceId);
        List<TaskOutDTO> taskOutDTOS = new ArrayList<>();
        for (Task task : tasks) {
            taskOutDTOS.add(new TaskOutDTO(task.getTitle(), task.getDescription(), task.getStatus()));
        }
        return taskOutDTOS;
    }


}

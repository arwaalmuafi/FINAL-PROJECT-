# FINAL-PROJECT-

Task MODEL+REPOSETORY

Controller: TaskController
GET /tasks/explorer/{explorerId}
Get all tasks assigned to a specific explorer.
Service: getTasksByExplorer(explorerId)
PUT /tasks/{taskId}/status
Change the status of a task (e.g., from pending to completed).
Service: changeTaskStatus(taskId, status)
GET /tasks/progress
View task progress for all explorers.
Service: viewTaskProgressForAllExplorers()
GET /tasks/incomplete/explorer/{explorerId}
Get all incomplete tasks for a specific explorer.
Service: getIncompleteTasksForExplorer(explorerId)
PUT /tasks/{taskId}/complete
Mark a specific task as completed.
Service: changeTaskStatusToCompleted(taskId)
GET /tasks/experience/{experienceId}
Get all tasks associated with a specific experience.
Service: getTasksByExperience(experienceId)

Explorer Review MODEL+REPOSETORY

Controller: ReviewExplorerController
GET /reviews/explorer/{explorerId}
Get all reviews written by a specific explorer.
Service: getAllReviewsByExplorer(explorerId)
GET /reviews/organizer/{organizerId}
Get all reviews for a specific organizer.
Service: getReviewsByOrganizer(organizerId)
GET /reviews/explorer/low-to-high
Get explorer reviews sorted by rating (low to high).
Service: getExplorerReviewsFilteredByLowToHigh()
GET /reviews/explorer/high-to-low
Get explorer reviews sorted by rating (high to low).
Service: getExplorerReviewsFilteredByHighToLow()

Experience Review MODEL+REPOSETORY

Controller: ReviewExperienceController
GET /reviews/experience/{experienceId}/explorer
Get reviews for an experience, filtered by explorer.
Service: getExplorerReviewsFilteredByExperience(experienceId)
GET /reviews/experience/{experienceId}/date
Get reviews for an experience, sorted by date.
Service: getExperienceReviewsFilteredByDate(experienceId)
GET /reviews/experience/low-to-high
Get experience reviews sorted by rating (low to high).
Service: getExplorerReviewsFilteredByLowToHigh()
GET /reviews/experience/high-to-low
Get experience reviews sorted by rating (high to low).
Service: getExplorerReviewsFilteredByHighToLow()

Tag MODEL + REPOSETORY

Controller: TagController
POST /tags/experience/{experienceId}
Assign one or more tags to a specific experience.
Service: assignTagsToExperience(experienceId, tagList)
Structure
Model: Defines the data structure for each entity (Task, ReviewExplorer, ReviewExperience, Tag).
Repository: Handles database interactions for the respective entity.
Service: Implements the business logic for each endpoint.
Controller: Exposes the APIs for clients to interact with the system.

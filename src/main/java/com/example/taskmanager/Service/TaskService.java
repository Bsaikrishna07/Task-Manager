package com.example.taskmanager.Service;

import com.example.taskmanager.DTO.TaskDTO;
import com.example.taskmanager.Entity.Task;
import com.example.taskmanager.Repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    // Constructor injection (replaces @RequiredArgsConstructor)
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Convert Entity to DTO
    private TaskDTO convertToDTO(Task task) {
        return new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getCompleted(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }

    // Convert DTO to Entity
    private Task convertToEntity(TaskDTO dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setCompleted(dto.getCompleted() != null ? dto.getCompleted() : false);
        return task;
    }

    // Get all tasks
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get task by ID
    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        return convertToDTO(task);
    }

    // Create new task
    @Transactional
    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = convertToEntity(taskDTO);
        Task savedTask = taskRepository.save(task);
        return convertToDTO(savedTask);
    }

    // Update task
    @Transactional
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        existingTask.setTitle(taskDTO.getTitle());
        existingTask.setDescription(taskDTO.getDescription());
        existingTask.setCompleted(taskDTO.getCompleted());

        Task updatedTask = taskRepository.save(existingTask);
        return convertToDTO(updatedTask);
    }

    // Delete task
    @Transactional
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    // Toggle completion status
    @Transactional
    public TaskDTO toggleTaskCompletion(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        task.setCompleted(!task.getCompleted());
        Task updatedTask = taskRepository.save(task);
        return convertToDTO(updatedTask);
    }

    // Get incomplete tasks
    public List<TaskDTO> getIncompleteTasks() {
        return taskRepository.findByCompleted(false)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Search tasks
    public List<TaskDTO> searchTasks(String keyword) {
        return taskRepository.findByTitleContainingIgnoreCase(keyword)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
package com.example.taskmanager.Controller;
import com.example.taskmanager.DTO.TaskDTO;
import com.example.taskmanager.Service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")

public class TaskController {
    private final TaskService taskService;

    // GET all tasks
    // URL: http://localhost:8080/api/tasks

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<TaskDTO> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    // GET task by ID
    // URL: http://localhost:8080/api/tasks/1
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        try {
            TaskDTO task = taskService.getTaskById(id);
            return ResponseEntity.ok(task);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST create new task
    // URL: http://localhost:8080/api/tasks
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        TaskDTO createdTask = taskService.createTask(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    // PUT update task
    // URL: http://localhost:8080/api/tasks/1
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(
            @PathVariable Long id,
            @RequestBody TaskDTO taskDTO) {
        try {
            TaskDTO updatedTask = taskService.updateTask(id, taskDTO);
            return ResponseEntity.ok(updatedTask);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE task
    // URL: http://localhost:8080/api/tasks/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // PATCH toggle completion
    // URL: http://localhost:8080/api/tasks/1/toggle
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<TaskDTO> toggleTaskCompletion(@PathVariable Long id) {
        try {
            TaskDTO task = taskService.toggleTaskCompletion(id);
            return ResponseEntity.ok(task);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET incomplete tasks
    // URL: http://localhost:8080/api/tasks/incomplete
    @GetMapping("/incomplete")
    public ResponseEntity<List<TaskDTO>> getIncompleteTasks() {
        List<TaskDTO> tasks = taskService.getIncompleteTasks();
        return ResponseEntity.ok(tasks);
    }

    // GET search tasks
    // URL: http://localhost:8080/api/tasks/search?keyword=example
    @GetMapping("/search")
    public ResponseEntity<List<TaskDTO>> searchTasks(@RequestParam String keyword) {
        List<TaskDTO> tasks = taskService.searchTasks(keyword);
        return ResponseEntity.ok(tasks);
    }
}

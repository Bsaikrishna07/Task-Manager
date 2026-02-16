package com.example.taskmanager.Repository;
import com.example.taskmanager.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository

public interface TaskRepository extends JpaRepository<Task, Long> {
    // Find all tasks by completion status
    List<Task> findByCompleted(Boolean completed);

    // Find tasks by title containing a string (case-insensitive)
    List<Task> findByTitleContainingIgnoreCase(String keyword);

    // Custom query using JPQL
    @Query("SELECT t FROM Task t WHERE t.completed = false ORDER BY t.createdAt DESC")
    List<Task> findIncompleteTasks();

    // Native SQL query
    @Query(value = "SELECT * FROM tasks WHERE title LIKE %?1%", nativeQuery = true)
    List<Task> searchByTitle(String keyword);
}

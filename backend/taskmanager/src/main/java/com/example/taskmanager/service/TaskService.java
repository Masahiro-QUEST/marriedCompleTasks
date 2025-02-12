package com.example.taskmanager.service;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // 全タスク取得
    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    // ID指定でタスク取得
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    // タスク作成
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    // タスク更新
    public Task updateTask(Long id, Task updatedTask) {
        return taskRepository.findById(id)
            .map(task -> {
                task.setTitle(updatedTask.getTitle());
                task.setDescription(updatedTask.getDescription());
                task.setCompleted(updatedTask.isCompleted());
                return taskRepository.save(task);
            }).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    // タスク削除
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found");
        }
        taskRepository.deleteById(id);
    }
}

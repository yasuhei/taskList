package br.com.dio.tasklist.domain;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(TaskId taskId) {
        super("Task not found: " + taskId);
    }
}

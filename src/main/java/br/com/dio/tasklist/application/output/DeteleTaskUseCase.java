package br.com.dio.tasklist.application.output;

import br.com.dio.tasklist.domain.TaskId;
import br.com.dio.tasklist.domain.TaskNotFoundException;
import br.com.dio.tasklist.domain.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class DeteleTaskUseCase {
    private final TaskRepository repository;

    public DeteleTaskUseCase(TaskRepository repository) {
        this.repository = repository;
    }

    public void execute(TaskId taskId) {
        if(repository.findById(taskId).isEmpty()) {
            throw new TaskNotFoundException(taskId);
        } else {
            repository.delete(taskId);
        }
    }
}

package br.com.dio.tasklist.application;

import br.com.dio.tasklist.application.output.TaskOutput;
import br.com.dio.tasklist.domain.TaskId;
import br.com.dio.tasklist.domain.TaskNotFoundException;
import br.com.dio.tasklist.domain.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class GetTaskByIdUseCase {
    private final TaskRepository repository;

    public GetTaskByIdUseCase(TaskRepository repository) {
        this.repository = repository;

    }

    public TaskOutput execute(TaskId id) {
        return repository.findById(id).map(TaskOutput::from).orElseThrow(() -> new TaskNotFoundException(id));
    }
}

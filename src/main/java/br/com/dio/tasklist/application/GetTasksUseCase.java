package br.com.dio.tasklist.application;

import br.com.dio.tasklist.application.output.TaskOutput;
import br.com.dio.tasklist.domain.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GetTasksUseCase {

    private final TaskRepository repository;

    public GetTasksUseCase(TaskRepository repository) {
        this.repository = repository;
    }

    public List<TaskOutput> getTasks() {
        return repository.findAll().stream()
                .map(TaskOutput::from)
                .toList();
    }
}

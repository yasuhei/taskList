package br.com.dio.tasklist.application;

import br.com.dio.tasklist.application.input.CreateTaskInput;
import br.com.dio.tasklist.application.output.TaskOutput;
import br.com.dio.tasklist.domain.Task;
import br.com.dio.tasklist.domain.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateTaskUseCase {
    private final TaskRepository repository;

    public CreateTaskUseCase(TaskRepository repository) {
        this.repository = repository;
    }

    public TaskOutput execute(CreateTaskInput input) {
      var task =   new Task(input.title(), input.description());
        var saved =  repository.save(task);
      return TaskOutput.from(saved);

    }
}

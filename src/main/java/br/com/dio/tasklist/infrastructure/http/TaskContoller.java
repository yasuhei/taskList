package br.com.dio.tasklist.infrastructure.http;

import br.com.dio.tasklist.application.CreateTaskUseCase;
import br.com.dio.tasklist.application.input.CreateTaskInput;
import br.com.dio.tasklist.infrastructure.http.request.CreateTaskRequest;
import br.com.dio.tasklist.infrastructure.http.response.TaskResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/tasks")

 class TaskController {
    private final CreateTaskUseCase createTaskUseCase;

    public TaskController(CreateTaskUseCase createTaskUseCase) {
        this.createTaskUseCase = createTaskUseCase;
    }

    @PostMapping
    TaskResponse create(@RequestBody CreateTaskRequest request) {
        var input = request.toInput();
        var output  =  createTaskUseCase.execute(input);
        return TaskResponse.from(output);

    }

}

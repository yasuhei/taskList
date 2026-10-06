package br.com.dio.tasklist.infrastructure.http;

import br.com.dio.tasklist.TaskListApplication;
import br.com.dio.tasklist.application.*;
import br.com.dio.tasklist.application.input.CreateTaskInput;
import br.com.dio.tasklist.application.output.DeteleTaskUseCase;
import br.com.dio.tasklist.domain.TaskId;
import br.com.dio.tasklist.infrastructure.http.request.CreateTaskRequest;
import br.com.dio.tasklist.infrastructure.http.request.UpdateTaskRequest;
import br.com.dio.tasklist.infrastructure.http.response.TaskResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")

 class TaskController {
    private final CreateTaskUseCase createTaskUseCase;
    private final GetTasksUseCase getTasksUseCase;
    private final GetTaskByIdUseCase getTaskByIdUseCase;
    private final DeteleTaskUseCase deleteTaskUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;

    public TaskController(CreateTaskUseCase createTaskUseCase, GetTasksUseCase getTasksUseCase, GetTaskByIdUseCase getTaskByIdUseCase, DeteleTaskUseCase deleteTaskUseCase, UpdateTaskUseCase updateTaskUseCase) {
        this.createTaskUseCase = createTaskUseCase;
        this.getTasksUseCase = getTasksUseCase;
        this.getTaskByIdUseCase = getTaskByIdUseCase;
        this.deleteTaskUseCase = deleteTaskUseCase;
        this.updateTaskUseCase = updateTaskUseCase;
    }

    @PostMapping
    TaskResponse create(@RequestBody @Valid CreateTaskRequest request) {
        var input = request.toInput();
        var output  =  createTaskUseCase.execute(input);
        return TaskResponse.from(output);

    }
    @GetMapping
    List<TaskResponse> list() {
        return getTasksUseCase.execute().stream().map(TaskResponse::from).toList();
    }

    @GetMapping("/{id}")
    TaskResponse read(@PathVariable UUID id) {
        var output = getTaskByIdUseCase.execute(new TaskId(id));
        return TaskResponse.from(output);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable UUID id) {
        deleteTaskUseCase.execute(new TaskId(id));
    }

    @PatchMapping("/{id}")
    TaskResponse update(@PathVariable UUID id, @RequestBody UpdateTaskRequest request) {
        var input = request.toInput();
        var output = updateTaskUseCase.execute(new TaskId(id), input);
        return TaskResponse.from(output);
    }
}

package br.com.dio.tasklist.infrastructure.http.request;

import br.com.dio.tasklist.application.input.CreateTaskInput;

import java.util.Optional;

public record CreateTaskRequest(String title, Optional<String> description) {
    public CreateTaskInput toInput() {
        return new CreateTaskInput(title, description);
    }

}

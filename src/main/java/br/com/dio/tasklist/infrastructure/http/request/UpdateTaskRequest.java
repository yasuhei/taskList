package br.com.dio.tasklist.infrastructure.http.request;

import br.com.dio.tasklist.application.input.UpdateTaskInput;

import java.util.Optional;

public record UpdateTaskRequest(String title, String description, String status) {

    public UpdateTaskInput toInput() {
        return new UpdateTaskInput(
                Optional.ofNullable(title),
                Optional.ofNullable(description),
                Optional.ofNullable(status)
        );
    }
}

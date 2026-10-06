package br.com.dio.tasklist.infrastructure.http.request;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UpdateTaskRequestTest {

    @Test
    void convertsStatusToOptionalInput() {
        var input = new UpdateTaskRequest(null, null, "COMPLETED").toInput();

        assertEquals(Optional.of("COMPLETED"), input.status());
        assertEquals(Optional.empty(), input.title());
        assertEquals(Optional.empty(), input.description());
    }
}

package br.com.dio.tasklist.application.input;

import java.util.Optional;

public record CreateTaskInput(String title, Optional<String> description) {
}

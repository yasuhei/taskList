package br.com.dio.tasklist.application.input;

import java.util.Optional;

public record UpdateTaskInput(Optional<String> title, Optional<String> description, Optional<String> status) {

}

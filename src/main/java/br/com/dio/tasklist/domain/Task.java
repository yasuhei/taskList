package br.com.dio.tasklist.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import java.util.Optional;
@Getter
@Setter
public class Task {
    private TaskId id;
    private String title;
    private Optional<String> description;
    private TaskStatus status;

    public Task(String title, Optional<String> description) {
        Assert.notNull(title, "title must not be null");

        this.id = new TaskId();
        this.title = title;
        this.description = description;
        this.status = TaskStatus.PENDING;
    }

    public void update(Optional<String> title, Optional<String> description, Optional<String> status) {
        title.ifPresent(this::setTitle);
        description.ifPresent(d -> this.description = Optional.of(d));
        status.ifPresent(s -> this.status = TaskStatus.valueOf(s));
    }

}

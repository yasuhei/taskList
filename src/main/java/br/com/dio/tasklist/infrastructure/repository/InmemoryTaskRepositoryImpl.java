package br.com.dio.tasklist.infrastructure.repository;

import br.com.dio.tasklist.domain.Task;
import br.com.dio.tasklist.domain.TaskId;
import br.com.dio.tasklist.domain.TaskRepository;

import java.util.*;

public class InmemoryTaskRepositoryImpl implements TaskRepository {
    private final Map<TaskId, Task> storage = new HashMap<>();

    @Override
    public Task save(Task task) {
        storage.put(task.getId(), task);
        return task;
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(storage.values());

    }

    @Override
    public Optional<Task> findById(TaskId id) {

        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void delete(TaskId id) {
        storage.remove(id);
    }
}

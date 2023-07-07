package com.ll.stringpatterns.api.http.v1;

import com.ll.stringpatterns.api.http.v1.requests.TaskRequest;
import com.ll.stringpatterns.domain.task.Task;
import com.ll.stringpatterns.repositories.TasksRepository;
import com.ll.stringpatterns.usecases.create.CreateTaskCommand;
import com.ll.stringpatterns.usecases.create.CreateTaskUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
public class TasksEndpoints {
    private final CreateTaskUseCase createTaskUsecase;
    private final TasksRepository tasksRepository;

    public TasksEndpoints(@Autowired CreateTaskUseCase createTaskUsecase, @Autowired TasksRepository tasksRepository) {
        this.createTaskUsecase = createTaskUsecase;
        this.tasksRepository = tasksRepository;
    }

    @GetMapping("/tasks")
    public List<Task> getTasks(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int pageSize) {
        return tasksRepository.getTasks(page, pageSize);
    }

    @GetMapping("/task")
    public Task getTask(@RequestParam UUID id) {
        return tasksRepository.getTask(id).orElseThrow();
    }

    @PostMapping("/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public String createTask(@RequestBody TaskRequest taskRequest) {
        return createTaskUsecase.createTask(CreateTaskCommand.fromRequest(taskRequest)).getId().toString();
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNoSuchElementException(
            NoSuchElementException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }
}

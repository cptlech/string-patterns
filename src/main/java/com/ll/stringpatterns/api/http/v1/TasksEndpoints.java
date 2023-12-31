package com.ll.stringpatterns.api.http.v1;

import com.ll.stringpatterns.api.http.v1.requests.TaskRequest;
import com.ll.stringpatterns.api.http.v1.responses.TaskId;
import com.ll.stringpatterns.domain.task.Task;
import com.ll.stringpatterns.repositories.TasksRepository;
import com.ll.stringpatterns.usecases.create.CreateTaskCommand;
import com.ll.stringpatterns.usecases.create.CreateTaskUseCase;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@RestController
@RequiredArgsConstructor
public class TasksEndpoints {
    private final CreateTaskUseCase createTaskUsecase;
    private final TasksRepository tasksRepository;

    @GetMapping("/tasks")
    public List<Task> getTasks(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int pageSize) {
        return tasksRepository.getTasks(page, pageSize);
    }

    @GetMapping("/task")
    public Task getTask(@RequestParam UUID id) {
        return tasksRepository.getTask(id).orElseThrow();
    }

    @PostMapping(value = "/tasks", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody TaskId createTask(@RequestBody TaskRequest taskRequest) {
        if (isEmpty(taskRequest.getPattern())) {
            throw new IllegalArgumentException("Pattern cannot be empty");
        }
        if (isEmpty(taskRequest.getString())) {
            throw new IllegalArgumentException("String cannot be empty");
        }
        return new TaskId(createTaskUsecase.createTask(CreateTaskCommand.fromRequest(taskRequest)).getId().toString());
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

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleIllegalArgumentException(
            IllegalArgumentException exception
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }
}

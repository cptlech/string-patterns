package com.ll.stringpatterns.usecases.create;

import com.ll.stringpatterns.domain.task.Task;
import com.ll.stringpatterns.repositories.TasksRepository;
import com.ll.stringpatterns.usecases.process.ProcessTaskUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;

import static org.mockito.Mockito.verify;

public class CreateTaskUseCaseTest {

    private CreateTaskUseCase createTaskUseCase;
    @Mock
    private TasksRepository tasksRepository;
    @Mock
    private ProcessTaskUseCase processTaskUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.createTaskUseCase = new CreateTaskUseCase(tasksRepository, processTaskUseCase);
    }

    @Test
    public void shouldSaveTheTaskAndStartProcessing() {
        //Given
        int delay = 0;
        Instant now = Instant.now();
        CreateTaskCommand createTaskCommand = new CreateTaskCommand("pattern", "string", delay,
                Clock.fixed(now, ZoneId.of("UTC")),
                ()->UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));

        //When
        createTaskUseCase.createTask(createTaskCommand);

        //Then
        Task task = createTaskCommand.create();
        verify(tasksRepository).store(task);
        verify(processTaskUseCase).process(task, delay);
    }



}

package com.ll.stringpatterns.usecases.create;

import com.ll.stringpatterns.domain.task.Task;
import com.ll.stringpatterns.repositories.TasksRepository;
import com.ll.stringpatterns.usecases.process.ProcessTaskUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTaskUseCase {

    private final TasksRepository taskRepository;
    private final ProcessTaskUseCase processTaskUseCase;

    public Task createTask(CreateTaskCommand createTaskCommand) {
        Task task = createTaskCommand.create();
        taskRepository.store(task);
        processTaskUseCase.process(task, createTaskCommand.getDelayInMilliseconds());
        return task;
    }
}

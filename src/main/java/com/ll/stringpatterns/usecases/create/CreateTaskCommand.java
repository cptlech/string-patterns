package com.ll.stringpatterns.usecases.create;

import com.ll.stringpatterns.api.http.v1.requests.TaskRequest;
import com.ll.stringpatterns.domain.task.Task;

import java.time.Clock;
import java.util.UUID;
import java.util.function.Supplier;

public class CreateTaskCommand {
    private final String string;
    private final String pattern;
    private final int delayInMilliseconds;
    private Clock clock;
    private Supplier<UUID> uuidSupplier;

    public CreateTaskCommand(String pattern, String string, int delayInMilliseconds, Clock clock, Supplier<UUID>
            uuidSupplier) {
        this.string = string;
        this.pattern = pattern;
        this.delayInMilliseconds = delayInMilliseconds;
        this.clock = clock;
        this.uuidSupplier = uuidSupplier;
    }

    public static CreateTaskCommand fromRequest(TaskRequest taskRequest) {
        return new CreateTaskCommand(taskRequest.getPattern(), taskRequest.getString(), taskRequest.getDelay(),
                Clock.systemUTC(), UUID::randomUUID);
    }

    public Task create() {
        return new Task(this.pattern, this.string, this.clock, this.uuidSupplier);
    }

    public int getDelayInMilliseconds() {
        return delayInMilliseconds;
    }
}

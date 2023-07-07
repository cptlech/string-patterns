package com.ll.stringpatterns.domain.task;

import lombok.*;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Supplier;


@Data
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Task {

    @Setter(AccessLevel.NONE)
    private UUID id;
    private String pattern;
    private String string;
    private int progress;
    private Result result;
    private LocalDateTime created;

    public Task(String pattern, String string, Clock clock, Supplier<UUID> uuidSupplier) {
        this.pattern = pattern;
        this.string = string;
        this.progress = 0;
        this.id = uuidSupplier.get();
        this.result = Result.empty();
        this.created = LocalDateTime.now(clock);
    }

}

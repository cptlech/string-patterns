package com.ll.stringpatterns.repositories;

import com.ll.stringpatterns.domain.task.Task;
import com.ll.stringpatterns.jooq.tables.records.TaskRecord;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ll.stringpatterns.jooq.tables.Task.TASK;

@Repository
public class TasksRepository {
    private DSLContext dslContext;

    public TasksRepository(@Autowired DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public List<Task> getTasks(int page, int pageSize) {
        return dslContext.select().from(TASK).orderBy(TASK.CREATED.desc())
        .offset((page-1)*pageSize).limit(pageSize).fetch().map(record->toTask(record));
    }

    public void store(Task task) {
        TaskRecord taskRecord = toRecord(task);
        taskRecord.store();
    }

    public void update(Task task) {
        TaskRecord taskRecord = toRecord(task);
        taskRecord.update();
    }

    private Task toTask(Record taskRecord) {
        return new Task(taskRecord.get(TASK.ID),
                taskRecord.get(TASK.PATTERN),
                taskRecord.get(TASK.STRING),
                taskRecord.get(TASK.PROGRESS),
                new com.ll.stringpatterns.domain.task.Result(
                        taskRecord.get(TASK.PATTERN_FOUND),
                        taskRecord.get(TASK.PATTERN_OFFSET),
                        taskRecord.get(TASK.NUMBER_OF_TYPOS)
                ),
                taskRecord.get(TASK.CREATED));
    }

    private TaskRecord toRecord(Task task) {
        TaskRecord taskRecord = dslContext.newRecord(TASK, task);
        taskRecord.setPatternFound(task.getResult().isPatternFound());
        taskRecord.setPatternOffset(task.getResult().getPatternOffset());
        taskRecord.setNumberOfTypos(task.getResult().getNumberOfTypos());
        return taskRecord;
    }

    public void deleteAllTasks() {
        dslContext.truncate(TASK).execute();
    }

    public Optional<Task> getTask(UUID id) {
        Result<Record> result = dslContext.select().from(TASK).where(TASK.ID.eq(id)).fetch();
        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(toTask(result.get(0)));
        }
    }
}

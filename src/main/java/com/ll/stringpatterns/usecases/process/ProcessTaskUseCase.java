package com.ll.stringpatterns.usecases.process;


import com.ll.stringpatterns.domain.task.Result;
import com.ll.stringpatterns.domain.task.Task;
import com.ll.stringpatterns.repositories.TasksRepository;
import com.ll.stringpatterns.usecases.process.searchpattern.PatternSearchResult;
import com.ll.stringpatterns.usecases.process.searchpattern.ProgressNotifier;
import com.ll.stringpatterns.usecases.process.searchpattern.StringPatternSearcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Service
public class ProcessTaskUseCase {
    private ExecutorService executorService;
    private TasksRepository taskRepository;
    private StringPatternSearcher stringPatternSearcher;

    public ProcessTaskUseCase(@Autowired TasksRepository taskRepository,
                              @Autowired StringPatternSearcher stringPatternSearcher,
                              @Autowired ExecutorService executorService) {
        this.taskRepository = taskRepository;
        this.stringPatternSearcher = stringPatternSearcher;
        this.executorService = executorService;
    }

    public void process(Task task, int delayInMilliseconds) {
        executorService.submit(() -> {

            PatternSearchResult patternSearchResult =
                    stringPatternSearcher.run(task.getPattern(), task.getString(),
                            new ProgressNotifier(progress-> {task.setProgress(progress); taskRepository.update(task);}),
                            delayInMilliseconds);

            Result taskResult = new Result(
                    patternSearchResult.isPatternFound(),
                    patternSearchResult.getPatternOffset(),
                    patternSearchResult.getNumberOfTypos());

            task.setResult(taskResult);
            taskRepository.update(task);
        });
    }
}

@Configuration
class ProcessTaskUseCaseConfiguration {
    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(100);
    }
}

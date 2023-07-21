package com.ll.stringpatterns.usecases.process;

import com.ll.stringpatterns.domain.task.Result;
import com.ll.stringpatterns.domain.task.Task;
import com.ll.stringpatterns.repositories.TasksRepository;
import com.ll.stringpatterns.usecases.process.searchpattern.PatternSearchResult;
import com.ll.stringpatterns.usecases.process.searchpattern.ProgressNotifier;
import com.ll.stringpatterns.usecases.process.searchpattern.StringPatternSearcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutorService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class ProcessTaskUseCaseTest {
    private ProcessTaskUseCase processTaskUseCase;
    @Mock
    private TasksRepository tasksRepository;
    @Mock
    private StringPatternSearcher stringPatternSearcher;
    @Mock
    private ExecutorService executorService;
    @Captor
    private ArgumentCaptor<Runnable> runnable;
    @Captor
    private ArgumentCaptor<ProgressNotifier> progressNotifier;

    @BeforeEach
    public void setUp() {
        this.processTaskUseCase = new ProcessTaskUseCase(tasksRepository, stringPatternSearcher, executorService);
    }

    @Test
    public void shouldSubmitATaskToExecutorService() {
        //Given
        Task task = Task.builder().pattern("pattern").string("string").build();
        int delay = 0;
        PatternSearchResult patternSearchResult = new PatternSearchResult();

        //When
        processTaskUseCase.process(task, delay);

        //Then
        verify(executorService).submit(runnable.capture());

        //When
        when(stringPatternSearcher.run(eq("pattern"), eq("string"), any(ProgressNotifier.class), eq(delay)))
                .thenReturn(patternSearchResult);
        runnable.getValue().run();

        //Then
        verify(stringPatternSearcher).run(eq("pattern"), eq("string"), progressNotifier.capture(), eq(delay));
        task.setResult(new Result(false, null, null));
        verify(tasksRepository).update(task);

        //When
        Mockito.reset(tasksRepository);
        progressNotifier.getValue().notify(50);

        //Then
        task.setProgress(50);
        verify(tasksRepository).update(task);
    }


}

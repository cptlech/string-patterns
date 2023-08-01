package com.ll.stringpatterns.api.http.v1;

import com.ll.stringpatterns.domain.task.Result;
import com.ll.stringpatterns.domain.task.Task;
import com.ll.stringpatterns.repositories.TasksRepository;
import org.hamcrest.text.MatchesPattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.hamcrest.core.StringRegularExpression;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.matches;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test-containers-flyway")
public class TasksEndpointsTest {
    private static final String UUID_REGEX = "[0-9a-fA-F]{8}(?:-[0-9a-fA-F]{4}){3}-[0-9a-fA-F]{12}";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TasksRepository tasksRepository;

    @BeforeEach
    public void setUp() {
        tasksRepository.deleteAllTasks();
    }

    @Test
    public void shouldReturn404ForNonExistentTask() throws Exception {
        this.mockMvc.perform(get("/task?id=df1dedba-1f96-11ee-be56-0242ac120002"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnTaskJSON() throws Exception {
        Task task = Task.builder().id(UUID.fromString("fc5709f8-be14-4e5d-a8cf-183fabc66586")).string("string").pattern("pattern")
                        .progress(0).created(LocalDateTime.of(2023, 7, 7, 10, 0))
                .result(Result.empty()).build();
        tasksRepository.store(task);

        this.mockMvc.perform(get("/task?id="+task.getId()))
                .andExpect(status().isOk()).andExpect(content().json("{\"id\":\"fc5709f8-be14-4e5d-a8cf-183fabc66586\",\"pattern\":\"pattern\",\"string\":\"string\",\"progress\":0,\"result\":{\"patternFound\":false,\"patternOffset\":null,\"numberOfTypos\":null},\"created\":\"2023-07-07T10:00:00\"}"));
    }

    @Test
    public void shouldReturnEmptyTasksListJSON() throws Exception {
        this.mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    public void shouldReturnTasksListJSON() throws Exception {
        Task task = Task.builder().id(UUID.fromString("fc5709f8-be14-4e5d-a8cf-183fabc66586")).string("string").pattern("pattern")
                .progress(0).created(LocalDateTime.of(2023, 7, 7, 10, 0))
                .result(Result.empty()).build();
        tasksRepository.store(task);

        this.mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk()).andExpect(content().json("[{\"id\":\"fc5709f8-be14-4e5d-a8cf-183fabc66586\",\"pattern\":\"pattern\",\"string\":\"string\",\"progress\":0,\"result\":{\"patternFound\":false,\"patternOffset\":null,\"numberOfTypos\":null},\"created\":\"2023-07-07T10:00:00\"}]"));
    }

    @Test
    public void shouldReturnCreatedTaskIdJSON() throws Exception {
        this.mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"string\":\"AAA\",\"pattern\":\"B\"}"))
                        .andExpect(status().isCreated())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.taskId").value(MatchesPattern.matchesPattern(UUID_REGEX)));
    }
}

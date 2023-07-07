package com.ll.stringpatterns.repositories;

import com.ll.stringpatterns.domain.task.Result;
import com.ll.stringpatterns.domain.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test-containers-flyway")
public class TaskRepositoryTest {
   @Autowired
   private TasksRepository tasksRepository;

   @BeforeEach
   public void setUp() {
      tasksRepository.deleteAllTasks();
   }

   @Test
   void shouldGetAnEmptyListWhenNoTasks() {
      //When
       List<Task> tasks = tasksRepository.getTasks(1, 10);

       //Then
       assertTrue(tasks.isEmpty());
   }

   @Test
    void shouldGetAListOfTasksOrderedByDateTime(){
      //Given
       Task task1 = Task.builder().id(UUID.randomUUID()).string("AABBC").pattern("AB")
               .progress(0).result(Result.empty()).created(LocalDateTime.of(2023,7,7,10,0)).build();
       Task task2 = Task.builder().id(UUID.randomUUID()).string("BBCD").pattern("CA").progress(0)
               .result(Result.empty()).created(LocalDateTime.of(2023,7,7,10,1)).build();
       tasksRepository.store(task1);
       tasksRepository.store(task2);

       //When
       List<Task> tasks = tasksRepository.getTasks(1, 10);

       //Then
       assertEquals(List.of(task2, task1), tasks);
   }

   @Test
   void shouldGetAPageOfTasks(){
      //Given
      Task task1 = Task.builder().id(UUID.randomUUID()).string("AABBC").pattern("AB")
              .progress(0).result(Result.empty()).created(LocalDateTime.of(2023,7,7,10,0)).build();
      Task task2 = Task.builder().id(UUID.randomUUID()).string("BBCD").pattern("CA").progress(0)
              .result(Result.empty()).created(LocalDateTime.of(2023,7,7,10,1)).build();
      tasksRepository.store(task1);
      tasksRepository.store(task2);

      //When
      List<Task> tasks = tasksRepository.getTasks(2, 1);

      //Then
      assertEquals(List.of(task1), tasks);
   }

   @Test
   void shouldUpdateTask(){
      //Given
      Task task = Task.builder().id(UUID.randomUUID()).string("AABBC").pattern("AB").progress(0).result(Result.empty()).build();
      tasksRepository.store(task);

      //When
      task.setProgress(81);
      tasksRepository.update(task);

      //Then
      List<Task> tasks = tasksRepository.getTasks(1, 10);
      assertEquals(List.of(task), tasks);
   }

   @Test
   void shouldGetTaskWhenExists(){
      //Given
      Task task = Task.builder().id(UUID.randomUUID()).string("AABBC").pattern("AB").progress(0).result(Result.empty()).build();
      tasksRepository.store(task);

      //When
      Optional<Task> optionalRetrievedTask = tasksRepository.getTask(task.getId());

      //Then
      assertEquals(task, optionalRetrievedTask.get());
   }

   @Test
   void shouldGetEmptyOptionalWhenTaskDoesNotExist(){
      //When
      Optional<Task> optionalRetrievedTask = tasksRepository.getTask(UUID.randomUUID());

      //Then
      assertTrue(optionalRetrievedTask.isEmpty());
   }
}

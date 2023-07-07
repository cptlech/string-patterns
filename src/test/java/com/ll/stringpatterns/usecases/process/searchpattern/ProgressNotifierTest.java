package com.ll.stringpatterns.usecases.process.searchpattern;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProgressNotifierTest {

    @Test
    public void shouldNotifyProgressToConsumer() {
        //Given
        List<Integer> progresses = new ArrayList<>();
        ProgressNotifier progressNotifier = new ProgressNotifier(progress -> {progresses.add(progress);});

        //When
        progressNotifier.notify(0);
        progressNotifier.notify(50);

        //Then
        assertEquals(List.of(0, 50), progresses);
    }
}

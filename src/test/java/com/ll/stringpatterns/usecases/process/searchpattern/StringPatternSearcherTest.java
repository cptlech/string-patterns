package com.ll.stringpatterns.usecases.process.searchpattern;

import com.ll.stringpatterns.usecases.process.searchpattern.PatternSearchResult;
import com.ll.stringpatterns.usecases.process.searchpattern.ProgressNotifier;
import com.ll.stringpatterns.usecases.process.searchpattern.StringPatternSearcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringPatternSearcherTest {
    private StringPatternSearcher stringPatternSearcher;

    @BeforeEach
    public void setUp() {
        this.stringPatternSearcher = new StringPatternSearcher();
    }

    @Test
    public void shouldReturnAnEmptyResultWhenNoPatternFound() {
        //Given
        String string = "AAA";
        String pattern = "B";

        //When
        PatternSearchResult result = stringPatternSearcher.run(pattern, string, new ProgressNotifier(progress->{}), 0);

        //Then
        assertFalse(result.isPatternFound());
        assertNull(result.getPatternOffset());
        assertNull(result.getNumberOfTypos());
    }

    @Test
    public void shouldReturnFullyMatchedResult() {
        //Given
        String string = "ABCD";
        String pattern = "BCD";

        //When
        PatternSearchResult result = stringPatternSearcher.run(pattern, string, new ProgressNotifier(progress->{}), 0);

        //Then
        assertTrue(result.isPatternFound());
        assertEquals(1, result.getPatternOffset());
        assertEquals(0, result.getNumberOfTypos());
    }

    @Test
    public void shouldReturnBestPartialMatch() {
        //Given
        String string = "ABCDEFG";
        String pattern = "CFG";

        //When
        PatternSearchResult result = stringPatternSearcher.run(pattern, string, new ProgressNotifier(progress->{}), 0);

        //Then
        assertTrue(result.isPatternFound());
        assertEquals(4, result.getPatternOffset());
        assertEquals(1, result.getNumberOfTypos());
    }

    @Test
    public void shouldReturnFirstResultWhenPatternFullyMatched() {
        //Given
        String string = "ABCABC";
        String pattern = "ABC";

        //When
        PatternSearchResult result = stringPatternSearcher.run(pattern, string, new ProgressNotifier(progress->{}), 0);

        //Then
        assertTrue(result.isPatternFound());
        assertEquals(0, result.getPatternOffset());
        assertEquals(0, result.getNumberOfTypos());
    }

    @Test
    public void shouldReturnPartiallyMatchedPattern() {
        //Given
        String string = "ABCD";
        String pattern = "BWD";

        //When
        PatternSearchResult result = stringPatternSearcher.run(pattern, string, new ProgressNotifier(progress->{}), 0);

        //Then
        assertTrue(result.isPatternFound());
        assertEquals(1, result.getPatternOffset());
        assertEquals(1, result.getNumberOfTypos());
    }

    @Test
    public void shouldReturnFirstResultWhenPatternPartiallyMatched() {
        //Given
        String string = "ABCDEFG";
        String pattern = "TDD";

        //When
        PatternSearchResult result = stringPatternSearcher.run(pattern, string, new ProgressNotifier(progress->{}), 0);

        //Then
        assertTrue(result.isPatternFound());
        assertEquals(1, result.getPatternOffset());
        assertEquals(2, result.getNumberOfTypos());
    }
}
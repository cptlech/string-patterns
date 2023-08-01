package com.ll.stringpatterns.usecases.process.searchpattern;

import org.springframework.stereotype.Component;

@Component
public class StringPatternSearcher {

    public PatternSearchResult run(String pattern, String string, ProgressNotifier progressNotifier,
                                   int delayInMilliseconds) {
        int[] matchesCount = new int[string.length()];
        int currentIndex = 0;
        progressNotifier.notify(0);
        while (currentIndex < string.length()) {
            int numberOfMatches = countNumberOfMatches(pattern, string, currentIndex);
            matchesCount[currentIndex] = numberOfMatches;
            currentIndex++;
            progressNotifier.notify((currentIndex*100)/string.length());
            if (numberOfMatches == pattern.length()) {
                break;
            }
            try {
                Thread.sleep(delayInMilliseconds);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        progressNotifier.notify(100);

        return calculatePatternSearchResult(pattern, matchesCount);
    }

    private static int countNumberOfMatches(String pattern, String string, int startingIndex) {
        int currentPatternIndex = 0;
        int numberOfMatches = 0;
        while (startingIndex + currentPatternIndex < string.length() && currentPatternIndex < pattern.length()) {
            if (string.charAt(startingIndex +currentPatternIndex) == pattern.charAt(currentPatternIndex)) {
                numberOfMatches++;
            }
            currentPatternIndex++;
        }
        return numberOfMatches;
    }
    private static PatternSearchResult calculatePatternSearchResult(String pattern, int[] matchesCount) {
        int max = matchesCount[0];
        int maxIndex = 0;
        for (int i = 1; i < matchesCount.length; i++) {
            if (max < matchesCount[i]) {
                max = matchesCount[i];
                maxIndex = i;
            }
        }
        PatternSearchResult result = new PatternSearchResult();
        if (max == 0) {
            return result;
        }

        result.setPatternFound(true);
        result.setPatternOffset(maxIndex);
        result.setNumberOfTypos(pattern.length()- matchesCount[maxIndex]);
        return result;
    }
}

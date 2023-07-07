package com.ll.stringpatterns.usecases.process.searchpattern;

import lombok.Data;

@Data
public class PatternSearchResult {
    private boolean patternFound;
    private String pattern;
    private Integer patternOffset;
    private Integer numberOfTypos;
}

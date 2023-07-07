package com.ll.stringpatterns.domain.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class Result {
    private boolean patternFound;
    private Integer patternOffset;
    private Integer numberOfTypos;

    private Result(boolean patternFound) {
        this.patternFound = false;
        this.patternOffset = null;
        this.numberOfTypos = null;
    }

    public static Result empty() {
        return new Result(false);
    }
}
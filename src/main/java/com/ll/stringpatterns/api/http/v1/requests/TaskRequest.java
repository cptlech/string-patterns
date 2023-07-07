package com.ll.stringpatterns.api.http.v1.requests;

import lombok.Data;

@Data
public class TaskRequest {

    private String pattern;
    private String string;
    private int delay = 2000;
}

package com.ll.stringpatterns.usecases.process.searchpattern;

import java.util.function.Consumer;

public class ProgressNotifier {

    private final Consumer<Integer> progressConsumer;

    public ProgressNotifier(Consumer<Integer> progressConsumer) {
        this.progressConsumer = progressConsumer;
    }
    public void notify(int progress) {
        progressConsumer.accept(progress);
    }
}

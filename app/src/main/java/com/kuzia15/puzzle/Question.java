package com.kuzia15.puzzle;

public class Question {

    String correct;
    String fact;
    String[] options;

    public Question(String correct, String fact, String... options) {
        this.correct = correct;
        this.fact = fact;
        this.options = options;
    }
}
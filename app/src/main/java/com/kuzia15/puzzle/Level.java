package com.kuzia15.puzzle;

public class Level {

    public final int image;
    public final String answer;
    public final String fact;
    public final String[] variants;

    public Level(int image, String answer, String fact, String... variants) {
        this.image = image;
        this.answer = answer;
        this.fact = fact;
        this.variants = variants;
    }
}
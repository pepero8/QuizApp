package com.example.quizapp;

import androidx.annotation.NonNull;

public class Player {
    private String name;
    private int score;

    public Player() {}

    public Player(String name, int score) {
        this.name = name;
        this.score = score;
    }

    @NonNull
    @Override
    public String toString() {
        return name + ": " + score;
    }
}

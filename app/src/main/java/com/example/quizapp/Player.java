package com.example.quizapp;

import androidx.annotation.NonNull;

public class Player implements Comparable<Player> {
    private final String name;
//    private long topic1Score = 0;
//    private long topic2Score = 0;
//    private long topic3Score = 0;
//    private long topic4Score = 0;
    long score;

//    public Player(String name) {
//        this.name = name;
//    }

    public Player(String name, long score) {
        this.name = name;
        this.score = score;
    }

//    public void setScore(String topic, long score) {
//        switch (topic) {
//            case "역사":
//                topic1Score = score;
//                break;
//
//            case "인물":
//                topic2Score = score;
//                break;
//
//            case "과학":
//                topic3Score = score;
//                break;
//
//            case "외대":
//                topic4Score = score;
//                break;
//        }
//    }

//    @NonNull
//    @Override
//    public String toString() {
//        return name + " 역사:" + topic1Score +
//                      " 인물:" + topic2Score +
//                      " 과학:" + topic3Score +
//                      " 외대:" + topic4Score +
//                      " 총점:" + (topic1Score + topic2Score + topic3Score + topic4Score);
//    }

    @NonNull
    @Override
    public String toString() {
        return name + "   " + score;
    }

    @Override
    public int compareTo(Player player) {
        return (int) (player.score - score);
    }
}

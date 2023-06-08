package com.example.quizapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DataBase {
    interface Callback {
        void onDataReceived(HashMap<String, Long> scores);
    }
    private final static String URL = "https://uquiziquiz-ranking-default-rtdb.asia-southeast1.firebasedatabase.app/";
//    private static HashMap<String, Integer> scores;
    static void getScores(Callback callback, String topic) {
        FirebaseDatabase database = FirebaseDatabase.getInstance(URL);
        DatabaseReference ref = database.getReference(topic);
        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    callback.onDataReceived(null);
                }
                else {
                    HashMap<String, Long> scores = (HashMap<String, Long>) task.getResult().getValue();
                    Log.d("firebase", String.valueOf(scores));
                    callback.onDataReceived(scores);
                }
            }
        });
    }
    static void submitScore(String playerName, int score, String topic) {
        FirebaseDatabase database = FirebaseDatabase.getInstance(URL);
        DatabaseReference ref = database.getReference(topic);
        ref.child(playerName).setValue(score);
        Log.d("firebase", "submitted score - " + playerName + ", " + topic + ", " + score);
    }
}

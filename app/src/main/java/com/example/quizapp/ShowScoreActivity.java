package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ShowScoreActivity extends AppCompatActivity {
    private static final String TAG = "ShowScoreActivity";
    private String playername;
    private int score;
    private String topic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_score);

        // 하단 소프트바 제거
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        uiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        uiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);

        TextView scoreText = findViewById(R.id.scoreText);
        Button okButton = findViewById(R.id.okButton);
        Button rankingButton = findViewById(R.id.rankingButton2);

        Intent intent = getIntent();
        int size;
        if (intent != null) {
            playername = intent.getStringExtra("playername");
            score = intent.getIntExtra("score", 0);
            topic = intent.getStringExtra("topic");
            size = intent.getIntExtra("size", 0);
            scoreText.setText(score + "/" + size);
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowScoreActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        rankingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShowRankingActivity.class);
                startActivity(intent);
            }
        });

        DataBase.submitScore(playername, score, topic);
    }
}

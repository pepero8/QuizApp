package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SelectTopicActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_topic);

        String playername = getIntent().getStringExtra("playername");

        Button topic1 = findViewById(R.id.topic1);
        Button topic2 = findViewById(R.id.topic2);
        Button topic3 = findViewById(R.id.topic3);
        Button topic4 = findViewById(R.id.topic4);

        View.OnClickListener topicButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectTopicActivity.this, QuestionActivity.class);
                String topic = ((Button)view).getText().toString();
                Toast.makeText(SelectTopicActivity.this, "선택한 주제: " + topic, Toast.LENGTH_SHORT).show();
                intent.putExtra("playername", playername);
                intent.putExtra("topic", topic);
                startActivity(intent);
                finish();
            }
        };

        topic1.setOnClickListener(topicButtonListener);
        topic2.setOnClickListener(topicButtonListener);
        topic3.setOnClickListener(topicButtonListener);
        topic4.setOnClickListener(topicButtonListener);

    }
}

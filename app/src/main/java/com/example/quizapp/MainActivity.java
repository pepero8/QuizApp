package com.example.quizapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Questions questions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button nextButton = findViewById(R.id.nextButton);
        TextView question = findViewById(R.id.question);
        RadioButton choiceButton1 = findViewById(R.id.choice1);
        RadioButton choiceButton2 = findViewById(R.id.choice2);
        RadioButton choiceButton3 = findViewById(R.id.choice3);

        HashMap<String, String> questionMap = new HashMap<>();
        questionMap.put("한국외대 총장의 이름은?", "박정운:아이유:박명수");
        questionMap.put("내가 오늘 아침에 먹은 음식은?", "안먹음:된장찌개:김치찌개");
        questionMap.put("내 생일은?", "8월14일:1월30일:6월3일");
        questions = new Questions(questionMap);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Questions.QnA nextQnA = questions.next();
                question.setText(nextQnA.getQuestion());
                choiceButton1.setText(nextQnA.getChoices()[0]);
                choiceButton2.setText(nextQnA.getChoices()[1]);
                choiceButton3.setText(nextQnA.getChoices()[2]);
            }
        });
    }
}

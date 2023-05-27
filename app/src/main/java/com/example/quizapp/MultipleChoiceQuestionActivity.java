package com.example.quizapp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MultipleChoiceQuestionActivity extends AppCompatActivity {

    private static final String TAG = "객관식Activity";
    private Questions questions;

    TextView question;
    Button choiceButton1, choiceButton2, choiceButton3, choiceButton4;
    ProgressBar progressBar;
    TextView textViewTime;
    int correctCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Button nextButton = findViewById(R.id.nextButton);
        question = findViewById(R.id.question);
        choiceButton1 = findViewById(R.id.choice1);
        choiceButton2 = findViewById(R.id.choice2);
        choiceButton3 = findViewById(R.id.choice3);
        choiceButton4 = findViewById(R.id.choice4);
        progressBar = findViewById(R.id.progressBar);
        textViewTime = findViewById(R.id.textViewTime);

        CountDownTimer timer = new CountDownTimer(90000, 100) {
            @Override
            public void onTick(long l) {
                textViewTime.setText(hmsTimeFormatter(l));
                int current = progressBar.getProgress() - 1;
                progressBar.setProgress(current);
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(0);
                Toast.makeText(MultipleChoiceQuestionActivity.this, "End!", Toast.LENGTH_SHORT).show();
            }
        };
        timer.start();

        // 문제 모음집 객체 생성&초기화
        HashMap<String, String> questionMap = new HashMap<>();
        // "질문", "정답:오답:오답:오답"
        questionMap.put("한국외대 총장의 이름은?", "박정운:아이유:박명수:김인철");
        questionMap.put("내가 오늘 아침에 먹은 음식은?", "안먹음:된장찌개:김치찌개:부대찌개");
        questionMap.put("내 생일은?", "8월14일:1월30일:6월3일:12월30일");
        questionMap.put("매일 요구르트의 칼로리는?", "30kcal:25kcal:35kcal:20kcal");
        questions = new Questions(questionMap);

        View.OnClickListener choiceButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 정답인지 확인
                String text = ((Button)view).getText().toString();
                Questions.QnA qna = questions.current();
                if (text.equals(qna.getAnswer())) {
                    Log.d(TAG, "correct");
                    correctCount++;
                }

                // 다음 문제 로드
                loadNextQuestion();
            }
        };

        choiceButton1.setOnClickListener(choiceButtonListener);
        choiceButton2.setOnClickListener(choiceButtonListener);
        choiceButton3.setOnClickListener(choiceButtonListener);
        choiceButton4.setOnClickListener(choiceButtonListener);

        // 첫번째 문제 로드
        loadNextQuestion();

        // choiceButton1.setOnClickListener();
//        nextButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Questions.QnA nextQnA = questions.next();
//                question.setText(nextQnA.getQuestion());
//                choiceButton1.setText(nextQnA.getChoices()[0]);
//                choiceButton2.setText(nextQnA.getChoices()[1]);
//                choiceButton3.setText(nextQnA.getChoices()[2]);
//            }
//        });
    }

    public void loadNextQuestion() {
        Questions.QnA nextQnA = questions.next();
        question.setText("Q. " + nextQnA.getQuestion());
        String[] choices = nextQnA.getChoices();
        choiceButton1.setText(choices[0]);
        choiceButton2.setText(choices[1]);
        choiceButton3.setText(choices[2]);
        choiceButton4.setText(choices[3]);
    }

    private static String hmsTimeFormatter(long l) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(l) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(l)),
                TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));
    }
}

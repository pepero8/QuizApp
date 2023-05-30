package com.example.quizapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class QuestionActivity extends AppCompatActivity {
    private static final String TAG = "QuestionActivity";
    private static final long TIME_MS = 20000L;
    private Questions questions;
    private TextView multipleChoiceQuestionText, subjectiveQuestionText;
    private EditText subjectiveAnswerText;
    private Button choiceButton1, choiceButton2, choiceButton3, choiceButton4;
    private Button buttonSubmit;
    private ProgressBar progressBar;
    private TextView textViewTime;
    private int correctCount = 0;

    private ViewGroup questionChildLayout;
    private View subjectiveQ, multipleChoiceQ;
    private View currentView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        initializeVariables();

        // 문제 모음집 객체 생성&초기화
        HashMap<String, String> questionMap = new HashMap<>();
        // "타입:질문", "정답:오답:오답:오답"
        questionMap.put("M:한국외대 총장의 이름은?", "박정운:아이유:박명수:김인철");
        questionMap.put("M:내가 오늘 아침에 먹은 음식은?", "안먹음:된장찌개:김치찌개:부대찌개");
        questionMap.put("M:내 생일은?", "8월14일:1월30일:6월3일:12월30일");
        questionMap.put("M:매일 요구르트의 칼로리는?", "30kcal:25kcal:35kcal:20kcal");
        questionMap.put("S:우리나라 이름은?", "대한민국");
        questionMap.put("S:현재 년도는?", "2023");
        questionMap.put("S:1+1은?", "2");
        questions = new Questions(questionMap);

        subjectiveAnswerText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    // When EditText gains focus, clear the hint text
                    ((EditText)view).setHint("");
                } else {
                    // When EditText loses focus, set the hint text
                    ((EditText)view).setHint("답변 입력");
                }
            }
        });
        subjectiveAnswerText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    view.clearFocus();
                    return true;
                }
                return false;
            }
        });

        // 객관식 문제 layout의 선택지 버튼의 리스너
        View.OnClickListener choiceButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 정답인지 확인
                String text = ((Button)view).getText().toString();
                Questions.QnA qna = questions.current();
                if (text.equals(qna.getAnswer())) {
                    Log.d(TAG, "correct");
                    Toast.makeText(QuestionActivity.this, "정답입니다!", Toast.LENGTH_SHORT).show();
                    correctCount++;
                }
                else {
                    Toast.makeText(QuestionActivity.this, "오답입니다", Toast.LENGTH_SHORT).show();
                }

                // 다음 문제 로드
                loadNextQuestion();
            }
        };

        choiceButton1.setOnClickListener(choiceButtonListener);
        choiceButton2.setOnClickListener(choiceButtonListener);
        choiceButton3.setOnClickListener(choiceButtonListener);
        choiceButton4.setOnClickListener(choiceButtonListener);

        // 주관식 문제 layout의 제출 버튼에 리스너 등록
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = subjectiveAnswerText.getText().toString();
                Questions.QnA qna = questions.current();
                if (text.equals(qna.getAnswer())) {
                    Log.d(TAG, "correct");
                    Toast.makeText(QuestionActivity.this, "정답입니다!", Toast.LENGTH_SHORT).show();
//                    subjectiveAnswerText.setText("정답입니다!");
                    correctCount++;
                }
                else {
                    Toast.makeText(QuestionActivity.this, "오답입니다", Toast.LENGTH_SHORT).show();
//                    subjectiveAnswerText.setText("오답입니다");
                }

                // 다음 문제 로드
                loadNextQuestion();
            }
        });

        loadNextQuestion();

        // 시간 제한 설정
        progressBar.setMax((int) TIME_MS);
        progressBar.setProgress((int) TIME_MS);
        CountDownTimer timer = new CountDownTimer(TIME_MS, 100) {
            @Override
            public void onTick(long l) {
                textViewTime.setText(hmsTimeFormatter(l));
                int current = progressBar.getProgress() - 100;
                progressBar.setProgress(current);
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(0);
                Toast.makeText(QuestionActivity.this, "총 맞춘 개수: " + correctCount, Toast.LENGTH_SHORT).show();
            }
        };
        timer.start();
    }

    private void initializeVariables() {
        questionChildLayout = findViewById(R.id.questionChildLayout);
        subjectiveQ = getLayoutInflater().inflate(R.layout.subjective_question, questionChildLayout, false);
        multipleChoiceQ = getLayoutInflater().inflate(R.layout.multiplechoice_question, questionChildLayout, false);
//        questionChildLayout.addView(subjectiveQ);

        subjectiveQuestionText = subjectiveQ.findViewById(R.id.textViewQuestion);
        multipleChoiceQuestionText = multipleChoiceQ.findViewById(R.id.question);
        subjectiveAnswerText = subjectiveQ.findViewById(R.id.answer);
        buttonSubmit = subjectiveQ.findViewById(R.id.buttonSubmit);
        choiceButton1 = multipleChoiceQ.findViewById(R.id.choice1);
        choiceButton2 = multipleChoiceQ.findViewById(R.id.choice2);
        choiceButton3 = multipleChoiceQ.findViewById(R.id.choice3);
        choiceButton4 = multipleChoiceQ.findViewById(R.id.choice4);
        progressBar = findViewById(R.id.progressBar);
        textViewTime = findViewById(R.id.textViewTime);
    }

    private static String hmsTimeFormatter(long l) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(l) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(l)),
                TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));
    }

    public void loadNextQuestion() {
        Questions.QnA nextQnA = questions.next();
        if (nextQnA instanceof Questions.MultipleChoiceQnA) {
            changeToMultipleChoiceQview();

            multipleChoiceQuestionText.setText("Q. " + nextQnA.getQuestion());
            String[] choices = ((Questions.MultipleChoiceQnA) nextQnA).getChoices();
            choiceButton1.setText(choices[0]);
            choiceButton2.setText(choices[1]);
            choiceButton3.setText(choices[2]);
            choiceButton4.setText(choices[3]);
        }
        else if (nextQnA instanceof Questions.SubjectiveQnA) {
            changeToSubjectiveQview();

            subjectiveQuestionText.setText("Q. " + nextQnA.getQuestion());
            subjectiveAnswerText.setHint("답변 입력");
            subjectiveAnswerText.setText("");
        }
    }

    private void changeToSubjectiveQview() {
        if (currentView != null && currentView == subjectiveQ) return;
        // subjectiveQ = getLayoutInflater().inflate(R.layout.subjective_question, questionChildLayout, false);
        questionChildLayout.removeView(multipleChoiceQ);
        questionChildLayout.addView(subjectiveQ);
        currentView = subjectiveQ;
    }

    private void changeToMultipleChoiceQview() {
        if (currentView != null && currentView == multipleChoiceQ) return;
        // subjectiveQ = getLayoutInflater().inflate(R.layout.subjective_question, questionChildLayout, false);
        questionChildLayout.removeView(subjectiveQ);
        questionChildLayout.addView(multipleChoiceQ);
        currentView = multipleChoiceQ;
    }
}

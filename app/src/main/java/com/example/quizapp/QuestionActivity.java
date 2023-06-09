package com.example.quizapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class QuestionActivity extends AppCompatActivity {
    private static int questionFileId;
    private static final String TAG = "QuestionActivity";
    private static final long TIME_MS = 180000L; // 제한 시간(ms)
    private static final int MULTIPLEQ_POINT = 10; // 객관식 문제 배점
    private static final int SUBJECTIVEQ_POINT = 30; // 주관식 문제 배점
    private Questions questions;
    private TextView title;
    private TextView multipleChoiceQuestionText, subjectiveQuestionText;
    private EditText subjectiveAnswerText;
    private Button choiceButton1, choiceButton2, choiceButton3, choiceButton4;
    private Button buttonSubmit;
    private ProgressBar progressBar;
    private TextView textViewTime;
    private int correctCount = 0;
    private int score = 0;

    private ViewGroup questionChildLayout;
    private View subjectiveQ, multipleChoiceQ; // 주관식 뷰, 객관식 뷰
    private View currentView; // 현재 뷰
    private CountDownTimer timer;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // 하단 소프트바 제거
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        uiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        uiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);

        initializeVariables(); // 필드들 초기화

        Intent intent = getIntent();
        String playername = intent.getStringExtra("playername");
        String topic = intent.getStringExtra("topic");
        title.setText(playername);

        switch (topic) {
            case "역사":
                questionFileId = R.raw.questions_1;
                break;
            case "넌센스":
                questionFileId = R.raw.questions_2;
                break;
            case "과학":
                questionFileId = R.raw.questions_3;
                break;
            case "외대":
                questionFileId = R.raw.questions_4;
                break;
        }
        // 문제 모음집 읽어오기
        questions = Questions.readQuizFromFile(getResources(), questionFileId);

        subjectiveAnswerText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            // 주관식 답변 칸이 포커스를 얻으면 힌트 텍스트 삭제, 포커스를 잃으면 힌트 텍스트 보이기
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    ((EditText)view).setHint(""); // 포커스를 얻을 경우
                } else {
                    ((EditText)view).setHint("답변 입력"); // 포커스를 잃을 경우
                }
            }
        });
        subjectiveAnswerText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            // 엔터를 누르면 키보드 숨기기
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
                String text = ((Button)view).getText().toString();
                Questions.QnA qna = questions.current(); // 현재 문제 객체를 가져옴
                // 정답인지 확인
                if (text.equals(qna.getAnswer())) {
                    Log.d(TAG, "correct");
                    Toast.makeText(QuestionActivity.this, "정답입니다!", Toast.LENGTH_SHORT).show();
                    qna.correct();
                    correctCount++;
                    score += MULTIPLEQ_POINT;
                }
                else {
                    Toast.makeText(QuestionActivity.this, "오답입니다", Toast.LENGTH_SHORT).show();
                }

                // 지금까지 못맞춘 문제가 있다면 다음 문제 로드
                if (correctCount < questions.size())
                    loadNextQuestion();
                // 모든 문제의 정답을 맞췄을 경우 점수화면으로 넘어감
                else {
                    Intent myIntent = new Intent(QuestionActivity.this, ShowScoreActivity.class);
                    myIntent.putExtra("playername", playername);
                    myIntent.putExtra("count", correctCount);
                    myIntent.putExtra("score", score);
                    myIntent.putExtra("size", questions.size());
                    myIntent.putExtra("topic", topic);
                    startActivity(myIntent);
                    finish();
                }
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
                Questions.QnA qna = questions.current(); // 현재 문제 객체를 가져옴
                // 정답인지 확인
                if (text.equals(qna.getAnswer())) {
                    Log.d(TAG, "correct");
                    Toast.makeText(QuestionActivity.this, "정답입니다!", Toast.LENGTH_SHORT).show();
                    qna.correct();
                    correctCount++;
                    score += SUBJECTIVEQ_POINT;
                }
                else {
                    Toast.makeText(QuestionActivity.this, "오답입니다", Toast.LENGTH_SHORT).show();
                }

                // 지금까지 못맞춘 문제가 있다면 다음 문제 로드
                if (correctCount < questions.size())
                    loadNextQuestion();
                // 모든 문제의 정답을 맞췄을 경우 점수화면으로 넘어감
                else {
                    Intent myIntent = new Intent(QuestionActivity.this, ShowScoreActivity.class);
                    myIntent.putExtra("playername", playername);
                    myIntent.putExtra("count", correctCount);
                    myIntent.putExtra("score", score);
                    myIntent.putExtra("size", questions.size());
                    myIntent.putExtra("topic", topic);
                    startActivity(myIntent);
                    finish();
                }
            }
        });

        loadNextQuestion(); // 첫 문제 로드

        // 시간 제한 설정
        progressBar.setMax((int) TIME_MS);
        progressBar.setProgress((int) TIME_MS);
        timer = new CountDownTimer(TIME_MS, 100) {
            @Override
            public void onTick(long l) {
                textViewTime.setText(hmsTimeFormatter(l)); // 밀리초를 MM:SS 형식으로 변환
                if (l <= 10000) { // 10초 남았을 경우 빨간색으로 변경
                    textViewTime.setTextColor(getResources().getColor(R.color.red));
                }
                int current = progressBar.getProgress() - 100;
                progressBar.setProgress(current); // progress bar 감소
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(0);
                Intent myIntent = new Intent(QuestionActivity.this, ShowScoreActivity.class);
                myIntent.putExtra("playername", playername);
                myIntent.putExtra("count", correctCount);
                myIntent.putExtra("score", score);
                myIntent.putExtra("size", questions.size());
                myIntent.putExtra("topic", topic);
                startActivity(myIntent);
                finish();
            }
        };
        timer.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
        finish();
        Log.d(TAG, "stopped");
    }

    private void initializeVariables() {
        questionChildLayout = findViewById(R.id.questionChildLayout);
        subjectiveQ = getLayoutInflater().inflate(R.layout.subjective_question, questionChildLayout, false);
        multipleChoiceQ = getLayoutInflater().inflate(R.layout.multiplechoice_question, questionChildLayout, false);

        title = findViewById(R.id.question_title);
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

    // 밀리초를 MM:SS 형식으로 변환
    private static String hmsTimeFormatter(long l) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(l) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(l)),
                TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));
    }

    public void loadNextQuestion() {
        Questions.QnA nextQnA;
        while ((nextQnA = questions.next()).correct); // 지금까지 못 푼 문제를 반환
//        Questions.QnA nextQnA = questions.next();

        // 다음 문제가 객관식이라면
        if (nextQnA instanceof Questions.MultipleChoiceQnA) {
            changeToMultipleChoiceQview();

            multipleChoiceQuestionText.setText("Q. " + nextQnA.getQuestion());
            String[] choices = ((Questions.MultipleChoiceQnA) nextQnA).getChoices();
            Log.d("Questions", Arrays.toString(choices));
            choiceButton1.setText(choices[0]);
            choiceButton2.setText(choices[1]);
            choiceButton3.setText(choices[2]);
            choiceButton4.setText(choices[3]);
        }
        // 다음 문제가 주관식이라면
        else if (nextQnA instanceof Questions.SubjectiveQnA) {
            changeToSubjectiveQview();

            subjectiveQuestionText.setText("Q. " + nextQnA.getQuestion());
            subjectiveAnswerText.setHint("답변 입력");
            subjectiveAnswerText.setText("");
        }
    }

    // 주관식 뷰로 변경
    private void changeToSubjectiveQview() {
        // 현재 뷰가 이미 주관식 뷰라면 건너뜀
        if (currentView != null && currentView == subjectiveQ) return;
        questionChildLayout.removeView(multipleChoiceQ); // 객관식 뷰를 제거
        questionChildLayout.addView(subjectiveQ); // 주관식 뷰를 뷰 그룹에 자식 뷰로 삽입
        currentView = subjectiveQ;
    }

    // 객관식 뷰로 변경
    private void changeToMultipleChoiceQview() {
        // 현재 뷰가 이미 객관식 뷰라면 건너뜀
        if (currentView != null && currentView == multipleChoiceQ) return;
        questionChildLayout.removeView(subjectiveQ); // 주관식 뷰를 제거
        questionChildLayout.addView(multipleChoiceQ); // 객관식 뷰를 뷰 그룹에 자식 뷰로 삽입
        currentView = multipleChoiceQ;
    }
}

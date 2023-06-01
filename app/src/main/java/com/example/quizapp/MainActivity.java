package com.example.quizapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private String playerName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startButton = findViewById(R.id.startbutton);
        EditText playerNameView = findViewById(R.id.entry); // 닉네임 적는 곳

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            // 버튼 클릭 시 닉네임을 추출하고 QuestionActivity 시작
            public void onClick(View view) {
                playerName = playerNameView.getText().toString();
                Log.d(TAG, "player name: " + playerName);
                Intent myIntent = new Intent(MainActivity.this, QuestionActivity.class);
                startActivity(myIntent);
            }
        });

        playerNameView.setOnKeyListener(new View.OnKeyListener() {
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
    }
}

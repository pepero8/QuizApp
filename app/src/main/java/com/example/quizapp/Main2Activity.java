package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main2);

        // 하단 소프트바 제거
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        uiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        uiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);

        Button startButton = findViewById(R.id.gamestartbutton);
        TextView nametext = findViewById(R.id.nametext);
        Intent intent = getIntent();
        String playername = intent.getStringExtra("nametext");
        nametext.setText(playername);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectTopicActivity.class);
                intent.putExtra("playername", playername);
                startActivity(intent);
                finish();
            }
        });
    }

}

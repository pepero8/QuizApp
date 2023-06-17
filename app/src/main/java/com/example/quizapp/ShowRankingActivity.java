package com.example.quizapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowRankingActivity extends AppCompatActivity {
    private static final String TAG_LIST_FRAGMENT = "TAG_LIST_FRAGMENT";
    ScoreListFragment scoreListFragment;
    Button topic1RankingButton, topic2RankingButton, topic3RankingButton, topic4RankingButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_ranking);

        // 하단 소프트바 제거
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        uiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        uiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);

        topic1RankingButton = findViewById(R.id.topic1_ranking);
        topic2RankingButton = findViewById(R.id.topic2_ranking);
        topic3RankingButton = findViewById(R.id.topic3_ranking);
        topic4RankingButton = findViewById(R.id.topic4_ranking);
        ImageView closeButton = findViewById(R.id.close_ranking);

        FragmentManager fm = getSupportFragmentManager();

        if (savedInstanceState == null) {
            // 랭킹 프레임 추가
            FragmentTransaction ft = fm.beginTransaction();
            scoreListFragment = new ScoreListFragment();
            ft.add(R.id.ranking_frame, scoreListFragment, TAG_LIST_FRAGMENT);
            ft.commitNow();
        }
        else {
            scoreListFragment = (ScoreListFragment) fm.findFragmentByTag(TAG_LIST_FRAGMENT);
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String topic = ((Button)view).getText().toString();
                // 데이터베이스에 특정 주제의 점수 리스트를 요청
                DataBase.getScores(new DataBase.Callback() {
                    @Override
                    public void onDataReceived(HashMap<String, Long> players) {
                        List<Player> playersList = new ArrayList<>(players.size());
                        // players 맵 객체로부터 정보를 추출하여 players 객체들을 생성하고 playersList에 추가
                        for (Map.Entry<String, Long> entry : players.entrySet()) {
                            String name = entry.getKey();
                            Long score = entry.getValue();
                            Player player = new Player(name, score);
                            playersList.add(player);
                        }
                        scoreListFragment.setScores(playersList);
                    }
                }, topic);
            }
        };
        topic1RankingButton.setOnClickListener(listener);
        topic2RankingButton.setOnClickListener(listener);
        topic3RankingButton.setOnClickListener(listener);
        topic4RankingButton.setOnClickListener(listener);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

//    private void setColor(View view) {
//        for (Button button : topicButtonList) {
//            Log.d("FUCKING", "color filter set to null");
//            button.getBackground().setColorFilter(null);
//        }

//        topic1RankingButton.getBackground().setColorFilter(getResources().getColor(R.color.purple_200), PorterDuff.Mode.DST);
//        topic2RankingButton.getBackground().setColorFilter(getResources().getColor(R.color.purple_200), PorterDuff.Mode.DST);
//        topic3RankingButton.getBackground().setColorFilter(getResources().getColor(R.color.purple_200), PorterDuff.Mode.DST);
//        topic4RankingButton.getBackground().setColorFilter(getResources().getColor(R.color.purple_200), PorterDuff.Mode.DST);

//        topicButtonList.get(1).getBackground().setColorFilter(getResources().getColor(R.color.purple_200), PorterDuff.Mode.SCREEN);
//        view.getBackground().setColorFilter(getResources().getColor(R.color.purple_200), PorterDuff.Mode.SCREEN);
//        topic1RankingButton.getBackground().setColorFilter(null);
//        topic2RankingButton.getBackground().setColorFilter(null);
//        topic3RankingButton.getBackground().setColorFilter(null);
//        topic4RankingButton.getBackground().setColorFilter(null);
//    }
}

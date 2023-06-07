package com.example.quizapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ranking);

        // 하단 소프트바 제거
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        uiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        uiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);

        FragmentManager fm = getSupportFragmentManager();

        if (savedInstanceState == null) {
            Log.d("FUCK", "savedInstanceState is null");
            FragmentTransaction ft = fm.beginTransaction();
            scoreListFragment = new ScoreListFragment();
            ft.add(R.id.ranking_frame, scoreListFragment, TAG_LIST_FRAGMENT);
            ft.commitNow();
        }
        else {
            scoreListFragment = (ScoreListFragment) fm.findFragmentByTag(TAG_LIST_FRAGMENT);
        }

        DataBase.getScores(new DataBase.Callback() {
            @Override
            public void onDataReceived(HashMap<String, Long> players) {
                List<Player> playersList = new ArrayList<>(players.size());
                for (Map.Entry<String, Long> entry : players.entrySet()) {
                    String name = entry.getKey();
                    long score = entry.getValue();
                    Player player = new Player(name, (int)score);
                    playersList.add(player);
                }

                Log.d("FUCK", "size: " + playersList.size());

                scoreListFragment.setScores(playersList);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}

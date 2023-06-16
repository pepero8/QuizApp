package com.example.quizapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreListFragment extends Fragment {
    private static final int LIMIT = 50; // 상위 LIMIT명의 순위만 표시
    private final ArrayList<Player> scoresList = new ArrayList<>(LIMIT);
    private RecyclerView recyclerView;
    private final ScoreRecyclerViewAdapter scoreAdapter = new ScoreRecyclerViewAdapter(scoresList);

    public ScoreListFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scores_list, container, false);
        recyclerView = view.findViewById(R.id.list);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(scoreAdapter);
    }

    public void setScores(List<Player> players) {
        Log.d("SHIT", "" + players.size());

        scoresList.clear();
        scoreAdapter.notifyDataSetChanged();

        Collections.sort(players);

        for (int i = 0; i < players.size(); i++) {
            if (i >= LIMIT) break;
            scoresList.add(i, players.get(i));
            scoreAdapter.notifyItemInserted(i);
        }

//        for (int i = 0; i < scoresList.size(); i++) {
//            scoresList.remove(i);
//            scoreAdapter.notifyItemRemoved(i);
//        }
//
//        Collections.sort(players);
//
//        for (int i = 0; i < players.size(); i++) {
//            if (i >= LIMIT) break;
//            scoresList.add(i, players.get(i));
//            scoreAdapter.notifyItemChanged(i);
//        }


//        for (Player player : players) {
//            if (!scoresList.contains(player)) {
//                scoresList.add(player);
//                scoreAdapter.notifyItemInserted(scoresList.indexOf(player));
//            }
//        }
    }
}

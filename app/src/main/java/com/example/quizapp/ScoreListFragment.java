package com.example.quizapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ScoreListFragment extends Fragment {
    private ArrayList<Player> scoresList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ScoreRecyclerViewAdapter scoreAdapter = new ScoreRecyclerViewAdapter(scoresList);

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
        for (Player player : players) {
            if (!scoresList.contains(player)) {
                scoresList.add(player);
                scoreAdapter.notifyItemInserted(scoresList.indexOf(player));
            }
        }
    }
}

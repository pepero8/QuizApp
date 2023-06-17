package com.example.quizapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class ScoreRecyclerViewAdapter extends RecyclerView.Adapter<ScoreRecyclerViewAdapter.ViewHolder> {
    private final List<Player> players;

    public ScoreRecyclerViewAdapter(List<Player> players) {
        this.players = players;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_score, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.player = players.get(position); // 등수
        holder.detailsView.setText(players.get(position).getName()); // 닉네임 표시
        holder.scoreView.setText(String.valueOf(players.get(position).getScore())); // 점수 표시
        if (position == 0) {
            holder.detailsView.setTextColor(0xFFFFF064); // 1위면 텍스트를 노란색으로 변경
        }
        else if (position == 1) {
            holder.detailsView.setTextColor(0xFFC12DFF); // 2위면 텍스트를 보라색으로 변경
        }
        else if (position == 2) {
            holder.detailsView.setTextColor(0xFF0064FF); // 3위면 텍스트를 파란색으로 변경
        }
        else {
            holder.detailsView.setTextColor(0xFFdcdcdc); // 나머지 순위는 텍스트를 하얀색으로 변경
        }
        holder.rankNumberView.setText(String.valueOf(position + 1)); // 등수 표시
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View parentView;
        public final TextView detailsView;
        public final TextView scoreView;
        public final TextView rankNumberView;
        public Player player;

        public ViewHolder(View view) {
            super(view);
            parentView = view;
            detailsView = view.findViewById(R.id.list_item_name);
            scoreView = view.findViewById(R.id.list_item_score);
            rankNumberView = view.findViewById(R.id.list_item_rank);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + detailsView.getText() + "'";
        }
    }
}

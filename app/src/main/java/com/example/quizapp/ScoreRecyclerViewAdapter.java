package com.example.quizapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScoreRecyclerViewAdapter extends RecyclerView.Adapter<ScoreRecyclerViewAdapter.ViewHolder> {
    private final List<Player> players;

    public ScoreRecyclerViewAdapter(List<Player> players) {
        this.players = players;
//        players = new ArrayList<>(scoresMap.size());
//
////        HashMap.Entry<String, Integer>[] scoresSet = new HashMap.Entry<>[scoresMap.size()];
////        scoresSet = scoresMap.entrySet().toArray(scoresSet);
//        for (Map.Entry<String, Integer> entry : scoresMap.entrySet()) {
//            Player player = new Player(entry.getKey(), entry.getValue());
//            players.add(player);
//        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_score, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.player = players.get(position);
        holder.detailsView.setText(players.get(position).toString());
        holder.rankNumberView.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View parentView;
        public final TextView detailsView;
        public final TextView rankNumberView;
        public Player player;

        public ViewHolder(View view) {
            super(view);
            parentView = view;
            detailsView = view.findViewById(R.id.list_item_score_details);
            rankNumberView = view.findViewById(R.id.list_item_rank);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + detailsView.getText() + "'";
        }
    }
}

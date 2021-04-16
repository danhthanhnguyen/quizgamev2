package com.thanhnguyen.quizgame;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<String> mDataset;

    // view structure
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView scoretext;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            scoretext = itemView.findViewById(R.id.scoreTextView);
        }
    }

    public RecyclerViewAdapter(ArrayList<String> mDataset) {
        this.mDataset = mDataset;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing layout for activity score view
        View viewCell = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(viewCell);

        return viewHolder;
    }

    // passing element data for view holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.scoretext.setText(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
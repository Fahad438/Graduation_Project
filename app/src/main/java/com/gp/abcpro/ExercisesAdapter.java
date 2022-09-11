package com.gp.abcpro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesAdapter.ExerciseListViewHolder>{
    private Context mCtx;
    private List<Exercise> exercisesList;
    private RecyclerViewClickListener listener;

    public ExercisesAdapter(Context mCtx, List<Exercise> exercisesList, RecyclerViewClickListener listener) {
        this.mCtx = mCtx;
        this.exercisesList = exercisesList;
        this.listener = listener;

    }

    @NonNull
    @Override
    public ExerciseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);
        View view = layoutInflater.inflate(R.layout.exercise_layout, null);
        return new ExerciseListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseListViewHolder holder, int position) {
        Exercise exercise = exercisesList.get(position);
        holder.exerciseName.setText("Exercise "+ exercise.getLessonNum());
    }

    @Override
    public int getItemCount() { return exercisesList.size(); }


    class ExerciseListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView exerciseName;

        public ExerciseListViewHolder(final View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.exName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }
}

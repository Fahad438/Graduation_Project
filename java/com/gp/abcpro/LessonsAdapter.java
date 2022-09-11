package com.gp.abcpro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LessonsAdapter extends RecyclerView.Adapter<LessonsAdapter.LessonsListViewHolder>{
    private Context mCtx;
    private List<Lesson> lessonsList;
    private RecyclerViewClickListener listener;

    public LessonsAdapter(Context mCtx, List<Lesson> lessonsList, RecyclerViewClickListener listener) {
        this.mCtx = mCtx;
        this.lessonsList = lessonsList;
        this.listener = listener;

    }

    @NonNull
    @Override
    public LessonsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);
        View view = layoutInflater.inflate(R.layout.lesson_layout, null);
        return new LessonsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonsListViewHolder holder, int position) {
        Lesson lesson = lessonsList.get(position);
        holder.lessonName.setText("Lesson "+ lesson.getLessonNum());
    }

    @Override
    public int getItemCount() { return lessonsList.size(); }


    class LessonsListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView lessonName;

        public LessonsListViewHolder(final View itemView) {
            super(itemView);
            lessonName = itemView.findViewById(R.id.lessonName);
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

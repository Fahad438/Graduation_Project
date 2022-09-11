package com.gp.abcpro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdminViewQuestionsAdapter extends RecyclerView.Adapter<AdminViewQuestionsAdapter.VQuesViewHolder> {
    private Context mCtx;
    private List<Exercise> quAnswersList;

    public AdminViewQuestionsAdapter(Context mCtx, List<Exercise> quAnswersList) {
        this.mCtx = mCtx;
        this.quAnswersList = quAnswersList;
    }

    @Override
    public int getItemCount() {
        return quAnswersList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull VQuesViewHolder holder, int position) {
        Exercise exercise = quAnswersList.get(position);

        if(exercise.getLessonNum() == 0) {
            holder.lessonNum.setVisibility(View.GONE);
        }
        holder.lessonNum.setText("Lesson: "+exercise.getLessonNum());
        holder.question.setText("Q"+(position+1)+": "+exercise.getQuestion());
        holder.ans1.setText("A. "+exercise.getAns1());
        holder.ans2.setText("B. "+exercise.getAns2());
        holder.ans3.setText("C. "+exercise.getAns3());
        holder.ans4.setText("D. "+exercise.getAns4());
        holder.correct.setText("Correct answer: "+ exercise.getCorrectAns());
    }

    @NonNull
    @Override
    public VQuesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);
        View view = layoutInflater.inflate(R.layout.question_v_layout, null);
        return new VQuesViewHolder(view);
    }


    class VQuesViewHolder extends RecyclerView.ViewHolder {
        TextView lessonNum, question, ans1, ans2, ans3, ans4, correct;

        public VQuesViewHolder(@NonNull View itemView) {
            super(itemView);

            lessonNum = itemView.findViewById(R.id.lessonNum);
            question = itemView.findViewById(R.id.q);
            ans1 = itemView.findViewById(R.id.ans1);
            ans2 = itemView.findViewById(R.id.ans2);
            ans3 = itemView.findViewById(R.id.ans3);
            ans4 = itemView.findViewById(R.id.ans4);
            correct = itemView.findViewById(R.id.correct_answer);
        }
    }
}


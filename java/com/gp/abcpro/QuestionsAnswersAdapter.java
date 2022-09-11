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

public class QuestionsAnswersAdapter extends RecyclerView.Adapter<QuestionsAnswersAdapter.ExerciseViewHolder>{
    private Context mCtx;
    private List<Exercise> quAnswersList;
    public static ArrayList<String> finalUserAnswersList = StartExercise.userAnswersList;

    public QuestionsAnswersAdapter(Context mCtx, List<Exercise> quAnswersList) {
        this.mCtx = mCtx;
        this.quAnswersList = quAnswersList;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);
        View view = layoutInflater.inflate(R.layout.question_layout, null);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = quAnswersList.get(position);

        holder.question.setText(exercise.getQuestion());
        holder.ans1.setText(exercise.getAns1());
        holder.ans2.setText(exercise.getAns2());
        holder.ans3.setText(exercise.getAns3());
        holder.ans4.setText(exercise.getAns4());
    }

    @Override
    public int getItemCount() {
        return quAnswersList.size();
    }

    class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private int selectedId;
        TextView question;
        RadioButton ans1, ans2, ans3, ans4, selected;
        RadioGroup radioGroup;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.q);
            ans1 = itemView.findViewById(R.id.ans1);
            ans2 = itemView.findViewById(R.id.ans2);
            ans3 = itemView.findViewById(R.id.ans3);
            ans4 = itemView.findViewById(R.id.ans4);
            radioGroup = itemView.findViewById(R.id.ansRadGroup);
            selectedId = radioGroup.getCheckedRadioButtonId();
            selected = itemView.findViewById(selectedId);

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    selectedId = radioGroup.getCheckedRadioButtonId();
                    selected = itemView.findViewById(selectedId);
                    if(finalUserAnswersList.size()>0)
                    removePrevSelection();
                    finalUserAnswersList.add((String)selected.getText());
                }
            });
        }

        private void removePrevSelection() {
            Boolean conditionAns1 = finalUserAnswersList.contains(ans1.getText());
            Boolean conditionAns2 = finalUserAnswersList.contains(ans2.getText());
            Boolean conditionAns3 = finalUserAnswersList.contains(ans3.getText());

            if(conditionAns1) {
                finalUserAnswersList.remove(ans1.getText());
            } else if (conditionAns2) {
                finalUserAnswersList.remove(ans2.getText());
            } else if (conditionAns3) {
                finalUserAnswersList.remove(ans3.getText());
            } else {
                finalUserAnswersList.remove(ans4.getText());
            }
        }
    }
}


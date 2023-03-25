package com.cookandroid.subdietapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.subdietapp.R;
import com.cookandroid.subdietapp.exercise.ExerciseSearchEditActivity;
import com.cookandroid.subdietapp.model.exercise.Exercise;
import com.cookandroid.subdietapp.model.exercise.ExerciseRecord;


import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {

    Context context;
    ArrayList<ExerciseRecord> exercisesList;

    public ExerciseAdapter(Context context, ArrayList<ExerciseRecord> exercisesList) {
        this.context = context;
        this.exercisesList = exercisesList;
    }

    @NonNull
    @Override
    public ExerciseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exercise_row, parent, false);
        return new ExerciseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseAdapter.ViewHolder holder, int position) {

        ExerciseRecord exerciseRecord = exercisesList.get(position);

        holder.txtName.setText(exerciseRecord.getExerciseName());
        holder.txtTime.setText(String.valueOf((int) exerciseRecord.getExerciseTime()));

            //소숫점이 있는지 확인
            //유저가 직접 입력과 테이블 데이터 구분하기위해
        if(exerciseRecord.getTotalKcalBurn() %1 !=0 ) {
            //있으면 소수점 표현
            holder.txtCal.setText(String.format("%.2f", exerciseRecord.getTotalKcalBurn()));
        }else {
            //없으면 생략
            holder.txtCal.setText(String.format("%.0f", exerciseRecord.getTotalKcalBurn()));
        }


    }

    @Override
    public int getItemCount() {
        return exercisesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtName, txtTime, txtCal;
            CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtCal = itemView.findViewById(R.id.txtCal);
            cardView=itemView.findViewById(R.id.cardView);

            //오늘 운동한 리스트중 하나
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = getAdapterPosition();
                    ExerciseRecord exerciseRecord = exercisesList.get(index);
                    int exerciseId = exerciseRecord.getId();

                    String exerciseName = txtName.getText().toString();
                    int exerciseTime = Integer.parseInt(txtTime.getText().toString());
                    double totalKcal = Double.parseDouble(txtCal.getText().toString());


                    Intent intent = new Intent(context, ExerciseSearchEditActivity.class);
                    intent.putExtra("exerciseId", exerciseId);
                    intent.putExtra("exerciseName",exerciseName);
                    intent.putExtra("exerciseTime",exerciseTime);
                    intent.putExtra("totalKcal",totalKcal);
                    context.startActivity(intent);
                }
            });


        }
    }


}

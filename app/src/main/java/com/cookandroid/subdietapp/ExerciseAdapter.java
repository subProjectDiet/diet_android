package com.cookandroid.subdietapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.subdietapp.exercise.ExerciseSearchEditActivity;
import com.cookandroid.subdietapp.model.Exercise;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {

    Context context;
    ArrayList<Exercise> exercisesList = new ArrayList<>();

    public ExerciseAdapter(Context context, ArrayList<Exercise> exercisesList) {
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

        Exercise exercise = exercisesList.get(position);

        holder.txtName.setText(exercise.getExercise());
         holder.txtTime.setText(exercise.getExerciseTime());
        holder.txtCal.setText((int) exercise.getTotalKcalBurn());

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

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = getAdapterPosition();
                    Exercise exercise = exercisesList.get(index);
                    int exerciseId = exercise.getId();


                    Intent intent = new Intent(context, ExerciseSearchEditActivity.class);
                    intent.putExtra("exerciseId", exerciseId);
                    context.startActivity(intent);
                }
            });


        }
    }


}

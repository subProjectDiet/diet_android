package com.cookandroid.subdietapp;

import static android.content.Context.MODE_PRIVATE;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Date;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.subdietapp.api.ExerciseApi;
import com.cookandroid.subdietapp.api.NetworkClient;

import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.exercise.ExerciseSearchAddActivity;
import com.cookandroid.subdietapp.model.Exercise;
import com.cookandroid.subdietapp.model.ExerciseRes;


import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ExerciseSearchAdapter extends RecyclerView.Adapter<ExerciseSearchAdapter.ViewHolder> {

    Context context;
    ArrayList<Exercise> exercisesList;
    private int exerciseId;

    private Context mContext;
    private SharedPreferences mSharedPreferences;


    public ExerciseSearchAdapter(Context context, ArrayList<Exercise> exercisesList) {
        this.context = context;
        this.exercisesList = exercisesList;
    }

    @NonNull
    @Override
    public ExerciseSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exercise_search_row, parent, false);
        return new ExerciseSearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseSearchAdapter.ViewHolder holder, int position) {
        Exercise exercise = exercisesList.get(position);
        holder.txtName.setText(exercise.getExercise());
    }




    @Override
    public int getItemCount() {
        return exercisesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            cardView=itemView.findViewById(R.id.cardView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = getAdapterPosition();
                    Exercise exercise = exercisesList.get(index);
                    exerciseId = exercise.getId();

                    searchNetworkData();

                }
            });



        }
    }

    // TODO: 날짜를 포함해서 서버로 요청을 못하는중
    private void searchNetworkData() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(date);

        Log.i("날짜",dateString+"포맷"+format+"데이트"+date);


        Retrofit retrofit = NetworkClient.getRetrofitClient(context);

        ExerciseApi api = retrofit.create(ExerciseApi.class);

        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE);
        String accessToken = "Bearer " + mSharedPreferences.getString(Config.ACCESS_TOKEN, "");

        Log.i(TAG,"토큰확인"+accessToken);
        Call<Exercise> call = api.searchExerciseSelect(accessToken,exerciseId,dateString);
        Log.i(TAG,"콜확인"+call);
        call.enqueue(new Callback<Exercise>() {
            @Override
            public void onResponse(Call<Exercise> call, Response<Exercise> response) {
                if (response.isSuccessful()){
                    Log.i(TAG,"주소"+response);

                    String exercise = response.body().getExercise();
                    double totalKcalBurn = response.body().getTotalKcalBurn();


                    Intent intent = new Intent(context, ExerciseSearchAddActivity.class);
                    intent.putExtra("exercise",exercise);
                    intent.putExtra("totalKcalBurn",totalKcalBurn);
                    Log.i(TAG,"가저갈께요"+exercise+"칼로리"+totalKcalBurn);
                    context.startActivity(intent);

                }
            }

            @Override
            public void onFailure(Call<Exercise> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });


}


    }
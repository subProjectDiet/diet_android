package com.cookandroid.subdietapp.adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.subdietapp.R;
import com.cookandroid.subdietapp.api.ExerciseApi;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.exercise.ExerciseSearchAddActivity;
import com.cookandroid.subdietapp.exercise.SelectedExerciseSearchActivity;
import com.cookandroid.subdietapp.model.exercise.Exercise;
import com.cookandroid.subdietapp.model.exercise.ExerciseRes;

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

    private double totalKcalBurn;
    private String exerciseName;



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

                    //검색한 내용 바로 추가버튼으로 연결시켜주기
                    searchNetworkData();

                }
            });



        }
    }

    // 검색한 결과에서 클릭한 운동의 데이터를 가지고오는 api
    private void searchNetworkData() {


        String date = ((SelectedExerciseSearchActivity)SelectedExerciseSearchActivity.ExerContext).date;
        Log.i(TAG,"가저왔다날짜"+date);




        Retrofit retrofit = NetworkClient.getRetrofitClient(context);

        ExerciseApi api = retrofit.create(ExerciseApi.class);

        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE);
        String accessToken = "Bearer " + mSharedPreferences.getString(Config.ACCESS_TOKEN, "");

        Log.i(TAG,"토큰확인"+accessToken);
        Call<ExerciseRes> call = api.searchExerciseSelect(accessToken,exerciseId,date);
        Log.i(TAG,"콜확인"+call);
        call.enqueue(new Callback<ExerciseRes>() {
            @Override
            public void onResponse(Call<ExerciseRes> call, Response<ExerciseRes> response) {
                if (response.isSuccessful()){
                    Log.i(TAG,"주소"+response.body());


                    exerciseName = response.body().getItems().get(0).getExercise();
                    totalKcalBurn = response.body().getItems().get(0).getTotalKcalBurn();
                    int id = response.body().getItems().get(0).getId();


                    Intent intent = new Intent(context, ExerciseSearchAddActivity.class);
                    intent.putExtra("exercise",exerciseName);
                    intent.putExtra("totalKcalBurn",totalKcalBurn);
                    intent.putExtra("id",id);
                    intent.putExtra("date",date);
                    Log.i(TAG,"가저갈께요"+exerciseName+"칼로리"+totalKcalBurn+"id"+id);
                    context.startActivity(intent);


                }
            }

            @Override
            public void onFailure(Call<ExerciseRes> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });


}


    }
package com.cookandroid.subdietapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.subdietapp.FoodEditActivity;
import com.cookandroid.subdietapp.R;
import com.cookandroid.subdietapp.food.SelectedDinnerFoodActivity;
import com.cookandroid.subdietapp.model.food.Food;

import java.util.ArrayList;

public class FoodDinnerAdapter extends RecyclerView.Adapter<FoodDinnerAdapter.ViewHolder>{

    Context context;
    ArrayList<Food> foodList;


    public FoodDinnerAdapter(Context context, ArrayList<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodDinnerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // xml 파일을 연결하는 작업
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_record_row, parent, false);
        return new FoodDinnerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodDinnerAdapter.ViewHolder holder, int position) {


        Food food  = foodList.get(position);

        holder.txtFoodName.setText(food.getFoodName() + " (" + food.getGram() + "g)");
        holder.txtData.setText("탄수화물 "+food.getCarbs() + "g" + " 단백질 "+food.getProtein() + "g" + " 지방 "+food.getFat() + "g" );
        holder.txtKcal.setText(food.getKcal() + " kcal");



    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView txtFoodName, txtData, txtKcal;
        CardView cardView;





        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFoodName= itemView.findViewById(R.id.txtFoodName);
            txtData = itemView.findViewById(R.id.txtData);
            txtKcal = itemView.findViewById(R.id.txtKcal);
            cardView = itemView.findViewById(R.id.cardView);



            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    // 액티비티에서 mealtime 과 date 데이터를 받아온다
                    int mealtime = ((SelectedDinnerFoodActivity)SelectedDinnerFoodActivity.dinnerContext).mealtime;
                    String date = ((SelectedDinnerFoodActivity)SelectedDinnerFoodActivity.dinnerContext).date;


                    int index = getAdapterPosition();

                    Food food = foodList.get(index);
                    int foodRecordId = food.getId();


                    Log.i("GETDATA", "foodRecordId : " + foodRecordId + " date : "+date + " mealtime : " + mealtime);
                    Intent intent = new Intent(context, FoodEditActivity.class);
                    intent.putExtra("foodRecordId", foodRecordId+"");
                    intent.putExtra("food", food);
                    intent.putExtra("date", date);
                    intent.putExtra("mealtime", mealtime + "");

                    context.startActivity(intent);


                }
            });






        }
    }
}

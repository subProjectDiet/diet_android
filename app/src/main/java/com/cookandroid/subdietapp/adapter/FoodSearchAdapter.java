package com.cookandroid.subdietapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.subdietapp.R;
import com.cookandroid.subdietapp.model.food.Food;

import java.util.ArrayList;

public class FoodSearchAdapter extends RecyclerView.Adapter<FoodSearchAdapter.ViewHolder>{

    Context context;
    ArrayList<Food> foodList;


    public FoodSearchAdapter(Context context, ArrayList<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }


    @NonNull
    @Override
    public FoodSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // xml 파일을 연결하는 작업
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_search_row, parent, false);
        return new FoodSearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodSearchAdapter.ViewHolder holder, int position) {

        Food food  = foodList.get(position);

        holder.txtFoodName.setText(food.getFoodName());
        holder.txtKcal.setText(food.getKcal() + " kcal");


    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView txtFoodName, txtKcal;
        CardView cardView;





        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFoodName= itemView.findViewById(R.id.txtFoodName);
            txtKcal = itemView.findViewById(R.id.txtKcal);
            cardView = itemView.findViewById(R.id.cardView);



            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int index = getAdapterPosition();


                }
            });






        }
    }
}

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

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder>{

    Context context;
    ArrayList<Food> foodList;


    public FoodAdapter(Context context, ArrayList<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // xml 파일을 연결하는 작업
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_record_row, parent, false);
        return new FoodAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.ViewHolder holder, int position) {


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

                    int index = getAdapterPosition();


                }
            });






        }
    }
}

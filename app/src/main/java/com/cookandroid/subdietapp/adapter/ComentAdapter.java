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
import com.cookandroid.subdietapp.model.posting.Coment;

import java.util.ArrayList;

public class ComentAdapter extends RecyclerView.Adapter<ComentAdapter.ViewHolder>{

    Context context;
    ArrayList<Coment> comentList;

    public ComentAdapter(Context context, ArrayList<Coment> comentList) {
        this.context = context;
        this.comentList = comentList;
    }

    @NonNull
    @Override
    public ComentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // xml 파일을 연결하는 작업
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coment_row, parent, false);
        return new ComentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComentAdapter.ViewHolder holder, int position) {

        Coment coment = comentList.get(position);


        holder.txtNickName.setText(coment.getNickName());
        holder.txtContent.setText(coment.getContent());

//        2023-03-08T14:55:10
        holder.txtDate.setText( coment.getCreatedAt().substring(0, 9+1) + " " + coment.getCreatedAt().substring(11, 18+1) );


    }

    @Override
    public int getItemCount() {
        return comentList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        CardView cardView;

        TextView txtNickName, txtContent, txtDate;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNickName = itemView.findViewById(R.id.txtNickName);
            txtContent = itemView.findViewById(R.id.txtContent);
            txtDate = itemView.findViewById(R.id.txtDate);
            cardView = itemView.findViewById(R.id.cardView);



        }
    }
}
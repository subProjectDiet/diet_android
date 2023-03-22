package com.cookandroid.subdietapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cookandroid.subdietapp.R;
import com.cookandroid.subdietapp.SelectedPostingActivity;
import com.cookandroid.subdietapp.model.posting.Posting;

import java.util.ArrayList;

public class PostingAdapter extends RecyclerView.Adapter<PostingAdapter.ViewHolder>{

    Context context;
    ArrayList<Posting> postingList;

    public PostingAdapter(Context context, ArrayList<Posting> postingList) {
        this.context = context;
        this.postingList = postingList;
    }

    @NonNull
    @Override
    public PostingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // xml 파일을 연결하는 작업
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.posting_row, parent, false);
        return new PostingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostingAdapter.ViewHolder holder, int position) {

        Posting posting = postingList.get(position);


        holder.txtNickName.setText(posting.getNickName());
        holder.txtContent.setText(posting.getContent());
        holder.txtLike.setText(posting.getLikeCnt()+"");

//        2023-03-08T14:55:10
        holder.txtDate.setText( posting.getCreatedAt().substring(0, 9+1) + " " + posting.getCreatedAt().substring(11, 18+1) );

        Glide.with(context).load(posting.getImgurl())
                .placeholder(R.drawable.outline_insert_photo_24)
                .into(holder.imgPhoto);

        if (posting.getIsLike() == 1){
            holder.imgLike.setImageResource(R.drawable.baseline_favorite_24);

        }else{
            holder.imgLike.setImageResource(R.drawable.baseline_favorite_border_24);

        }


    }

    @Override
    public int getItemCount() {
        return postingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ImageView imgPhoto, imgLike;
        TextView txtLike, txtNickName, txtContent, txtDate;
        CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgLike= itemView.findViewById(R.id.imgLike);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            txtLike = itemView.findViewById(R.id.txtLike);
            txtNickName = itemView.findViewById(R.id.txtNickName);
            txtContent = itemView.findViewById(R.id.txtContent);
            txtDate = itemView.findViewById(R.id.txtDate);
            cardView = itemView.findViewById(R.id.cardView);


            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int index = getAdapterPosition();
                    Posting posting = postingList.get(index);

                    int postingId = posting.getId();

                    Intent intent = new Intent(context, SelectedPostingActivity.class);
                    intent.putExtra("postingId", postingId);
                    context.startActivity(intent);


                }
            });


        }
    }
}


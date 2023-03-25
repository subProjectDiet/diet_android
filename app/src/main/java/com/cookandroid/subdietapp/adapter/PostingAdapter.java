package com.cookandroid.subdietapp.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cookandroid.subdietapp.R;
import com.cookandroid.subdietapp.SelectedPostingActivity;
import com.cookandroid.subdietapp.api.LikeApi;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.model.posting.Posting;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PostingAdapter extends RecyclerView.Adapter<PostingAdapter.ViewHolder>{

    int postingId;

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
        holder.txtLike.setText(posting.getLikeCnt() +"");

//        2023-03-08T14:55:10
        holder.txtDate.setText( posting.getCreatedAt().substring(0, 9+1) + " " + posting.getCreatedAt().substring(11, 18+1) );

        Glide.with(context).load(posting.getImgurl().replace("http","https"))
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


            SharedPreferences sp = context.getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
            String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int index = getAdapterPosition();
                    Posting posting = postingList.get(index);

                    postingId = posting.getId();

                    Log.i("POSTING_ID", postingId+"");
                    Intent intent = new Intent(context, SelectedPostingActivity.class);
                    intent.putExtra("postingId", postingId);
                    intent.putExtra("userId", posting.getUserId());

                    context.startActivity(intent);


                }
            });

            imgLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int index = getAdapterPosition();
                    Posting posting = postingList.get(index);



                    // 2. 해당행의 좋아요가 이미 좋아요인지 아닌지 파악
                    if (posting.getIsLike() == 0) {
                        // 3. 해당 좋아요에 맞는 좋아요 API를 호출
                        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
                        LikeApi api = retrofit.create(LikeApi.class);



                        Call<Void> call = api.postLike(accessToken, posting.getId());

                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    // 4. 화면에 결과를 표시
                                    posting.setIsLike(1);
                                    posting.setLikeCnt(posting.getLikeCnt() + 1);
                                    notifyDataSetChanged();

//                                    adapter2.notifyDataSetChanged();

                                } else {
                                    Toast.makeText(context, "좋아요 처리에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(context, "서버와 통신이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        // 3. 좋아요 해제 API를 호출
                        // 3. 해당 좋아요에 맞는 좋아요 API를 호출
                        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
                        LikeApi api = retrofit.create(LikeApi.class);

                        Call<Void> call = api.deleteLike(accessToken, posting.getId());

                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    // 4. 화면에 결과를 표시
                                    posting.setIsLike(0);
                                    posting.setLikeCnt(posting.getLikeCnt() - 1);
//                                    adapter2.notifyDataSetChanged();

                                    notifyDataSetChanged();

                                } else {
                                    Toast.makeText(context, "좋아요 해제에 실패했습니다.", Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(context, "서버와 통신이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
        });




        }
    }
}


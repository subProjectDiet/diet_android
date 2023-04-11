package com.cookandroid.subdietapp.food;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.subdietapp.AddKcalDirectActivity;
import com.cookandroid.subdietapp.R;
import com.cookandroid.subdietapp.SearchFoodActivity;
import com.cookandroid.subdietapp.adapter.FoodDinnerAdapter;
import com.cookandroid.subdietapp.api.FoodApi;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.api.VisionNetworkClient;
import com.cookandroid.subdietapp.api.VsionApi;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.model.food.Food;
import com.cookandroid.subdietapp.model.food.FoodRes;
import com.cookandroid.subdietapp.model.food.TotalKcal;
import com.cookandroid.subdietapp.model.food.TotalKcalRes;
import com.cookandroid.subdietapp.model.food.VisionRes;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectedDinnerFoodActivity extends AppCompatActivity {

    ImageView imgBack, imgKcal;
    Button btnAdd, btnSearch, btnEnd;
    TextView txtTotalKcal;
    EditText editSearch;
    RecyclerView recyclerView;
    FoodDinnerAdapter foodAdapter;
    ArrayList<Food> foodList = new ArrayList<>();
    public String date;

    TotalKcal totalKcal = new TotalKcal();
    private ProgressDialog dialog;

    // 페이징 처리를 위한 변수
    int count;
    int offset = 0;
    int limit = 25;

    public int mealtime = 2;

    public static Context dinnerContext;

    private boolean isloading = false;

    private File photoFile;
    private String vkcal;
    private String vgram;
    private String vprotein;
    private String vfat;
    private String vcarb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_dinner_food);
        btnAdd = findViewById(R.id.btnAdd);
        btnSearch = findViewById(R.id.btnSearch);
        btnEnd = findViewById(R.id.btnEnd);
        imgKcal=findViewById(R.id.imgKcal);
        imgBack = findViewById(R.id.imgBack);

        editSearch = findViewById(R.id.editSearch);

        txtTotalKcal = findViewById(R.id.txtTotalKcal);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(SelectedDinnerFoodActivity.this));

        recyclerView.addItemDecoration(new DividerItemDecoration(SelectedDinnerFoodActivity.this, 1));

        // 어댑터로 데이터를 보내기 위한 변수
        dinnerContext = this;

        // 다이어리 페이지에서 요일 정보 받아오기
        // 2023-03-26
        date = getIntent().getStringExtra("date");

        Log.i("DATETEST", date+"");

        // 1. 칼로리 직접 추가로 이동
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectedDinnerFoodActivity.this, AddKcalDirectActivity.class);
                intent.putExtra("date", date);
                intent.putExtra("mealtime", mealtime + "");
                startActivity(intent);
            }
        });

        // 2. 검색으로 칼로리 추가
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword = editSearch.getText().toString().trim();

                if (keyword.isEmpty()){
                    return;
                }


                Intent intent = new Intent(SelectedDinnerFoodActivity.this, SearchFoodActivity.class);
                intent.putExtra("keyword", keyword);
                intent.putExtra("mealtime", mealtime + "");
                intent.putExtra("date", date);

                startActivity(intent);

            }
        });

        // 사진
        imgKcal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog();

            }
        });


        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SelectedDinnerFoodActivity.this);
        builder.setTitle(R.string.alert_title);
        builder.setItems(R.array.alert_photo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    // todo : 사진찍는 코드 실행
                    camera();

                } else if (i == 1) {
                    // todo : 앨범에서 사진 가져오는 코드 실행
                    album();
                }
            }
        });

        AlertDialog alert = builder.create();
        alert.show();


    }

    private void album() {
        if(checkPermission()){
            displayFileChoose();
        }else{
            requestPermission();
        }

    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(SelectedDinnerFoodActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(result == PackageManager.PERMISSION_DENIED){
            return false;
        }else{
            return true;
        }

    }

    private void requestPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(SelectedDinnerFoodActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(SelectedDinnerFoodActivity.this, "권한 수락이 필요합니다.",
                    Toast.LENGTH_SHORT).show();
        }else{
            ActivityCompat.requestPermissions(SelectedDinnerFoodActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 500);
        }

    }

    private void displayFileChoose() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "SELECT IMAGE"), 300);

    }

    private void camera() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                SelectedDinnerFoodActivity.this, android.Manifest.permission.CAMERA);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SelectedDinnerFoodActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    1000);
            Toast.makeText(SelectedDinnerFoodActivity.this, "카메라 권한이 필요합니다.",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (i.resolveActivity(SelectedDinnerFoodActivity.this.getPackageManager()) != null) {

                // 사진의 파일명을 만들기
                String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                photoFile = getPhotoFile(fileName);

                Uri fileProvider = FileProvider.getUriForFile(SelectedDinnerFoodActivity.this,
                        "com.cookandroid.subdietapp.fileprovider", photoFile);
                i.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
                startActivityForResult(i, 100);

            } else {
                Toast.makeText(SelectedDinnerFoodActivity.this, "카메라 앱이 필요합니다.",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    private File getPhotoFile(String fileName) {
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try{
            return File.createTempFile(fileName, ".jpg", storageDirectory);
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        if(requestCode == 100 && resultCode == RESULT_OK){

            Bitmap photo = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

            ExifInterface exif = null;
            try {
                exif = new ExifInterface(photoFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            photo = rotateBitmap(photo, orientation);

            // 압축시킨다. 해상도 낮춰서
            OutputStream os;
            try {
                os = new FileOutputStream(photoFile);
                photo.compress(Bitmap.CompressFormat.JPEG, 50, os);
                os.flush();
                os.close();
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
            }

            photo = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

            visionNetwokData();


            // 네트워크로 데이터 보낸다.



        }else if(requestCode == 300 && resultCode == RESULT_OK && data != null &&
                data.getData() != null){

            Uri albumUri = data.getData( );
            String fileName = getFileName( albumUri );
            try {

                ParcelFileDescriptor parcelFileDescriptor = getContentResolver( ).openFileDescriptor( albumUri, "r" );
                if ( parcelFileDescriptor == null ) return;
                FileInputStream inputStream = new FileInputStream( parcelFileDescriptor.getFileDescriptor( ) );
                photoFile = new File( this.getCacheDir( ), fileName );
                FileOutputStream outputStream = new FileOutputStream( photoFile );
                IOUtils.copy( inputStream, outputStream );

//                //임시파일 생성
//                File file = createImgCacheFile( );
//                String cacheFilePath = file.getAbsolutePath( );


                // 압축시킨다. 해상도 낮춰서
                Bitmap photo = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                OutputStream os;
                try {
                    os = new FileOutputStream(photoFile);
                    photo.compress(Bitmap.CompressFormat.JPEG, 60, os);
                    os.flush();
                    os.close();
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
                }

                visionNetwokData();

//                imageView.setImageBitmap( getBitmapAlbum( imageView, albumUri ) );

            } catch ( Exception e ) {
                e.printStackTrace( );
            }

            // 네트워크로 보낸다.
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void visionNetwokData() {

        showProgress("영양정보를 불러오는 중입니다");
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), photoFile);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", photoFile.getName(), requestBody);

        Retrofit retrofit = VisionNetworkClient.getRetrofitClient(SelectedDinnerFoodActivity.this);
        VsionApi visionApi = retrofit.create(VsionApi.class);

        Call<VisionRes> call = visionApi.visionResult(photo);
        call.enqueue(new Callback<VisionRes>() {
            @Override
            public void onResponse(Call<VisionRes> call, Response<VisionRes> response) {



                if(response.isSuccessful()){
                    dismissProgress();

                    for(int i = 0; i < response.body().getItems().size(); i++) {
                        vkcal =response.body().getItems().get(i).getKcal();
                        Log.i(TAG, "확인쓰" + vkcal);
                        if(vkcal != null){
                            break;
                        }
                    }
                    for(int i = 0; i < response.body().getItems().size(); i++) {

                        vcarb = response.body().getItems().get(i).get탄수화물();
                        Log.i(TAG, "확인쓰" + vcarb);
                        if(vcarb != null){
                            break;
                        }

                    }
                    for(int i = 0; i < response.body().getItems().size(); i++) {

                        vprotein = response.body().getItems().get(i).get단백질();
                        Log.i(TAG, "확인쓰" + vprotein);
                        if(vprotein != null){
                            break;
                        }

                    }
                    for(int i = 0; i < response.body().getItems().size(); i++) {
                        vfat = response.body().getItems().get(i).get지방();
                        Log.i(TAG, "확인쓰" + vfat);
                        if(vfat != null){
                            break;
                        }

                    }
                    for(int i = 0; i < response.body().getItems().size(); i++) {

                        vgram = response.body().getItems().get(i).getGram();
                        Log.i(TAG, "확인쓰" + vgram);
                        if(vgram != null){
                            break;
                        }
                    }




                    Intent intent = new Intent(SelectedDinnerFoodActivity.this, AddKcalDirectActivity.class);
                    intent.putExtra("date", date);
                    intent.putExtra("mealtime", mealtime + "");
                    intent.putExtra("carbs", vcarb);
                    intent.putExtra("gram", vgram);
                    intent.putExtra("protein", vprotein);
                    intent.putExtra("fat", vfat);
                    intent.putExtra("kcal", vkcal);
                    intent.putExtra("camera",1);


                    startActivity(intent);



                }
            }

            @Override
            public void onFailure(Call<VisionRes> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });

    }

    private void showProgress(String message) {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(message);
        dialog.show();

    }

    private void dismissProgress() {

        dialog.dismiss();
    }

    private String getFileName(Uri albumUri) {
        Cursor cursor = getContentResolver( ).query( albumUri, null, null, null, null );
        try {
            if ( cursor == null ) return null;
            cursor.moveToFirst( );
            @SuppressLint("Range") String fileName = cursor.getString( cursor.getColumnIndex( OpenableColumns.DISPLAY_NAME ) );
            cursor.close( );
            return fileName;

        } catch ( Exception e ) {
            e.printStackTrace( );
            cursor.close( );
            return null;
        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        getNetworkData();
        getKcalNetworkData();
    }

    // 점심에 섭취한 칼로리 총합을 가져온다
    private void getKcalNetworkData() {

        Log.i("DATETEST", date);

        Retrofit retrofit = NetworkClient.getRetrofitClient(this);

        FoodApi api = retrofit.create(FoodApi.class);


        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<TotalKcalRes> call = api.getTotalDinnerKcal(accessToken, date);

        call.enqueue(new Callback<TotalKcalRes>() {

            @Override
            public void onResponse(Call<TotalKcalRes> call, Response<TotalKcalRes> response) {


                if (response.isSuccessful()) {


                    // 사용자가 너무빨리 뒤로가기를 눌렀을때 에러가 발생한다.
                    // 이를 방지하기 위해 try catch문을 사용한다.


                    try {

//                        response.body().getTotalKcal().getTotalKcal();

                        txtTotalKcal.setText("("+response.body().getTotalKcal().getTotalKcal() + "kcal)");

                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }
            }

            @Override
            public void onFailure(Call<TotalKcalRes> call, Throwable t) {
                Log.i("다이어리", t.getMessage());
            }
        });






    }

    private void getNetworkData() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(SelectedDinnerFoodActivity.this);

        FoodApi api = retrofit.create(FoodApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        offset = 0;
        count = 0;

        Call<FoodRes> call = api.getDinnerKcal(accessToken, date , offset, limit);
        call.enqueue(new Callback<FoodRes>() {

            @Override
            public void onResponse(Call<FoodRes> call, Response<FoodRes> response) {

                // getNetworkData 함수는, 항상 처음에 데이터를 가져오는 동작이므로
                // 초기화 코드가 필요.
                foodList.clear();


                if (response.isSuccessful()) {

                    count = response.body().getCount();

                    offset = offset + count;

                    foodList.addAll(response.body().getItems());

                    foodAdapter = new FoodDinnerAdapter(SelectedDinnerFoodActivity.this, foodList);
                    recyclerView.setAdapter(foodAdapter);


                } else {


                }

            }

            @Override
            public void onFailure(Call<FoodRes> call, Throwable t) {

            }
        });


    }









}
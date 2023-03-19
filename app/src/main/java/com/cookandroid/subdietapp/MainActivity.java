package com.cookandroid.subdietapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigationView;

    // 각 프레그먼트를 멤버변수로 만든다.
    public Fragment firstFragment;
    Fragment secondFragment;
    Fragment thirdFragment;
    Fragment forthFragment;


    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//        startActivity(intent);


        navigationView = findViewById(R.id.bottomNavigationView);

        firstFragment = new FirstFragment();
        secondFragment = new SecondFragment();
        thirdFragment = new ThirdFragment();
        forthFragment = new forthFragment();


        // 탭바를 누르면 동작하는 코드
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                fragment = null;

                if (itemId == R.id.firstFragment){
                    fragment = firstFragment;

                } else if (itemId == R.id.secondFragment){
                    fragment = secondFragment;
                } else if (itemId == R.id.thirdFragment) {
                    fragment = thirdFragment;
                } else if (itemId == R.id.forthFragment) {
                    fragment = forthFragment;
                }
                return loadFragment(fragment);
            }
        });


    }

    private boolean loadFragment(Fragment fragment){
        if (fragment != null){
            // 화면 바뀌게 하는 코드
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
            return true;
        } else {
            return false;
        }
    }
}
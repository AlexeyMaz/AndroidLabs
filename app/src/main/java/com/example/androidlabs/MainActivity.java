package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onMenuButtonClick(View view) {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        drawerLayout.openDrawer(GravityCompat.START);
    }
}
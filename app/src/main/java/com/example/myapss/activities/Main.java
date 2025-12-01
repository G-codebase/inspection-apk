package com.example.myapss.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapss.R;
import com.example.myapss.utils.PreferenceManager;

public class Main extends AppCompatActivity {

    private Button btnAddInspection;
    private Button btnLogout;
    private TextView userEmailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        btnAddInspection = findViewById(R.id.btn_add_inspection);
        btnLogout = findViewById(R.id.btn_logout);
        userEmailText = findViewById(R.id.user_email_text);

        String userEmail = PreferenceManager.getUserEmail(this);
        userEmailText.setText("User: " + userEmail);
    }

    private void setupListeners() {
        btnAddInspection.setOnClickListener(v -> {
            Intent intent = new Intent(Main.this, Page1.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> performLogout());
    }

    private void performLogout() {
        PreferenceManager.logout(this);
        Intent intent = new Intent(Main.this, Login.class);
        startActivity(intent);
        finish();
    }

//    boolean isFirstLaunch = true;
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        if (isFirstLaunch) {
//            isFirstLaunch = false;
//            startActivity(new Intent(Main.this, Page1.class));
//            finish();
//        }
//    }
}
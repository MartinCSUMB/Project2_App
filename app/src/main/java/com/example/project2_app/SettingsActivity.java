package com.example.project2_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.backArrow).setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close SettingsActivity
        });


        findViewById(R.id.changeStoreButton).setOnClickListener(v -> {
            Intent intent = StoreActivity.storeIntentFactory(getApplicationContext());
            startActivity(intent);
        });

        findViewById(R.id.changeUsernameButton).setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, ChangeUsernameActivity.class);
            startActivity(intent);
        });


        findViewById(R.id.changePasswordButton).setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        });
    }
}
package com.example.project2_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.project2_app.database.InventoryManagementRepository;
import com.example.project2_app.database.entities.User;
import com.example.project2_app.databinding.ActivityRegistrationBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegistrationActivity extends AppCompatActivity {

    private String mUsername = "";
    private String mPassword = "";
    private String mConfirmPassword = "";

    private ActivityRegistrationBinding binding;

    private InventoryManagementRepository repository;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.d("RegistrationActivity", "RegistrationActivity loaded");

        executor.execute(() -> {
            repository = new InventoryManagementRepository(getApplication());
            runOnUiThread(() -> {
                Log.d("RegistrationActivity", "Repository initialized successfully");
                // Continue setup if needed
            });
        });
        binding.backArrow.setOnClickListener(v -> onBackPressed());
        binding.returnToAdminMenuFromRegistrationButton.setOnClickListener(v -> {
            Intent intent = MainActivity.mainActivityIntentFactory(getApplicationContext());
            startActivity(intent);
        });

        binding.enterRegisterButton.setOnClickListener(v -> {
            mUsername = binding.usernameEditText.getText().toString().trim();
            mPassword = binding.passwordEditText.getText().toString().trim();
            mConfirmPassword = binding.confirmPasswordEditText.getText().toString().trim();

            if (mUsername.isEmpty() || mPassword.isEmpty() || mConfirmPassword.isEmpty()) {
                Toast.makeText(RegistrationActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            executor.execute(() -> {
                boolean usernameExists = doesUserNameExist(mUsername);
                runOnUiThread(() -> {
                    if (!usernameExists) {
                        if (mPassword.equals(mConfirmPassword)) {
                            createUser(mUsername, mPassword);
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegistrationActivity.this, "This username is not available", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });
    }

    static Intent registrationIntentFactory(Context context) {
        return new Intent(context, RegistrationActivity.class);
    }

    private boolean doesUserNameExist(String username) {
        User user = repository.getUserByUsername(username);
        return user != null;
    }

    private void createUser(String username, String password) {
        User newUser = new User(username, password);
        executor.execute(() -> {
            repository.insertUser(newUser);
            runOnUiThread(() -> {
                Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}

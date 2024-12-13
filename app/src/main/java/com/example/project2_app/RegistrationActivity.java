package com.example.project2_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2_app.database.InventoryManagementRepository;
import com.example.project2_app.database.entities.User;
import com.example.project2_app.databinding.ActivityRegistrationBinding;

public class RegistrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding binding;
    private InventoryManagementRepository repository;
    private boolean isHandled = false; // Prevent multiple executions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = InventoryManagementRepository.getRepository(getApplication());

        binding.enterRegisterButton.setOnClickListener(v -> {
            String username = binding.usernameEditText.getText().toString().trim();
            String password = binding.passwordEditText.getText().toString().trim();
            String confirmPassword = binding.confirmPasswordEditText.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            validateAndRegisterUser(username, password);
        });
    }

    private void validateAndRegisterUser(String username, String password) {
        repository.getUserByUserName(username.toLowerCase()).observe(this, existingUser -> {
            if (isHandled) return; // Prevent multiple triggers
            repository.getUserByUserName(username.toLowerCase()).removeObservers(this); // Remove observer immediately

            if (existingUser != null) {
                Toast.makeText(this, "This username is already taken.", Toast.LENGTH_SHORT).show();
            } else {
                // Username is valid; create the new user
                createUser(username, password);
            }
        });
    }

    private void createUser(String username, String password) {
        User newUser = new User(username, password);
        repository.insertUser(newUser);
        isHandled = true; // Mark as handled
        Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show();
        finish(); // Close the registration activity
    }

    public static Intent registrationIntentFactory(Context context) {
        return new Intent(context, RegistrationActivity.class);
    }
}

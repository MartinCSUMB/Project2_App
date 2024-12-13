package com.example.project2_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2_app.database.InventoryManagementRepository;

public class ChangeUsernameActivity extends AppCompatActivity {

    private InventoryManagementRepository repository;
    private boolean isHandled = false; // Prevent multiple executions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);

        repository = InventoryManagementRepository.getRepository(getApplication());

        findViewById(R.id.saveUsernameButton).setOnClickListener(v -> {
            String newUsername = ((android.widget.EditText) findViewById(R.id.newUsernameEditText)).getText().toString().trim();
            validateAndChangeUsername(newUsername);
        });
    }

    private void validateAndChangeUsername(String newUsername) {
        if (newUsername.isEmpty()) {
            Toast.makeText(this, "Username cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFERENCE_USERID_KEY, Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt(MainActivity.SHARED_PREFERENCE_USERID_KEY, -1);

        if (userId == -1) {
            Toast.makeText(this, "No logged-in user found.", Toast.LENGTH_SHORT).show();
            return;
        }

        repository.getUserByUserId(userId).observe(this, currentUser -> {
            if (isHandled) return; // Avoid multiple triggers
            repository.getUserByUserId(userId).removeObservers(this); // Remove observer immediately

            if (currentUser == null) {
                Toast.makeText(this, "User not found.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if the new username is the same as the current username
            if (currentUser.getUsername().equalsIgnoreCase(newUsername)) {
                Toast.makeText(this, "The entered username is already your current username.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if the new username already exists (case-insensitive)
            repository.getUserByUserName(newUsername.toLowerCase()).observe(this, existingUser -> {
                if (isHandled) return; // Avoid multiple triggers
                repository.getUserByUserName(newUsername.toLowerCase()).removeObservers(this); // Remove observer immediately

                if (existingUser != null) {
                    Toast.makeText(this, "This username is already taken.", Toast.LENGTH_SHORT).show();
                } else {
                    // Update username
                    currentUser.setUsername(newUsername);
                    repository.updateUser(currentUser);
                    isHandled = true; // Mark as handled

                    // Success message and redirect
                    Toast.makeText(this, "Username updated successfully.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChangeUsernameActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
        });
    }

    public static Intent changeUsernameIntentFactory(Context context) {
        return new Intent(context, ChangeUsernameActivity.class);
    }
}

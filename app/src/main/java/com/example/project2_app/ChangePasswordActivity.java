package com.example.project2_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2_app.database.InventoryManagementRepository;
import com.example.project2_app.databinding.ActivityChangePasswordBinding;

public class ChangePasswordActivity extends AppCompatActivity {

    private ActivityChangePasswordBinding binding;
    private InventoryManagementRepository repository;
    private boolean isHandled = false; // Class-level flag

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = InventoryManagementRepository.getRepository(getApplication());

        binding.savePasswordButton.setOnClickListener(v -> changePassword());
    }

    private void changePassword() {
        // Retrieve input values
        String oldPassword = binding.oldPasswordEditText.getText().toString();
        String newPassword = binding.newPasswordEditText.getText().toString();
        String confirmPassword = binding.confirmPasswordEditText.getText().toString();

        // Input validations
        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "New passwords do not match.", Toast.LENGTH_SHORT).show();
            return;
        }

        // User verification
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFERENCE_USERID_KEY, Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt(MainActivity.SHARED_PREFERENCE_USERID_KEY, -1);

        if (userId == -1) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Observe user data and handle password change
        repository.getUserByUserId(userId).observe(this, user -> {
            if (isHandled) return; // Prevent multiple executions

            repository.getUserByUserId(userId).removeObservers(this); // Ensure observer is removed

            if (user == null) {
                Toast.makeText(this, "User not found.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if the old password is correct
            if (!user.getPassword().equals(oldPassword)) {
                Toast.makeText(this, "Old password is incorrect.", Toast.LENGTH_SHORT).show();
                return; // Stop further execution
            }

            // Check if the new password matches the current password
            if (user.getPassword().equals(newPassword)) {
                Toast.makeText(this, "New password cannot be the same as the current password.", Toast.LENGTH_SHORT).show();
                return; // Stop further execution
            }

            // Password is valid, update to new password
            user.setPassword(newPassword);
            repository.updateUser(user);

            // Mark process as handled and navigate back to MainActivity
            isHandled = true; // Set the flag only on successful update
            Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
            intent.putExtra("PASSWORD_CHANGE_MESSAGE", "Password updated successfully.");
            startActivity(intent);
            finish(); // Close this activity
        });
    }

    public static Intent changePasswordIntentFactory(Context context) {
        return new Intent(context, ChangePasswordActivity.class);
    }
}

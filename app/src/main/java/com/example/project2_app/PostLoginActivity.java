package com.example.project2_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project2_app.database.InventoryManagementRepository;
import com.example.project2_app.databinding.ActivityManageItemsBinding;
import com.example.project2_app.databinding.ActivityPostLoginBinding;

public class PostLoginActivity extends AppCompatActivity {

    private ActivityPostLoginBinding binding;

    private InventoryManagementRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Simulated value of isAdmin; replace with actual logic
        boolean isAdmin = getIsAdmin(); // Replace with your real database or session logic

        // Show or hide the admin button based on isAdmin
        if (isAdmin) {
            binding.adminButton.setVisibility(View.VISIBLE); // Show the button for admins
        } else {
            binding.adminButton.setVisibility(View.GONE); // Hide the button for regular users
        }

        // Admin button action
        binding.adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Admin features accessed!", Toast.LENGTH_SHORT).show();
                // Navigate to admin activity or perform admin action
            }
        });
    }

    private boolean getIsAdmin() {
    }
}
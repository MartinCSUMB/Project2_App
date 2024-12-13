package com.example.project2_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2_app.database.InventoryManagementRepository;
import com.example.project2_app.databinding.ActivityAdminBinding;

public class AdminActivity extends AppCompatActivity {

    private ActivityAdminBinding binding;
    private InventoryManagementRepository repository;

    public static final String TAG = "DAC_INVENTORY_MANAGER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = InventoryManagementRepository.getRepository(getApplication());

        binding.manageItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ManageItemsActivity.manageItemsIntentFactory(getApplicationContext());
                startActivity(intent);
            }
        });

        binding.manageAislesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ManageAislesActivity.manageShelvesIntentFactory(getApplicationContext());
                startActivity(intent);
            }
        });

        binding.getInventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = GetInventoryActivity.getInventoryIntentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
        binding.manageUsersButton.setOnClickListener(v -> {
            Intent intent = ManageUsersActivity.manageUsersIntentFactory(getApplicationContext());
            startActivity(intent);
        });
    }

    static Intent adminIntentFactory(Context context){
        return new Intent(context, AdminActivity.class);
    }

}
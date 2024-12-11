package com.example.project2_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project2_app.databinding.ActivityGetInventoryBinding;

public class GetInventoryActivity extends AppCompatActivity {

    private ActivityGetInventoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetInventoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String itemName = intent.getStringExtra("productKey");
        String itemAisle = intent.getStringExtra("aisleKey");

        binding.returnToAdminMenuFromInventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdminActivity.adminIntentFactory(getApplicationContext());
                startActivity(intent);
            }
        });

    }

    static Intent getInventoryIntentFactory(Context context){
        return new Intent(context, GetInventoryActivity.class);
    }
}
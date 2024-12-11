package com.example.project2_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.project2_app.database.InventoryManagementRepository;
import com.example.project2_app.database.entities.Product;
import com.example.project2_app.databinding.ActivityMainBinding;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private InventoryManagementRepository repository;

    private int id;
    private int result;
    private Product product;

    int loggedStoreId = -1;



    public static Intent mainActivityIntentFactory(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //starts with StoreActivity
        /*

        loginStore();

        if(loggedStoreId == -1) {
            Intent intent = StoreActivity.storeIntentFactory(getApplicationContext());
            startActivity(intent);
        }

         */

        repository = InventoryManagementRepository.getRepository(getApplication());

        binding.helloButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdminActivity.adminIntentFactory(getApplicationContext());
                startActivity(intent);
            }
        });

        binding.registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = RegistrationActivity.registrationIntentFactory(getApplicationContext());
                startActivity(intent);


            }

        });

    }

    private void loginStore() {

    }

}
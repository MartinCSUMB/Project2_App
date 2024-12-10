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
    private String username = "";
    private String password = "";


    public static Intent mainActivityIntentFactory(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        repository = InventoryManagementRepository.getRepository(getApplication());




        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getInformationFromDisplay();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check user credentials in the database
                else{
                    boolean isValidUser= repository.validateUser(username, password);

                        if (isValidUser) {
                            // Navigate to PostLoginActivity
                            Intent intent = new Intent(MainActivity.this, PostLoginActivity.class);
                            intent.putExtra("username", username);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                        }
                    }

                    public void onFailure(Throwable t) {
                        Toast.makeText(MainActivity.this, "Login failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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
    private void getInformationFromDisplay(){
        username = binding.usernameEditText.getText().toString().trim();
        password = binding.passwordEditText.getText().toString().trim();

    }

}
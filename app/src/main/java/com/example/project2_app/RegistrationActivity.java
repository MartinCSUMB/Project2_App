package com.example.project2_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;

import com.example.project2_app.database.InventoryManagementRepository;
import com.example.project2_app.databinding.ActivityRegistrationBinding;

public class RegistrationActivity extends AppCompatActivity {

    private String mUsername = "";
    private String mPassword = "";
    private String mConfirmPassword = "";

    private ActivityRegistrationBinding binding;

    private InventoryManagementRepository repository;

    private Observer observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.returnToAdminMenuFromRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainActivity.mainActivityIntentFactory(getApplicationContext());
                startActivity(intent);
            }
        });

        binding.enterRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!doesUserNameExist(mUsername)){
                    if(mPassword.equals(mConfirmPassword)){
                        //todo create user
                    }
                    else{
                        Toast.makeText(RegistrationActivity.this, "passwords do not match", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(RegistrationActivity.this, "this username is not available",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    static Intent registrationIntentFactory(Context context){
        return new Intent(context, RegistrationActivity.class);
    }

    private boolean doesUserNameExist(String username){
        //todo make repository method to get number of users with matching id
        /*
        if (repository.getUserByUsername()< 0){
            return false;
           }
        else{
            return true;
           }
         */

        /**
         * @QUERY("SELECT COUNT() FROM InventoryManagementDatabase.USER_TABLE WHERE username = :username")
         */
        return false;
    }
}
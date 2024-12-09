package com.example.project2_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2_app.databinding.ActivityManageShelvesBinding;

public class ManageAislesActivity extends AppCompatActivity {

    String mShelvesAdd = "";
    String mShelvesRemove = "";

    private ActivityManageShelvesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageShelvesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.aisleEnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo add shelf to db
            }
        });

        binding.shelfRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo remove shelf from db
                // clear db of products with matching shelf Id
                // todo make spinner for existing shelves
            }
        });

        binding.returnToAdminMenuFromShelvesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdminActivity. adminIntentFactory(getApplicationContext());
                startActivity(intent);
            }
        });

        }

    private void getShelfToAdd(){
        mShelvesAdd = binding.aisleNameEditText.getText().toString();
        // create aisle and add to database
    }


    static Intent manageShelvesIntentFactory(Context context){
        return new Intent(context, ManageAislesActivity.class);
    }

}

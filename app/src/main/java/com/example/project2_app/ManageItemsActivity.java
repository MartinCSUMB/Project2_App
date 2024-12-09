package com.example.project2_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project2_app.databinding.ActivityAdminBinding;
import com.example.project2_app.databinding.ActivityManageItemsBinding;

public class ManageItemsActivity extends AppCompatActivity {

    private ActivityManageItemsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageItemsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String[] arraySpinner = new String[]{"add", "remove"};

        Spinner s = (Spinner) findViewById(R.id.addOrRemoveSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        binding.enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo add item to database
            }
        });

        binding.returnToAdminMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdminActivity. adminIntentFactory(getApplicationContext());
                startActivity(intent);
            }
        });

    }

    static Intent manageItemsIntentFactory(Context context){
        return new Intent(context, ManageItemsActivity.class);
    }

}
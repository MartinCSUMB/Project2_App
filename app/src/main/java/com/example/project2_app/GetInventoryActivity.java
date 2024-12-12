package com.example.project2_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project2_app.database.InventoryManagementRepository;
import com.example.project2_app.database.entities.Aisle;
import com.example.project2_app.database.entities.Product;
import com.example.project2_app.databinding.ActivityGetInventoryBinding;

import java.util.ArrayList;
import java.util.List;

public class GetInventoryActivity extends AppCompatActivity {

    private int mStoreId = 1;

    private InventoryManagementRepository repository;

    private ActivityGetInventoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetInventoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        repository = InventoryManagementRepository.getRepository(getApplication());
        updateDisplay();
        binding.allInventoryTextView.setMovementMethod(new ScrollingMovementMethod());


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

    private void updateDisplay(){
        List<Aisle> allAisles = repository.getAllAislesByStoreId(mStoreId);
        List<Product> productsOnAisle;

        if(allAisles.isEmpty()){
            binding.allInventoryTextView.setText("No items in inventory. womp womp :(");
        }
        else {
            StringBuilder sb = new StringBuilder();
            for (Aisle aisle : allAisles) {
                sb.append("Aisle: ").append(aisle.getName()).append(":").append('\n');
                productsOnAisle = repository.getProductListByAisleId(aisle.getAisleId());
                for (Product product : productsOnAisle) {
                    sb.append("  Product: ").append(product.getName()).append(" (P/N: ").
                            append(product.getPartNumber()).append(")").append('\n').append("  Price: ").append(product.getCost()).
                            append(" Quantity: ").append(product.getCount()).append('\n').append('\n');
                }


            }
            binding.allInventoryTextView.setText(sb.toString());
        }
    }
}
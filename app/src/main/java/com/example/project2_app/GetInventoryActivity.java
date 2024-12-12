package com.example.project2_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

        //setting up store spinners
        String[] storeSpinner = new String[]{"College Ave", "Murphy Canyon Rd", "Dennery Rd"};

        Spinner storeSpin = (Spinner) findViewById(R.id.storeSpinner);
        ArrayAdapter<String> storeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, storeSpinner);
        storeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        storeSpin.setAdapter(storeAdapter);

        binding.storeSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = storeSpin.getSelectedItem().toString();
                if(location.equals("College Ave")) mStoreId = 1;
                if(location.equals("Murphy Canyon Rd")) mStoreId = 2;
                if(location.equals("Dennery Rd")) mStoreId = 3;
                updateDisplay();
            }

        });

        binding.bookmarkButton.setOnClickListener(v -> {
            String productName = binding.productToBookMarkSpinner.getSelectedItem().toString();
            Product product = repository.getProductByNameAndStoreIdFuture(productName, mStoreId);

            if (product != null) {
                boolean currentBookmarkStatus = product.isBookmarked();
                repository.updateBookmark(product.getProductId(), !currentBookmarkStatus);
                updateDisplay(); // Refresh inventory view
            }
        });



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

    private void updateDisplay() {
        List<Aisle> allAisles = repository.getAllAislesByStoreId(mStoreId);
        List<Product> productsOnAisle;
        List<String> productNames = new ArrayList<>(); // To populate the spinner

        if (allAisles.isEmpty()) {
            binding.allInventoryTextView.setText("No items in inventory. womp womp :(");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Aisle aisle : allAisles) {
                sb.append("Aisle: ").append(aisle.getName()).append(":").append('\n');
                productsOnAisle = repository.getProductListByAisleId(aisle.getAisleId());
                for (Product product : productsOnAisle) {
                    sb.append("  Product: ").append(product.getName())
                            .append(" (Bookmarked: ").append(product.isBookmarked() ? "Yes" : "No").append(")")
                            .append('\n')
                            .append("  Price: ").append(product.getCost())
                            .append(" Quantity: ").append(product.getCount())
                            .append('\n').append('\n');
                    productNames.add(product.getName()); // Add product name to the list
                }
            }
            binding.allInventoryTextView.setText(sb.toString());
        }

        ArrayAdapter<String> productAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, productNames);
        productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.productToBookMarkSpinner.setAdapter(productAdapter);
    }


}
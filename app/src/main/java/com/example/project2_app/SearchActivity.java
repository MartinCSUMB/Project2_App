package com.example.project2_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.project2_app.database.InventoryManagementRepository;
import com.example.project2_app.database.entities.Aisle;
import com.example.project2_app.database.entities.Product;
import com.example.project2_app.databinding.ActivitySearchBinding;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding binding;
    private InventoryManagementRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //repository = InventoryManagementRepository.getRepository(getApplication());

        //binding.searchButton.setOnClickListener(v -> findSearch());
        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = searchListActivity.searchListIntentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
//        binding.returnToMainButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish(); // Optional: Use this to prevent returning to the SearchActivity on back press
//            }
//        });
//        binding.returnToMainButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish(); // Optional: Use this to prevent returning to the SearchActivity on back press
//            }
//        });
        binding.returnToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Optional: Use this to prevent returning to the SearchActivity on back press
            }
        });
    }

    private void findSearch() {
        String itemName = binding.searchNameEditText.getText().toString();
        String itemAisle = binding.searchAisleEditText.getText().toString();

        if (itemName.isEmpty() && itemAisle.isEmpty()) {
            toastMaker("Name or Aisle should not be empty");
            return;
        }

        if (!itemName.isEmpty() && !itemAisle.isEmpty()) {
            searchProductInAisle(itemAisle);
        } else if (!itemName.isEmpty()) {
            searchProductByName(itemName);
        } else {
            searchAisle(itemAisle);
        }
        //Log.i("DAC_APP", "Received Product ID: " + repository.getAisleByName(itemAisle).getValue().getAisleId() + ", Aisle ID: " + repository.getProductByName(itemName).getValue().getProductId());

    }

    private void searchProductInAisle(String aisleName) {
        LiveData<Aisle> aisleObserver = repository.getAisleByName(aisleName);
        aisleObserver.observe(this, aisle -> {
            if (aisle != null) {
                LiveData<Product> productObserver = repository.getProductByAisleID(aisle.getAisleId());
                productObserver.observe(this, product -> {
                    if (product != null) {
                        saveProductToPreferences(product.getProductId());
                    } else {
                        toastMaker("Product not found");
                    }
                });
            } else {
                toastMaker("Aisle not found");
            }
        });
    }

    private void searchProductByName(String itemName) {
        LiveData<Product> productObserver = repository.getProductByName(itemName);
        productObserver.observe(this, product -> {
            if (product != null) {
                saveProductToPreferences(product.getProductId());
            } else {
                toastMaker("Product not found");
            }
        });
    }

    private void searchAisle(String aisleName) {
        LiveData<Aisle> aisleObserver = repository.getAisleByName(aisleName);
        aisleObserver.observe(this, aisle -> {
            if (aisle != null) {
                saveProductToPreferences(aisle.getAisleId());
            } else {
                toastMaker("Aisle not found");
            }
        });
    }

    private void saveProductToPreferences(int productId) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(searchListActivity.SHARED_PREFERENCE_ITEM_ID_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putInt(searchListActivity.SHARED_PREFERENCE_ITEM_ID_KEY, productId);
        sharedPrefEditor.apply();

        Intent intent = searchListActivity.searchListIntentFactory(getApplicationContext());
        startActivity(intent);
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    static Intent searchIntentFactory(Context context) {
        return new Intent(context, SearchActivity.class);
    }
}

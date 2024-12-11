package com.example.project2_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findSearch();
            }
        });
    }

    private void findSearch(){
        String itemName = binding.searchNameEditText.getText().toString();
        String itemAisle = binding.searchAisleEditText.getText().toString();

        if(itemName.isEmpty() && itemAisle.isEmpty()){
            toastMaker("Name or Aisle should not be empty");
            return;
        }

        if(!itemName.isEmpty() && !itemAisle.isEmpty()){
            LiveData<Aisle> aisleObserver = repository.getAisleByName(itemAisle);
            aisleObserver.observe(this, aisle -> {
                if(aisle != null){
                    LiveData<Product> productObserver = repository.getProductByAisleID(aisle.getAisleId());
                    productObserver.observe(this, product -> {
                        if(product != null){
                            Intent intent = GetInventoryActivity.getInventoryIntentFactory(getApplicationContext());
                            intent.putExtra("productKey",itemName);
                            intent.putExtra("aisleKey",itemAisle);
                            startActivity(intent);
                        }
                        else {
                            toastMaker("Product not found");
                        }
                    });
                }
                else {
                    toastMaker("Aisle not found");
                }
            });
        }

        if(!itemName.isEmpty() && itemAisle.isEmpty()) {
            LiveData<Product> productObserver = repository.getProductByName(itemName);
            productObserver.observe(this, product -> {
                if (product != null) {
                    Intent intent = GetInventoryActivity.getInventoryIntentFactory(getApplicationContext());
                    intent.putExtra("productKey",itemName);
                    startActivity(intent);
                } else {
                    toastMaker("Product not found");
                }
            });
        }

        if(!itemAisle.isEmpty() && itemName.isEmpty()) {
            LiveData<Aisle> aisleObserver = repository.getAisleByName(itemAisle);
            aisleObserver.observe(this, aisle -> {
                if (aisle != null) {
                    LiveData<Product> productObserver = repository.getProductByAisleID(aisle.getAisleId());
                    productObserver.observe(this, product -> {
                        Intent intent = GetInventoryActivity.getInventoryIntentFactory(getApplicationContext());
                        intent.putExtra("aisleKey",itemAisle);
                        startActivity(intent);
                    });
                } else {
                    toastMaker("Aisle not found");
                }
            });
        }
    }

    private void toastMaker(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    static Intent searchIntentFactory(Context context){
        return new Intent(context, SearchActivity.class);
    }
}
package com.example.project2_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.project2_app.database.InventoryManagementDatabase;
import com.example.project2_app.database.InventoryManagementRepository;
import com.example.project2_app.database.ProductDAO;
import com.example.project2_app.database.StoreDAO;
import com.example.project2_app.database.entities.Aisle;
import com.example.project2_app.database.entities.Product;
import com.example.project2_app.database.entities.Store;
import com.example.project2_app.databinding.ActivitySearchListBinding;

import java.util.ArrayList;
import java.util.List;

public class searchListActivity extends AppCompatActivity {

    public static final String SHARED_PREFERENCE_ITEM_ID_KEY = "com.example.project2_app.SHARED_PREFERENCE_ITEM_ID_KEY";
    private static final String SAVED_INSTANCE_STATE_AISLE_ID_KEY = "com.example.project2_app.SAVED_INSTANCE_STATE_AISLE_ID_KEY";
    private static final String SHARED_PREFERENCE_AISLE_ID_KEY = "com.example.project2_app.SHARED_PREFERENCE_AISLE_ID_KEY";
    private static final String SAVED_INSTANCE_STATE_ITEM_ID_KEY = "com.example.project2_app.SAVED_INSTANCE_STATE_ITEM_ID_KEY";
    private static final int NOT_VALID = -1;

    private ActivitySearchListBinding binding;

    int aisleId = -1;
    int productId = 1;

    private ListView listView;

    private InventoryManagementRepository repository;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        Intent intent = getIntent();
//        productId = intent.getIntExtra("productId", -1);  // Getting the product ID
//        aisleId = intent.getIntExtra("aisleId", -1);      // Getting the aisle ID
//        Log.i("DAC_APP", "Received Product ID: " + productId + ", Aisle ID: " + aisleId);
//
        super.onCreate(savedInstanceState);
        binding = ActivitySearchListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listView = findViewById(R.id.searchedProductsListView);

        repository = InventoryManagementRepository.getRepository(getApplication());

        if(repository != null){
            List<Product> products = repository.getProducts();

            if(products != null && !products.isEmpty()){
                List<String> productsList = new ArrayList<>();
                for(Product product : products){
                    String format = product.getName() + " " +
                                    product.getAisleId() + " " +
                                    product.getCost() + " " +
                                    product.getPartNumber();
                    productsList.add(format);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, productsList);
                listView.setAdapter(adapter);
            }
        }
//
//        retrieveProducts(savedInstanceState);

        binding.searchedReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SearchActivity.searchIntentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
    }


//    private void retrieveProducts(Bundle savedInstanceState) {
//        if (savedInstanceState != null) {
//            aisleId = savedInstanceState.getInt(SAVED_INSTANCE_STATE_AISLE_ID_KEY, NOT_VALID);
//            productId = savedInstanceState.getInt(SAVED_INSTANCE_STATE_ITEM_ID_KEY, NOT_VALID);
//        }
//
//        // Log the restored values for debugging
//        Log.d("searchListActivity", "Restored aisleId: " + aisleId + ", productId: " + productId);
//
//        if (aisleId != -1) {
//            LiveData<Aisle> aisle = repository.getAisleById(aisleId);
//
//            aisle.observe(this, fetchedAisle -> {
//                if (fetchedAisle != null) {
//                    List<Product> items = repository.getProductListByAisleId(fetchedAisle.getAisleId());
//
//                    if (items != null && !items.isEmpty()) {
//                        List<String> itemsDisplay = new ArrayList<>();
//                        for (Product item : items) {
//                            String format = item.getName() + " " +
//                                    fetchedAisle.getName() + " " +
//                                    item.getCost() + " " +
//                                    item.getPartNumber();
//                            itemsDisplay.add(format);
//                        }
//                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
//                                android.R.layout.simple_list_item_checked, itemsDisplay);
//                        listView.setAdapter(adapter);
//                    } else {
//                        Log.d("searchListActivity", "No products found for the specified aisle.");
//                    }
//                } else {
//                    Log.d("searchListActivity", "Aisle not found.");
//                }
//            });
//        }
//
//        if (productId != -1 && aisleId == -1) {
//            LiveData<Product> item = repository.getProductById(productId);
//            item.observe(this, fetchedProduct -> {
//                if (fetchedProduct != null) {
//                    String format = fetchedProduct.getName() + " " +
//                            repository.getAisleById(fetchedProduct.getAisleId()).getValue().getName() +
//                            " " + fetchedProduct.getCost() + " " + fetchedProduct.getPartNumber();
//                    List<String> items = new ArrayList<>();
//                    items.add(format);
//                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
//                            android.R.layout.simple_list_item_checked, items);
//                    listView.setAdapter(adapter);
//                } else {
//                    Log.d("searchListActivity", "Product not found.");
//                }
//            });
//        }
//    }
//
//    @Override
//    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        retrieveProducts(savedInstanceState);
//    }

    static Intent searchListIntentFactory(Context context){
        return new Intent(context, searchListActivity.class);
    }
}
//it works now

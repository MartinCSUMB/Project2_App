package com.example.project2_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project2_app.database.InventoryManagementDatabase;
import com.example.project2_app.database.InventoryManagementRepository;
import com.example.project2_app.database.StoreDAO;
import com.example.project2_app.database.entities.Store;
import com.example.project2_app.databinding.ActivityStoreBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class StoreActivity extends AppCompatActivity {

    private ActivityStoreBinding binding;
    private ListView listView;
    private InventoryManagementRepository repository;
    private String selection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listView = findViewById(R.id.storesListView);

        repository = InventoryManagementRepository.getRepository(getApplication());

        if(repository != null) {
            List<Store> storeStreets = repository.getAllStores();

            if(storeStreets != null && !storeStreets.isEmpty()){
                List<String> streetStrings = new ArrayList<>();
                for(Store storeStreet : storeStreets){
                    streetStrings.add(storeStreet.getStoreStreet());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, streetStrings);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener((parent, view, position, id) -> {
                    selection = (String) parent.getItemAtPosition(position);
                    Toast.makeText(StoreActivity.this, selection, Toast.LENGTH_SHORT).show();
                });

                binding.chooseStoreButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!selection.isEmpty()){
                            startActivity(MainActivity.mainActivityIntentFactory(getApplicationContext()));
                        }
                        else{
                            Toast.makeText(StoreActivity.this, "Selection Empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        } else {
            Log.i("Store Activity","Problem when getting all Stores in the repository");
        }
    }

    static Intent storeIntentFactory(Context context){
        return new Intent(context, StoreActivity.class);
    }
}
package com.example.project2_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.project2_app.database.InventoryManagementRepository;
import com.example.project2_app.database.entities.Aisle;
import com.example.project2_app.database.entities.Product;
import com.example.project2_app.databinding.ActivityManageShelvesBinding;

import java.util.ArrayList;
import java.util.List;

public class ManageAislesActivity extends AppCompatActivity {

    String mShelvesAdd = "";
    String mShelvesRemove = "";

    private List<Aisle> allAisles;

    private List<Product> allProducts;

    private InventoryManagementRepository repository;

    private ActivityManageShelvesBinding binding;

    String[] arrayOfAisleNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageShelvesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        repository = InventoryManagementRepository.getRepository(getApplication());
        makeArrayForSpinner();
        setSpinner();


        binding.aisleEnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertAisleRecord();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                makeArrayForSpinner();

                setSpinner();
            }
        });

        binding.shelfRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAisleRecord();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                makeArrayForSpinner();

                setSpinner();

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

    static Intent manageShelvesIntentFactory(Context context){
        return new Intent(context, ManageAislesActivity.class);
    }

    private void insertAisleRecord(){
        mShelvesAdd =  binding.aisleNameEditText.getText().toString();
        if(mShelvesAdd.isEmpty()){
            Toast.makeText(this, "enter name for aisle", Toast.LENGTH_SHORT).show();
            return;
        }
        Aisle aisle= new Aisle(mShelvesAdd);
        repository.insertAisle(aisle);
    }

    private void deleteAisleRecord(){
        if(binding.shelfNameSpinner.getSelectedItem()==null){
            Toast.makeText(this, "no more aisles to remove", Toast.LENGTH_SHORT).show();
        }
        else{
            allProducts = repository.getAllProductsFuture();
            mShelvesRemove = binding.shelfNameSpinner.getSelectedItem().toString();
            Aisle aisleDelete = null;
            for(Aisle aisle : allAisles){
                if (aisle.getName().equals(mShelvesRemove)){
                    aisleDelete = aisle;
                }
            }
            if(aisleDelete!=null){
                repository.deleteAisle(aisleDelete);
                Toast.makeText(this, "aisle deleted and the items in it", Toast.LENGTH_SHORT).show();
                for(Product product: allProducts){
                    if (product.getAisleId()==aisleDelete.getAisleId()){
                        repository.deleteProduct(product);
                    }
                }
            }
        }
    }

    private void makeArrayForSpinner(){
        allAisles = repository.getAllAislesFuture();
        arrayOfAisleNames =  new String[allAisles.size()];
        for(int i = 0; i < allAisles.size(); i++){
                arrayOfAisleNames[i] = allAisles.get(i).getName();
            }
    }

    private void setSpinner(){
        Spinner s = (Spinner) findViewById(R.id.shelfNameSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayOfAisleNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
    }
}

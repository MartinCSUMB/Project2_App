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

/**
 * @author Joe Young
 * This activity adds and removes aisles from the database. The same ailse name can exist multiple
 * times in the database but can only exist once per store (store id). If an aisle is deleted with
 * products in it, those products are deleted as well
 */

public class ManageAislesActivity extends AppCompatActivity {

    private String mShelvesAdd = "";
    private String mShelvesRemove = "";

    private int mStoreId = 0;

    private Aisle aisle;

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
                makeArrayForSpinner();
                setSpinner();
            }

        });


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

    /**
     * adds aisle to data base only if it does not already exist in the store
     */
    private void insertAisleRecord(){
        mShelvesAdd =  binding.aisleNameEditText.getText().toString();
        if(mShelvesAdd.isEmpty()){
            Toast.makeText(this, "enter name for aisle", Toast.LENGTH_SHORT).show();
            return;
        }
        aisle = repository.getAisleByNameAndStoreIdFuture(mShelvesAdd, mStoreId);
        if(aisle!=null){
            Toast.makeText(this, "this aisle already exists for this store", Toast.LENGTH_SHORT).show();
        }
        else{
            Aisle aisle= new Aisle(mShelvesAdd,mStoreId);
            repository.insertAisle(aisle);
            Toast.makeText(this, "aisle created!", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * removes the aisle from the store if it exists
     */
    private void deleteAisleRecord(){
        if(binding.shelfNameSpinner.getSelectedItem()==null){
            Toast.makeText(this, "no more aisles to remove", Toast.LENGTH_SHORT).show();
        }
        else{
            mShelvesRemove = binding.shelfNameSpinner.getSelectedItem().toString();
            aisle = repository.getAisleByNameAndStoreIdFuture(mShelvesRemove, mStoreId);{
                if(aisle!= null){
                    repository.deleteAisle(aisle);
                    Toast.makeText(this, "aisle deleted along with its contents", Toast.LENGTH_SHORT).show();
                    allProducts = repository.getAllProductsFuture();
                    for(Product product: allProducts){
                        if (product.getAisleId() == aisle.getAisleId()){
                            repository.deleteProduct(product);
                        }
                    }
                }
            }

        }
    }

    //create an array of names with aisles in this store
    private void makeArrayForSpinner(){
        allAisles = repository.getAllAislesByStoreId(mStoreId);
        arrayOfAisleNames =  new String[allAisles.size()];
        for(int i = 0; i < allAisles.size(); i++){
            arrayOfAisleNames[i] = allAisles.get(i).getName();
        }
    }

    //sets spinner using the array of aisle names
    private void setSpinner(){
        Spinner s = (Spinner) findViewById(R.id.shelfNameSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayOfAisleNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
    }

}

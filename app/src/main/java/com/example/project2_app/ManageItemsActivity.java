package com.example.project2_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2_app.database.InventoryManagementRepository;
import com.example.project2_app.database.entities.Aisle;
import com.example.project2_app.database.entities.Product;
import com.example.project2_app.databinding.ActivityManageItemsBinding;

import java.util.List;

public class ManageItemsActivity extends AppCompatActivity {

    //data from edit text boxes
    private String mAisleName = "";
    private String mName = "";
    private int mPartNumber = 0;
    private int mQuantity = 0;
    private double mCost =0.0;
    private String mAction = "";

    //used for setting entities retrieved from db
    private Aisle aisle;
    private Product product;

    //used for creating spinner
    String[] arrayOfAisleNames;
    private List<Aisle> allAisles;

    private ActivityManageItemsBinding binding;
    private InventoryManagementRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageItemsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        repository = InventoryManagementRepository.getRepository(getApplication());

        //make spinner for add and remove
        String[] arraySpinner = new String[]{"add", "remove"};

        Spinner actionSpin = (Spinner) findViewById(R.id.addOrRemoveSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actionSpin.setAdapter(adapter);

        //make spinner for aisles
        makeArrayForSpinner();

        Spinner aisleSpin = (Spinner) findViewById(R.id.aisleSpinner);
        ArrayAdapter<String> aisleAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayOfAisleNames);
        aisleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aisleSpin.setAdapter(aisleAdapter);

        binding.enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInformationFromDisplay();
                insertProductRecord();
            }
        });

        //gets aisle spinner value
        aisleSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                mAisleName = aisleSpin.getSelectedItem().toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //get action spinner value
        actionSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                mAction = actionSpin.getSelectedItem().toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
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

    private void getInformationFromDisplay(){
        mName = binding.itemNameEditText.getText().toString();
        try{
            mCost =Double.parseDouble(binding.costEditText.getText().toString());
        }catch (NumberFormatException e){
            //log
        }
        try{
            mPartNumber = Integer.parseInt(binding.itemPartNumberEditText.getText().toString());
        } catch (NumberFormatException e) {
            //log
        }
        try{
            mQuantity = Integer.parseInt(binding.quantityEditText.getText().toString());
        } catch (NumberFormatException e) {
           //log
        }
    }

    private void makeArrayForSpinner(){
        allAisles = repository.getAllAislesFuture();
        arrayOfAisleNames =  new String[allAisles.size()];
        for(int i = 0; i < allAisles.size(); i++){
            arrayOfAisleNames[i] = allAisles.get(i).getName();
        }
    }

    private void insertProductRecord(){
        if(mAisleName.isEmpty()){
            Toast.makeText(this, "add aisles first then add items!", Toast.LENGTH_SHORT).show();
        }
        else if(mName.isEmpty() || mQuantity==0){
            Toast.makeText(this, "make sure there is at least a name and quantity are filled",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            aisle = repository.getAisleByNameFuture(mAisleName);
            if (mAction.equals("add")) {
                addProductToDB(mName, aisle);
            } else {
                removeProductFromDB(mName, aisle);
            }
        }
    }

    private void removeProductFromDB(String name, Aisle aisle) {
        product=repository.getProductByNameFuture(name);
        if(product!=null){
            if(product.getCount()-mQuantity < 1){
                repository.deleteProduct(product);
            }
            else{
                repository.updateCountByName(mName, product.getCount()-mQuantity);
            }
        }
        else{
            Toast.makeText(this, "That product does not exist in the inventory", Toast.LENGTH_SHORT).show();
        }
    }

    private void addProductToDB(String name, Aisle aisle){
        product = repository.getProductByNameFuture(name);
        //product name already exists, update the product info and increment the quantity as specified by user input
        if(product!=null ){
            repository.updateCountByName(mName,product.getCount() + mQuantity);
            repository.updatePartNumberById(product.getProductId(), mPartNumber);
            repository.updateProductAisleIdById(product.getProductId(), aisle.getAisleId());
            repository.updateProductCost(product.getProductId(), mCost);
            Toast.makeText(this, "updated product information", Toast.LENGTH_SHORT).show();
        }
        //else create new product and insert into db
        else{
            Product productToAdd = new Product(aisle.getAisleId(), mName, mCost, mPartNumber, mQuantity);
            repository.insertProduct(productToAdd);
            Toast.makeText(this, "added new item to aisle", Toast.LENGTH_SHORT).show();
        }
    }

}
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

/**
 * @author: Joseph Young
 * Description: activity for adding and removing products from db. Multiple instances of a product can
 * exist in the database but it can only exist once per store. Adding items will either create a new
 * product if it is brand new to the store, or update the product information with the user input if
 * that store already has that product. Product IDs will be unique to every product
 */

public class ManageItemsActivity extends AppCompatActivity {

    //data from edit text boxes
    private String mAisleName = "";
    private String mName = "";
    private int mPartNumber = 0;
    private int mQuantity = 0;
    private double mCost =0.0;
    private int mStoreId = 0;
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

        //todo retrieve store id

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
                setUpAisleSpinner();
            }

        });



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

    /**
     * gets info from edit text boxes
     */
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

    /**
     * create array to be used for setting up spinner
     */
    private void makeArrayForSpinner(){
        allAisles = repository.getAllAislesByStoreId(mStoreId);
        arrayOfAisleNames =  new String[allAisles.size()];
        for(int i = 0; i < allAisles.size(); i++){
            arrayOfAisleNames[i] = allAisles.get(i).getName();
        }
    }

    /**
     * ensures aisle and name are not blank. makes methods calls to add or remove value from the db
     * depending on what is selected from the add/remove spinner
     */
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

    /**
     * checks to see if this name is in the store. if the product exists in the store, it will be !Null.
     * product is deleted once there are none left. If there are none left, the user is notified
     */
    private void removeProductFromDB(String name, Aisle aisle) {
        product = repository.getProductByNameAndStoreIdFuture(name,mStoreId);
        if(product!=null){
            if(product.getCount()-mQuantity < 1){
                repository.deleteProduct(product);
            }
            else{
                repository.updateProductCount(product.getProductId(), product.getCount()-mQuantity);
            }
        }
        else{
            Toast.makeText(this, "That product does not exist in the inventory", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * checks to see if this name is in the store. if the product exists in the store, it will be !Null.
     * if the product exists the quantity is of the product is increased by the quantity the user specifies.
     * All other product fields are update with the input from the user input. If the does not exist in
     * the store yet, it will new product will be inserted into the db.
     */
    private void addProductToDB(String name, Aisle aisle){
        product = repository.getProductByNameAndStoreIdFuture(name,mStoreId);
        //product name already exists, update the product info and increment the quantity as specified by user input
        if(product!=null){
            repository.updateProductCount(product.getProductId(),product.getCount() + mQuantity);
            repository.updatePartNumberById(product.getProductId(), mPartNumber);
            repository.updateProductAisleIdById(product.getProductId(), aisle.getAisleId());
            repository.updateProductCost(product.getProductId(), mCost);
            Toast.makeText(this, "updated product information", Toast.LENGTH_SHORT).show();
        }
        //else create new product and insert into db
        else{
            Product productToAdd = new Product(aisle.getAisleId(), mName, mCost, mPartNumber, mQuantity, mStoreId);
            repository.insertProduct(productToAdd);
            Toast.makeText(this, "added new item to aisle", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpAisleSpinner(){
        Spinner aisleSpin = (Spinner) findViewById(R.id.aisleSpinner);
        ArrayAdapter<String> aisleAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayOfAisleNames);
        aisleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aisleSpin.setAdapter(aisleAdapter);
    }
}
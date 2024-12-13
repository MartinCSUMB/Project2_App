package com.example.project2_app.database;

import android.content.Context;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.project2_app.database.entities.Aisle;
import com.example.project2_app.database.entities.Product;
import com.example.project2_app.database.entities.Store;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.util.List;

import static org.junit.Assert.*;


public class InventoryManagementDatabaseTestInserts {

    private InventoryManagementDatabase database;

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        database = Room.inMemoryDatabaseBuilder(context, InventoryManagementDatabase.class).build();
    }

    @After
    public void tearDown() {
        database.close();
    }

    //this test checks insert product
    @Test
    public void testInsertProduct() {
        Product product = new Product(1, "Test Product", 10.0, 1234, 5, 1);
        database.productDAO().insert(product);
        List<Product> products = database.productDAO().getAllProductsFuture();
        assertEquals(1, products.size());
    }


    //this test tests inserting an aisle
    @Test
    public void testInsertAisle() {
        Aisle aisle = new Aisle("Test Aisle", 1);
        database.aisleDAO().insert(aisle);
        List<Aisle> aisles = database.aisleDAO().getAllAislesListFuture();
        assertEquals(1, aisles.size());
        assertEquals("Test Aisle", aisles.get(0).getName());
    }

    @Test
    public void testInsertStore() {
        Store store = new Store("123 Main St");
        database.storeDAO().insert(store);
        List<Store> stores = database.storeDAO().getAllStores();
        assertEquals(1, stores.size());
        assertEquals("123 Main St", stores.get(0).getStoreStreet());
    }






}

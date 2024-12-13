package com.example.project2_app.database;

import android.content.Context;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.project2_app.database.entities.Product;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.util.List;

import static org.junit.Assert.*;


public class InventoryManagementDatabaseTest {

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

    @Test
    public void testInsertProduct() {
        Product product = new Product(1, "Test Product", 10.0, 1234, 5, 1);
        database.productDAO().insert(product);
        List<Product> products = database.productDAO().getAllProductsFuture();
        assertEquals(1, products.size());
    }

    @Test
    public void testUpdateProduct() {
        Product product = new Product(1, "Test Product", 10.0, 1234, 5, 1);
        database.productDAO().insert(product);
        product.setName("Updated Product");
        database.productDAO().update(product);
        Product updatedProduct = database.productDAO().getProductByNameAndStoreFuture("Updated Product", 1);
        assertNotNull(updatedProduct);
        assertEquals("Updated Product", updatedProduct.getName());
    }

    @Test
    public void testDeleteProduct() {
        Product product = new Product(1, "Test Product", 10.0, 1234, 5, 1);
        database.productDAO().insert(product);
        database.productDAO().delete(product);
        List<Product> products = database.productDAO().getAllProductsFuture();
        assertTrue(products.isEmpty());
    }



}

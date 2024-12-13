package com.example.project2_app.database;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import junit.framework.TestCase;



public class ProductDAOTest extends TestCase {

    private static InventoryManagementDatabase db;

    ProductDAO pDao = null;

    @Override
    protected void setUp() throws Exception {
        db = Room.inMemoryDatabaseBuilder(
                        ApplicationProvider.getApplicationContext(),
                        InventoryManagementDatabase.class
                )
                .allowMainThreadQueries()
                .build();
    }

    public void testGetAllProducts() {
    }

    public void testDeleteAll() {
    }

    public void testGetProductByName() {
    }

    public void testGetProductByPartNumber() {
    }

    public void testGetProductByID() {
    }

    public void testGetProductByAisleID() {
    }

    public void testUpdateCount() {
    }

    public void testUpdateCost() {
    }

    public void testGetBookmarkedItems() {
    }

    public void testGetProductByNameAndStoreFuture() {
    }

    public void testUpdatePartNumberById() {
    }

    public void testUpdateProductAisleIdById() {
    }

    public void testGetAllProductsFuture() {
    }

    public void testGetProductListByAisleId() {
    }

    public void testGetProducts() {
    }

    public void testUpdateBookmark() {
    }
}
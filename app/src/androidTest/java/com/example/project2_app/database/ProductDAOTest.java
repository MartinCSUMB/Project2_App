package com.example.project2_app.database;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.project2_app.database.entities.Product;

import junit.framework.TestCase;



public class ProductDAOTest extends TestCase {

    private static final String TEST1 = "test1";

    private static final String TEST2 = "test2";
    Product testProd1 = new Product(1,TEST1,1,1,1,1);
    Product testProd2 = new Product(2,TEST2,2,2,2,2);
    Product testProd3;
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
    public void tearDown() throws Exception {
        db.close();
    }

    public void testInsert() {
        assertNull(testProd3);
        pDao.insert(testProd1);

    }
}
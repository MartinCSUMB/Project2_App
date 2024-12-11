package com.example.project2_app.database;

import androidx.room.Room;

import androidx.test.core.app.ApplicationProvider;

import com.example.project2_app.database.entities.Store;

import junit.framework.TestCase;
public class StoreDAOTest extends TestCase {

    private static InventoryManagementDatabase db;
    private final String FIRST_ADDRESS = "Waterman Ave";
    private final String SECOND_ADDRESS = "Rockwood Ave";
    private final String THIRD_ADDRESS = "Wildcat Dr";
    private final int FIRST_STORE_ID = 1;
    private final int THIRD_STORE_ID = 3;
    StoreDAO sDao = null;

    Store testStore1 = new Store(FIRST_ADDRESS);
    Store testStore2 = new Store(SECOND_ADDRESS);
    Store testStore3 = new Store(THIRD_ADDRESS);

    public void setUp() throws Exception {
        db = Room.inMemoryDatabaseBuilder(
                        ApplicationProvider.getApplicationContext(),
                        InventoryManagementDatabase.class
                )
                .allowMainThreadQueries()
                .build();
        sDao = db.storeDAO();
    }

    public void tearDown() throws Exception {
        db.close();
    }

    public void testInsert() {
        sDao.insert(testStore1);
        assertNotNull(sDao);
        sDao.insert(testStore2);
        assertNotSame(sDao.getStoreByStreet(FIRST_ADDRESS),
                sDao.getStoreByStreet(SECOND_ADDRESS));
        sDao.insert(testStore1);
        assertEquals(sDao.getStoreById(FIRST_STORE_ID),
                sDao.getStoreById(THIRD_STORE_ID));
    }

    public void testUpdate() {
        sDao.insert(testStore1);
        sDao.insert(testStore3);
        sDao.insert(testStore1);
        assertNotNull(sDao);
        assertEquals(sDao.getStoreById(FIRST_STORE_ID),
                sDao.getStoreById(THIRD_STORE_ID));
        Store newStore = new Store(SECOND_ADDRESS);
        newStore.setStoreId(THIRD_STORE_ID);
        sDao.update(newStore);
        assertNotSame(sDao.getStoreById(FIRST_STORE_ID),
                sDao.getStoreById(THIRD_STORE_ID));
    }

    public void testDelete() {
        sDao.insert(testStore1);
        sDao.insert(testStore2);
        Store store1 = sDao.getStoreByStreet(FIRST_ADDRESS);
        Store store2 = sDao.getStoreByStreet(SECOND_ADDRESS);
        assertNotNull(store1);
        assertNotNull(store2);
        sDao.delete(store1);
        assertNull(sDao.getStoreByStreet(FIRST_ADDRESS));
        assertNotNull(sDao.getStoreByStreet(SECOND_ADDRESS));
    }
}
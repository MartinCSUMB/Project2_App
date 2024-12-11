package com.example.project2_app.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.project2_app.database.entities.Store;

import java.util.List;

@Dao
public interface StoreDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Store... store);

    @Update
    void update(Store store);

    @Delete
    void delete(Store store);

    @Query("SELECT * FROM " + InventoryManagementDatabase.STORE_TABLE + " ORDER BY storeStreet ASC")
    List<Store> getAllStores();

    @Query("SELECT storeId, storeStreet FROM " + InventoryManagementDatabase.STORE_TABLE + " WHERE storeStreet = :storeStreet")
    Store getStoreByStreet(String storeStreet);

    @Query("SELECT * FROM " + InventoryManagementDatabase.STORE_TABLE + " WHERE storeId = :storeId")
    Store getStoreById(int storeId);
}

package com.example.project2_app.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.project2_app.database.entities.Store;

import java.util.List;

@Dao
public interface StoreDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Store... store);

    @Query("SELECT * FROM " + InventoryManagementDatabase.STORE_TABLE + " ORDER BY storeStreet ASC")
    List<Store> getAllStores();
}

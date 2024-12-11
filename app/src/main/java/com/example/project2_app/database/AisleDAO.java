package com.example.project2_app.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import androidx.room.Update;


import com.example.project2_app.database.entities.Aisle;
import com.example.project2_app.database.entities.Product;

import java.util.List;

@Dao
public interface AisleDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Aisle... aisle);

    @Update
    void update(Aisle aisle);

    @Delete
    void delete(Aisle aisle);

    @Query("SELECT * FROM " + InventoryManagementDatabase.AISLE_TABLE + " ORDER BY name DESC")
    List<Aisle> getAllRecords();

    @Query("SELECT * FROM " + InventoryManagementDatabase.AISLE_TABLE + " ORDER BY name ASC")
    LiveData<List<Aisle>> getAllAisles();

    @Query("SELECT * FROM " + InventoryManagementDatabase.AISLE_TABLE + " WHERE name = :name")
    LiveData<Aisle> getAisleByName(String name);

    @Query("SELECT * FROM " + InventoryManagementDatabase.AISLE_TABLE + " WHERE aisleId = :aisleId")
    LiveData<Aisle> getAisleById(int aisleId);

    @Query("SELECT * FROM " + InventoryManagementDatabase.AISLE_TABLE + " WHERE name= :name")
    Aisle getAisleByNameFuture(String name);

    //new methods
    @Query("SELECT * FROM " + InventoryManagementDatabase.AISLE_TABLE + " WHERE name= :name" + " AND storeId = :storeId")
    Aisle getAisleByNameAndStoreIdFuture(String name, int storeId);

    @Query("SELECT * FROM " + InventoryManagementDatabase.AISLE_TABLE + " WHERE storeId=:storeId")
    List<Aisle> getAllAislesByStoreId(int storeId);

}

package com.example.project2_app.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.project2_app.database.entities.Product;

import java.util.List;

@Dao
public interface ProductDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Product... product);
    @Delete
    void delete(Product product);
    @Query("SELECT * FROM " + InventoryManagementDatabase.PRODUCT_TABLE + " ORDER BY name ASC")
    LiveData<List<Product>> getAllProducts();
    @Query("DELETE FROM " + InventoryManagementDatabase.PRODUCT_TABLE)
    void deleteAll();
    @Query("SELECT * FROM " + InventoryManagementDatabase.PRODUCT_TABLE + " WHERE name = :name")
    LiveData<Product> getProductByName(String name);
    @Query("SELECT * FROM " + InventoryManagementDatabase.PRODUCT_TABLE + " WHERE partNumber = :partNumber")
    LiveData<Product> getProductByPartNumber(int partNumber);
    @Query("SELECT * FROM " + InventoryManagementDatabase.PRODUCT_TABLE + " WHERE productId = :productID")
    LiveData<Product> getProductByID(int productID);
    @Query("SELECT * FROM " + InventoryManagementDatabase.PRODUCT_TABLE + " WHERE aisleId = :aisleId")
    LiveData<Product> getProductByAisleID(int aisleId);
    @Query("UPDATE productTable SET count=:count WHERE productId = :productId")
    void updateCount (int productId, int count);
    @Query("UPDATE productTable SET cost=:cost WHERE productId = :productId")
    void updateCost (int productId, double cost);


}

package com.example.project2_app.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.project2_app.database.entities.User;

import java.util.List;

@Dao
public interface UserDAO {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... user);

    @Query("SELECT * FROM " + InventoryManagementDatabase.USER_TABLE + " ORDER BY username")
    List<User> getAllUsers();

    @Query("DELETE FROM " + InventoryManagementDatabase.USER_TABLE)
    void deleteAll();
    @Query("SELECT * FROM " + InventoryManagementDatabase.USER_TABLE + " WHERE username == :username")
    LiveData<User> getUserByUserName(String username);
    @Query("SELECT * FROM " + InventoryManagementDatabase.USER_TABLE + " WHERE id == :userId")
    LiveData<User> getUserByUserId(int userId);
}

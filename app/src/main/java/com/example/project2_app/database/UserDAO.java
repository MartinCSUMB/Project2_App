package com.example.project2_app.database;

import androidx.room.DAO;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.project2_app.database.entities.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM " + InventoryManagementDatabase.USER_TABLE + "ORDER BY username")
    List<User> getAllUsers();

    @Query("DELETE FROM " + InventoryManagementDatabase.USER_TABLE)
    void deleteAll();
}

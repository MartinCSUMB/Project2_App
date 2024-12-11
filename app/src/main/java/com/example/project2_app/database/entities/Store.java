package com.example.project2_app.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.project2_app.database.InventoryManagementDatabase;

import java.util.Objects;

@Entity(tableName = InventoryManagementDatabase.STORE_TABLE)
public class Store {
    @PrimaryKey(autoGenerate = true)
    private int storeId;

    private String storeStreet;

    public Store(String storeStreet){
        this.storeStreet = storeStreet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store = (Store) o;
        return Objects.equals(storeStreet, store.storeStreet);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(storeStreet);
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreStreet() {
        return storeStreet;
    }

    public void setStoreStreet(String storeStreet) {
        this.storeStreet = storeStreet;
    }
}

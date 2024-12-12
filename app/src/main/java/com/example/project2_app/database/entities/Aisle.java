package com.example.project2_app.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.project2_app.database.InventoryManagementDatabase;

import java.util.Objects;

@Entity(tableName = InventoryManagementDatabase.AISLE_TABLE)
public class Aisle {
    @PrimaryKey(autoGenerate = true)
    private int aisleId;

    private int storeId;
    private String name;

    public Aisle(String name,int storeId) {
        this.name = name;
        this.storeId = storeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aisle aisle = (Aisle) o;
        return Objects.equals(name, aisle.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    public int getAisleId() {
        return aisleId;
    }

    public void setAisleId(int aisleId) {
        this.aisleId = aisleId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
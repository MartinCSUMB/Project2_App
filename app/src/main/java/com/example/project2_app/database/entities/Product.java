package com.example.project2_app.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import com.example.project2_app.database.InventoryManagementDatabase;
import java.util.Objects;

@Entity(tableName = InventoryManagementDatabase.PRODUCT_TABLE)
public class Product {

    @PrimaryKey(autoGenerate = true)
    private int productId;

    private int aisleId;

    private int storeId;

    private String name;

    private double cost;

    private int partNumber;

    private int count;

    private boolean isBookmarked;

    public Product(int aisleId, String name, double cost, int partNumber, int count, int storeId){
        this.aisleId = aisleId;
        this.storeId = storeId;
        this.name = name;
        this.cost = cost;
        this.partNumber = partNumber;
        this.count = count;
        this.isBookmarked = false;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return productId == product.productId && cost == product.cost && partNumber == product.partNumber && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, name, cost, partNumber);
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getAisleId() {
        return aisleId;
    }

    public void setAisleId(int aisleId) {
        this.aisleId = aisleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getPartNumber() {
        return partNumber;
    }
    public void setPartNumber(int partNumber) {
        this.partNumber = partNumber;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

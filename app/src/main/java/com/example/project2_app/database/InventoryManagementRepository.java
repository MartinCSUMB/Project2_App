package com.example.project2_app.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.project2_app.AdminActivity;
import com.example.project2_app.database.entities.Aisle;
import com.example.project2_app.database.entities.Product;
import com.example.project2_app.database.entities.Store;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class InventoryManagementRepository {

    private final ProductDAO productDAO;
    private final AisleDAO aisleDAO;
    private StoreDAO storeDAO;
    private LiveData<List<Product>> allProducts;
    private LiveData<List<Aisle>> allAisles;
    private List<Store> allStores;
    private static InventoryManagementRepository repository;
    private InventoryManagementRepository(Application application){
        InventoryManagementDatabase db  = InventoryManagementDatabase.getDatabase(application);
        productDAO = db.productDAO();
        aisleDAO = db.aisleDAO();
        storeDAO = db.storeDAO();

        allProducts = productDAO.getAllProducts();
        allAisles = aisleDAO.getAllAisles();
        allStores = storeDAO.getAllStores();
    }

    //product methods
    public LiveData<List<Product>> getAllProducts(){
        return allProducts;
    }
    public void insertProduct(Product product){
        InventoryManagementDatabase.databaseWriteExecutor.execute(() -> {
            productDAO.insert(product);
        });
    }

    public LiveData<Product> getProductByName(String name){
        return productDAO.getProductByName(name);
    }

    public LiveData<Product> getProductByPartNumber(int partNumber){
        return productDAO.getProductByPartNumber(partNumber);
    }

    public LiveData<Product> getProductByID(int id){
        return productDAO.getProductByID(id);
    }

    public LiveData<Product> getProductByAisleID(int aisleId){
        return productDAO.getProductByAisleID(aisleId);
    }

    public void updateProductCount(int productId, int count){
        InventoryManagementDatabase.databaseWriteExecutor.execute(() ->{
            productDAO.updateCount(productId, count);
        });
    }

    public void updateProductCost(int productId, double cost){
        InventoryManagementDatabase.databaseWriteExecutor.execute(() ->{
            productDAO.updateCost(productId, cost);
        });
    }

    //Aisle methods
    public void insertAisle(Aisle aisle){
        InventoryManagementDatabase.databaseWriteExecutor.execute(() ->{
            aisleDAO.insert(aisle);
        });
    }

    public void deleteAisle(Aisle aisle){
        InventoryManagementDatabase.databaseWriteExecutor.execute(()->{
            aisleDAO.delete(aisle);
        });
    }

    public LiveData<List<Aisle>> getAllAisles(){
        return allAisles;
    }

    public LiveData<Aisle> getAisleByName(String name){
        return aisleDAO.getAisleByName(name);
    }

    public LiveData<Aisle> getAisleById(int aisleId){
        return aisleDAO.getAisleById(aisleId);
    }

    public static InventoryManagementRepository getRepository(Application application) {
        if(repository != null){
            return repository;
        }
        Future<InventoryManagementRepository> future = InventoryManagementDatabase.databaseWriteExecutor.submit(
                new Callable<InventoryManagementRepository>(){
                @Override
                public InventoryManagementRepository call() throws Exception{
                    return new InventoryManagementRepository(application);
            }                                                                                                                        }
        );
        try{
            return future.get();
        }catch (InterruptedException | ExecutionException e){
            Log.d(AdminActivity.TAG, "Problem with repo, thread error");
        }
        return null;
    }

    //Store Methods
    public void insertStore(Store... store){
        InventoryManagementDatabase.databaseWriteExecutor.execute(() ->{
            storeDAO.insert(store);
        });
    }

    public List<Store> getAllStores(){
        return allStores;
    }

}

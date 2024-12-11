package com.example.project2_app.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.project2_app.AdminActivity;
import com.example.project2_app.database.entities.Aisle;
import com.example.project2_app.database.entities.Product;
import com.example.project2_app.database.entities.Store;

import java.util.ArrayList;
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
    private ArrayList<Aisle> allAisleArrayList;
    private static InventoryManagementRepository repository;
    private InventoryManagementRepository(Application application){
        InventoryManagementDatabase db  = InventoryManagementDatabase.getDatabase(application);
        productDAO = db.productDAO();
        aisleDAO = db.aisleDAO();
        storeDAO = db.storeDAO();

        allProducts = productDAO.getAllProducts();

        this.allAisleArrayList = (ArrayList<Aisle>) this.aisleDAO.getAllRecords();

        allAisles = aisleDAO.getAllAisles();
        allStores = storeDAO.getAllStores();
    }

    //product methods
    public List<Product> getProductListByAisleId(int aisleId){
        Future<List<Product>> future = InventoryManagementDatabase.databaseWriteExecutor.submit(
                new Callable<List<Product>>(){
                    @Override
                    public List<Product> call() throws Exception{
                        return productDAO.getProductListByAisleId(aisleId);
                    }                                                                                                                        }
        );
        try{
            return future.get();
        }catch (InterruptedException | ExecutionException e){
            Log.d(AdminActivity.TAG, "problem getting list of products with aisle id");
        }
        return null;
    }
    public void updateIsBookMarkedByName(boolean isBookMarked, String name){
        InventoryManagementDatabase.databaseWriteExecutor.execute(() ->{
            productDAO.updateIsBookMarkedByName(isBookMarked,name);
        });
    }
    public List<Product> getAllProductsFuture() {
        Future<ArrayList<Product>> future = InventoryManagementDatabase.databaseWriteExecutor.submit(
                new Callable<ArrayList<Product>>() {
                    @Override
                    public ArrayList<Product> call() throws Exception {
                        return (ArrayList<Product>) productDAO.getAllProductsFuture();
                    }
                });
        try{
            return future.get();
        }catch(InterruptedException | ExecutionException e){
            e.printStackTrace();
        }
        return null;
    }

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
    public void  updatePartNumberById(int productId, int partNumber){
        InventoryManagementDatabase.databaseWriteExecutor.execute(()->{
            productDAO.updatePartNumberById(productId, partNumber);
        });
    }
    public void  updateProductAisleIdById(int productId, int aisleID){
        InventoryManagementDatabase.databaseWriteExecutor.execute(()->{
            productDAO.updateProductAisleIdById(productId, aisleID);
        });
    }

    public void updateCountByName(String name, int count){
        InventoryManagementDatabase.databaseWriteExecutor.execute(() ->{
            productDAO.updateCountByName(name, count);
        });
    }

    public int getCountOfMatchingProductNames(String name){
       return productDAO.getCountOfMatchingProductNames(name);
    }

    public void deleteProduct(Product product){
        InventoryManagementDatabase.databaseWriteExecutor.execute(()->{
            productDAO.delete(product);
        });
    }

    public Product getProductByNameFuture(String name) {
        Future<Product> future = InventoryManagementDatabase.databaseWriteExecutor.submit(
                new Callable<Product>(){
                    @Override
                    public Product call() throws Exception{
                        return productDAO.getProductByNameFuture(name) ;
                    }                                                                                                                        }
        );
        try{
            return future.get();
        }catch (InterruptedException | ExecutionException e){
            Log.d(AdminActivity.TAG, "Problem getting name out of");
        }
        return null;
    }

    public Product getProductByNameAndStoreIdFuture(String name, int storeId) {
        Future<Product> future = InventoryManagementDatabase.databaseWriteExecutor.submit(
                new Callable<Product>(){
                    @Override
                    public Product call() throws Exception{
                        return productDAO.getProductByNameAndStoreFuture(name,storeId) ;
                    }                                                                                                                        }
        );
        try{
            return future.get();
        }catch (InterruptedException | ExecutionException e){
            Log.d(AdminActivity.TAG, "Problem getting name out of");
        }
        return null;
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

    public List<Aisle> getAllAislesByStoreId(int storeId){
        Future<List<Aisle>> future = InventoryManagementDatabase.databaseWriteExecutor.submit(
                new Callable<List<Aisle>>(){
                    @Override
                    public List<Aisle> call() throws Exception{
                        return aisleDAO.getAllAislesByStoreId(storeId);
                    }                                                                                                                        }
        );
        try{
            return future.get();
        }catch (InterruptedException | ExecutionException e){
            Log.d(AdminActivity.TAG, "problem getting list of products with aisle id");
        }
        return null;
    }

    public LiveData<List<Aisle>> getAllAisles(){
        return allAisles;
    }

    public ArrayList<Aisle> getAllAislesFuture() {
        Future<ArrayList<Aisle>> future = InventoryManagementDatabase.databaseWriteExecutor.submit(
                new Callable<ArrayList<Aisle>>() {
                    @Override
                    public ArrayList<Aisle> call() throws Exception {
                        return (ArrayList<Aisle>) aisleDAO.getAllRecords();
                    }
                });
        try{
            return future.get();
        }catch(InterruptedException | ExecutionException e){
            e.printStackTrace();
        }
        return null;
    }

    public Aisle getAisleByNameAndStoreIdFuture(String name, int storeId) {
        Future<Aisle> future = InventoryManagementDatabase.databaseWriteExecutor.submit(
                new Callable<Aisle>(){
                    @Override
                    public Aisle call() throws Exception{
                        return aisleDAO.getAisleByNameAndStoreIdFuture(name,storeId);
                    }                                                                                                                        }
        );
        try{
            return future.get();
        }catch (InterruptedException | ExecutionException e){
            Log.d(AdminActivity.TAG, "Problem getting name out of");
        }
        return null;
    }

    public LiveData<Aisle> getAisleByName(String name){
        return aisleDAO.getAisleByName(name);
    }

    public LiveData<Aisle> getAisleById(int aisleId){
        return aisleDAO.getAisleById(aisleId);
    }

    public Aisle getAisleByNameFuture(String name) {
        Future<Aisle> future = InventoryManagementDatabase.databaseWriteExecutor.submit(
                new Callable<Aisle>(){
                    @Override
                    public Aisle call() throws Exception{
                        return aisleDAO.getAisleByNameFuture(name) ;
                    }                                                                                                                        }
        );
        try{
            return future.get();
        }catch (InterruptedException | ExecutionException e){
            Log.d(AdminActivity.TAG, "Problem getting name out of");
        }
        return null;
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

    //bookmark methods

    public LiveData<List<Product>> getBookmarkedItems() {
        return productDAO.getBookmarkedItems();
    }

}

package com.example.project2_app.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.project2_app.AdminActivity;
import com.example.project2_app.database.entities.Aisle;
import com.example.project2_app.database.entities.Product;
import com.example.project2_app.database.entities.Store;
import com.example.project2_app.database.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class InventoryManagementRepository {

    private final ProductDAO productDAO;
    private final AisleDAO aisleDAO;
    private final UserDAO userDAO;
    private final StoreDAO storeDAO;

    private LiveData<List<Product>> allProducts;
    private LiveData<List<Aisle>> allAisles;
    private List<Store> allStores;
    List<Product> products;
    private static InventoryManagementRepository repository;

    public InventoryManagementRepository(Application application) {
        InventoryManagementDatabase db = InventoryManagementDatabase.getDatabase(application);

        productDAO = db.productDAO();
        aisleDAO = db.aisleDAO();
        storeDAO = db.storeDAO();
        userDAO = db.userDAO();

        allProducts = productDAO.getAllProducts();
        allAisles = aisleDAO.getAllAisles();
        allStores = storeDAO.getAllStores();
        products = productDAO.getProducts();
    }

    public static InventoryManagementRepository getRepository(Application application) {
        if (repository != null) {
            return repository;
        }
        Future<InventoryManagementRepository> future = InventoryManagementDatabase.databaseWriteExecutor.submit(
                new Callable<InventoryManagementRepository>() {
                    @Override
                    public InventoryManagementRepository call() throws Exception {
                        return new InventoryManagementRepository(application);
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d(AdminActivity.TAG, "Problem with repo, thread error");
        }
        return null;
    }

    // User Methods
    public void insertUser(User user) {
        InventoryManagementDatabase.databaseWriteExecutor.execute(() -> userDAO.insert(user));
    }

    public User getUserByUsername(String username) {
        Callable<User> callable = () -> userDAO.getUserByUserName(username).getValue();
        Future<User> future = InventoryManagementDatabase.databaseWriteExecutor.submit(callable);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e(AdminActivity.TAG, "Error retrieving user by username", e);
        }
        return null;
    }

    // Product Methods
    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public void insertProduct(Product product) {
        InventoryManagementDatabase.databaseWriteExecutor.execute(() -> {
            productDAO.insert(product);
        });
    }

    public LiveData<Product> getProductByName(String name) {
        return productDAO.getProductByName(name);
    }

    public LiveData<Product> getProductByPartNumber(int partNumber) {
        return productDAO.getProductByPartNumber(partNumber);
    }

    public LiveData<Product> getProductByID(int id) {
        return productDAO.getProductByID(id);
    }

    public LiveData<Product> getProductByAisleID(int aisleId) {
        return productDAO.getProductByAisleID(aisleId);
    }

    public void updateProductCount(int productId, int count) {
        InventoryManagementDatabase.databaseWriteExecutor.execute(() -> {
            productDAO.updateCount(productId, count);
        });
    }

    public void updateProductCost(int productId, double cost) {
        InventoryManagementDatabase.databaseWriteExecutor.execute(() -> {
            productDAO.updateCost(productId, cost);
        });
    }

    // Aisle Methods
    public void insertAisle(Aisle aisle) {
        InventoryManagementDatabase.databaseWriteExecutor.execute(() -> {
            aisleDAO.insert(aisle);
        });
    }

    public void deleteAisle(Aisle aisle) {
        InventoryManagementDatabase.databaseWriteExecutor.execute(() -> {
            aisleDAO.delete(aisle);
        });
    }

    public LiveData<List<Aisle>> getAllAisles() {
        return allAisles;
    }

    public LiveData<Aisle> getAisleByName(String name) {
        return aisleDAO.getAisleByName(name);
    }

    public LiveData<Aisle> getAisleById(int aisleId) {
        return aisleDAO.getAisleById(aisleId);
    }

    // Store Methods
    public void insertStore(Store... store) {
        InventoryManagementDatabase.databaseWriteExecutor.execute(() -> {
            storeDAO.insert(store);
        });
    }

    public List<Store> getAllStores() {
        return allStores;
    }

    // Bookmark Methods
    public LiveData<List<Product>> getBookmarkedItems() {
        return productDAO.getBookmarkedItems();
    }
    public LiveData<User> getUserByUserName(String username) {
        return userDAO.getUserByUserName(username);
    }
    public LiveData<User> getUserByUserId(int loggedInUserId) {
        return userDAO.getUserByUserId(loggedInUserId);
    }
    public void updateStoreSelected(int userId, String storeSelected) {
        InventoryManagementDatabase.databaseWriteExecutor.execute(() -> {
            userDAO.updateStoreSelected(userId, storeSelected);
        });
    }
    public void updateUser(User user) {
        InventoryManagementDatabase.databaseWriteExecutor.execute(() -> userDAO.update(user));
    }

    //methods joe added
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

    public void deleteProduct(Product product){
        InventoryManagementDatabase.databaseWriteExecutor.execute(()->{
            productDAO.delete(product);
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

    public ArrayList<Aisle> getAllAislesListFuture() {
        Future<ArrayList<Aisle>> future = InventoryManagementDatabase.databaseWriteExecutor.submit(
                new Callable<ArrayList<Aisle>>() {
                    @Override
                    public ArrayList<Aisle> call() throws Exception {
                        return (ArrayList<Aisle>) aisleDAO.getAllAislesListFuture();
                    }
                });
        try{
            return future.get();
        }catch(InterruptedException | ExecutionException e){
            e.printStackTrace();
        }
        return null;
    }

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

    //krithika

    public void updateBookmark(int productId, boolean isBookmarked) {
        InventoryManagementDatabase.databaseWriteExecutor.execute(() -> {
            productDAO.updateBookmark(productId, isBookmarked);
        });
    }

    public List<Product> getProducts() {
        return products;
    }
}

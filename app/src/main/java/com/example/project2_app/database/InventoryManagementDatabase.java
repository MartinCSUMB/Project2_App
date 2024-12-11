package com.example.project2_app.database;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.room.Database;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.project2_app.AdminActivity;
import com.example.project2_app.database.entities.Aisle;
import com.example.project2_app.database.entities.Product;
import com.example.project2_app.database.entities.Store;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



@Database(entities = {Product.class, Aisle.class, Store.class}, version = 1, exportSchema = false)
public abstract class InventoryManagementDatabase extends RoomDatabase {

    public abstract ProductDAO productDAO();
    public abstract AisleDAO aisleDAO();
    public abstract StoreDAO storeDAO();
    public static final String PRODUCT_TABLE = "productTable";
    public static final String AISLE_TABLE = "aisleTable";
    public static final String STORE_TABLE = "storeTable";
    public static final String USER_TABLE = "userTable";
    private final static String DATABASE_NAME = "InventoryManagementDatabase";
    private static volatile InventoryManagementDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static InventoryManagementDatabase getDatabase(final Context context) {
       if(INSTANCE == null){
           synchronized (InventoryManagementDatabase.class){
               if(INSTANCE == null){
                   INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    InventoryManagementDatabase.class,
                                    DATABASE_NAME
                           )
                           .addCallback(addDefaultValues)
                           .build();
                   //had to add this to get db to open in inspector (this is different from the gymlog videos)
                    SupportSQLiteDatabase sdb = INSTANCE.getOpenHelper().getWritableDatabase();
               }
           }
       }
       return  INSTANCE;
    }

    private static final RoomDatabase.Callback addDefaultValues = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);
            Log.i(AdminActivity.TAG, "DATABASE CREATED");
            databaseWriteExecutor.execute(()-> {
                ProductDAO pdao = INSTANCE.productDAO();
                pdao.deleteAll();
                Product testProduct = new Product(1, "test", 1.0, 1234, 1,1);
                pdao.insert(testProduct);
                AisleDAO adao = INSTANCE.aisleDAO();
                Aisle testAisle = new Aisle("bathroom",1);
                adao.insert(testAisle);
                Product testProduct2 = new Product(1,"toiler paper", 5.00, 2342990, 2,1);
                pdao.insert(testProduct2);

                StoreDAO sDao = INSTANCE.storeDAO();
                Store store1 = new Store("College Ave");
                sDao.insert(store1);
                Store store2 = new Store("Murphy Canyon Rd");
                sDao.insert(store2);
                Store store3 = new Store("Dennery Rd");
                sDao.insert(store3);

            });
        }
    };


}

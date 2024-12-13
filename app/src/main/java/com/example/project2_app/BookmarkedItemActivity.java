package com.example.project2_app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2_app.database.InventoryManagementRepository;
import com.example.project2_app.database.entities.Product;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookmarkedItemActivity extends AppCompatActivity {

    private InventoryManagementRepository repository;
    private TextView bookmarkedItemsListTextView;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarked_items);

        bookmarkedItemsListTextView = findViewById(R.id.bookmarkedItemsListTextView);
        Button returnToMenuButton = findViewById(R.id.returnToPrevPage);

        repository = InventoryManagementRepository.getRepository(getApplication());


        executor.execute(() -> {
            List<Product> bookmarkedProducts = repository.getBookmarkedProducts();


            runOnUiThread(() -> displayBookmarkedItems(bookmarkedProducts));
        });

        
        returnToMenuButton.setOnClickListener(v -> finish()); // Finish the activity to return to the previous page
    }

    private void displayBookmarkedItems(List<Product> bookmarkedProducts) {
        StringBuilder sb = new StringBuilder();

        if (bookmarkedProducts.isEmpty()) {
            sb.append("No bookmarked items.");
        } else {
            for (Product product : bookmarkedProducts) {
                sb.append("Product: ").append(product.getName())
                        .append("\nPrice: ").append(product.getCost())
                        .append(" | Quantity: ").append(product.getCount())
                        .append("\n\n");
            }
        }

        bookmarkedItemsListTextView.setText(sb.toString());
    }
}

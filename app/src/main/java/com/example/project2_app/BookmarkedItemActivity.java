package com.example.project2_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2_app.database.InventoryManagementRepository;
import com.example.project2_app.database.entities.Product;

import java.util.List;

public class BookmarkedItemActivity extends AppCompatActivity {

    private InventoryManagementRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarked_items);

        repository = InventoryManagementRepository.getRepository(getApplication());
        TextView bookmarkedItemsListTextView = findViewById(R.id.bookmarkedItemsListTextView);
        Button returnToMenuButton = findViewById(R.id.returnToMenuButton);

        // Populate the list of bookmarked items
        List<Product> bookmarkedProducts = repository.getBookmarkedProducts();
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

        // Handle the return button
        returnToMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(BookmarkedItemActivity.this, GetInventoryActivity.class);
            startActivity(intent);
        });
    }
}

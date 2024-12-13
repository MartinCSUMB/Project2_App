package com.example.project2_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2_app.database.InventoryManagementRepository;
import com.example.project2_app.database.entities.User;
import com.example.project2_app.database.entities.UserAdapter;

import java.util.ArrayList;
import java.util.List;

public class ManageUsersActivity extends AppCompatActivity {

    private InventoryManagementRepository repository;
    private RecyclerView usersRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        repository = InventoryManagementRepository.getRepository(getApplication());
        usersRecyclerView = findViewById(R.id.usersRecyclerView);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ManageUsersActivity.this, AdminActivity.class);
            startActivity(intent);
            finish();
        });

        loadUsers();
    }

    private void loadUsers() {
        // Fetch all users from the database
        List<User> allUsers = repository.getAllUsers();
        int currentAdminUserId = getCurrentAdminUserId(); // Get the current admin's user ID

        // Filter out the current admin
        List<User> filteredUsers = new ArrayList<>();
        for (User user : allUsers) {
            if (user.getId() != currentAdminUserId) { // Exclude current admin by ID
                filteredUsers.add(user);
            }
        }

        if (filteredUsers.isEmpty()) {
            Toast.makeText(this, "No users available to manage.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Pass the filtered list to the adapter
        UserAdapter adapter = new UserAdapter(filteredUsers, user -> openUserOptions(user));
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        usersRecyclerView.setAdapter(adapter);
    }


    private void openUserOptions(User user) {
        String[] options = {
                user.isAdmin() ? "Revoke Admin Privileges" : "Grant Admin Privileges",
                "Delete User"
        };

        new android.app.AlertDialog.Builder(this)
                .setTitle("Manage User: " + user.getUsername())
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            toggleAdminStatus(user);
                            break;
                        case 1:
                            deleteUser(user);
                            break;
                    }
                })
                .show();
    }

    private void toggleAdminStatus(User user) {
        user.setAdmin(!user.isAdmin());
        repository.updateUser(user);
        loadUsers(); // Refresh the RecyclerView
    }

    private void deleteUser(User user) {
        // Delete the user from the database
        repository.deleteUser(user);

        // Refresh the RecyclerView data
        List<User> updatedUsers = repository.getAllUsers(); // Fetch updated user list
        UserAdapter adapter = (UserAdapter) usersRecyclerView.getAdapter();
        if (adapter != null) {
            adapter.updateData(updatedUsers); // Update the adapter with the new data
        }
    }
    private int getCurrentAdminUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFERENCE_USERID_KEY, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(MainActivity.SHARED_PREFERENCE_USERID_KEY, -1); // Default to -1 if not found
    }

    public static Intent manageUsersIntentFactory(Context context) {
        return new Intent(context, ManageUsersActivity.class);
    }

}

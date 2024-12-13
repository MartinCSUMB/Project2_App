package com.example.project2_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.project2_app.database.InventoryManagementRepository;
import com.example.project2_app.database.entities.Product;
import com.example.project2_app.database.entities.User;
import com.example.project2_app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String MAIN_ACTIVITY_USER_ID = "com.example.project2_app.MAIN_ACTIVITY_USER_ID";
    private static final String SAVED_INSTANCE_STATE_USERID_KEY = "com.example.project2_app.SAVED_INSTANCE_STATE_USERID_KEY";

    static final String SHARED_PREFERENCE_USERID_KEY = "com.example.project2_app.SHARED_PREFERENCE_USERID_KEY";
    static final String SHARED_PREFERENCE_USERID_VALUE = "com.example.project2_app.SHARED_PREFERENCE_USERID_VALUE";
    private static final int LOGGED_OUT = -1;
    private ActivityMainBinding binding;
    private InventoryManagementRepository repository;
// hello
    private int id;
    private int result;
    private Product product;

    int loggedInUserId = -1;
    private User user;

    public static Intent mainActivityIntentFactory(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        repository = InventoryManagementRepository.getRepository(getApplication());
        loginUser(savedInstanceState);

        // Check for the password change message
        String message = getIntent().getStringExtra("PASSWORD_CHANGE_MESSAGE");
        if (message != null && !message.isEmpty()) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        if (loggedInUserId == -1) {
            Intent intent = LoginActivity.loginIntentFactory(getApplicationContext());
            startActivity(intent);
        }
        updateSharedPreference();
        toggleAdminButtonVisibility();


        binding.activity1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SearchActivity.searchIntentFactory(getApplicationContext());
                startActivity(intent);

            }
        });

        binding.adminMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdminActivity.adminIntentFactory(getApplicationContext());
                startActivity(intent);

            }
        });




    }

    private void loginUser(Bundle savedInstanceState) {
        // Check shared preferences for logged-in user
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        loggedInUserId = sharedPreferences.getInt(getString(R.string.preference_userId_key), LOGGED_OUT);

        // Check saved instance state for logged-in user
        if (loggedInUserId == LOGGED_OUT && savedInstanceState != null && savedInstanceState.containsKey(getString(R.string.preference_userId_key))) {
            loggedInUserId = savedInstanceState.getInt(getString(R.string.preference_userId_key), LOGGED_OUT);
        }

        // Check intent for logged-in user
        if (loggedInUserId == LOGGED_OUT) {
            loggedInUserId = getIntent().getIntExtra(MAIN_ACTIVITY_USER_ID, LOGGED_OUT);
        }

        if (loggedInUserId == LOGGED_OUT) {
            return;
        }

        LiveData<User> userObserver = repository.getUserByUserId(loggedInUserId);
        userObserver.observe(this, user -> {
            this.user = user;
            if (this.user != null) {
                if (this.user.getStoreSelected() == null || this.user.getStoreSelected().isEmpty()) {
                    // Redirect only if no store is selected
                    Intent intent = StoreActivity.storeIntentFactory(getApplicationContext());
                    startActivity(intent);
                    finish(); // Prevent returning to this activity
                } else {
                    // Store selected, update UI
                    String welcomeMessage = user.isAdmin() ? "Welcome Admin: " + user.getUsername() : "Welcome User: " + user.getUsername();
                    binding.welcomeTextView.setText(welcomeMessage + "\nStore: " + user.getStoreSelected());
                }

                // Handle Admin Menu Button visibility
                toggleAdminButtonVisibility();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("SAVED_INSTANCE_STATE_USERID_KEY", loggedInUserId);
        updateSharedPreference();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settingsMenuItem) {
            // Navigate to SettingsActivity
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.logoutMenuItem) {
            // Show logout dialog
            showLogoutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void showLogoutDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
        final AlertDialog alertDialog = alertBuilder.create();

        alertBuilder.setMessage("Logout?");

        alertBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });

        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertBuilder.create().show();
    }

    private void logout() {

        loggedInUserId = LOGGED_OUT;
        updateSharedPreference();

        getIntent().putExtra(MAIN_ACTIVITY_USER_ID,LOGGED_OUT);

        startActivity(LoginActivity.loginIntentFactory(getApplication()));
    }
    private void updateSharedPreference(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putInt(getString(R.string.preference_userId_key), loggedInUserId);
        sharedPrefEditor.apply();
    }

    private void loginStore(String store) {
        Log.i("DAC_APP", "Main Activity");
    }

    static Intent mainActivityIntentFactory(Context context, int userId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MAIN_ACTIVITY_USER_ID, userId);
        return intent;
    }
    private void toggleAdminButtonVisibility() {
        if (user != null && user.isAdmin()) {
            Log.d("AdminButtonVisibility", "Button set to VISIBLE");
            binding.adminMenuButton.setVisibility(View.VISIBLE);
        } else {
            Log.d("AdminButtonVisibility", "Button set to GONE");
            binding.adminMenuButton.setVisibility(View.GONE);
        }
    }

}
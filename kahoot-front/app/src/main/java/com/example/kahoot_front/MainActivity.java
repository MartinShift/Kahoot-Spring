package com.example.kahoot_front;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.kahoot_front.MainFragment;
import com.example.kahoot_front.service.SharedPrefManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (isUserLoggedIn()) {
            loadFragment(new MainFragment());
        } else {
            loadFragment(new LoginFragment());
        }
    }

    private boolean isUserLoggedIn() {
        return SharedPrefManager.getInstance(this).isLoggedIn();
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isUserLoggedIn()) {
            getMenuInflater().inflate(R.menu.user_menu, menu);
        } else {
            getMenuInflater().inflate(R.menu.guest_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
        if (isUserLoggedIn()) {
            getMenuInflater().inflate(R.menu.user_menu, menu);
        } else {
            getMenuInflater().inflate(R.menu.guest_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.login) {
            loadFragment(new LoginFragment());
            return true;
        }
        if(item.getItemId() == R.id.register) {
            loadFragment(new RegisterFragment());
            return true;
        }
        if(item.getItemId() == R.id.logout) {
            SharedPrefManager.getInstance(this).logout();
            loadFragment(new LoginFragment());
            return true;
        }
        if(item.getItemId() == R.id.profile)
        {
            loadFragment(new ProfileFragment());
            return true;
        }
        if(item.getItemId() == R.id.notes)
        {
            loadFragment(new MainFragment());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
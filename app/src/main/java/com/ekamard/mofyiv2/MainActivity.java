package com.ekamard.mofyiv2;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setVisibility(View.GONE);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        FloatingActionButton btnChangeLanguage = findViewById(R.id.btn_language);
        btnChangeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changeLanguage = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(changeLanguage);
            }
        });

        BottomNavigationView navigationView = findViewById(R.id.navBottomView);
        AppBarConfiguration myAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_movies, R.id.nav_tvShow, R.id.nav_actor
        ).build();

        NavController myNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, myNavController, myAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, myNavController);
    }

}

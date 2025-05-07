package com.example.teleweather;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AppActivity extends AppCompatActivity {
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_app);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // Configuramos NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        // Configurar BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Configurar opciones de navegación para evitar acumular fragmentos en el BackStack
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_location, R.id.nav_pronosticos, R.id.nav_deportes
        ).build();

        // Manejar los eventos de navegación para limpiar el BackStack
        bottomNavigationView.setOnItemSelectedListener(item -> {
            // Limpiar el BackStack al navegar entre los destinos principales
            navController.popBackStack(navController.getGraph().getStartDestinationId(), false);

            int itemId = item.getItemId();
            if (itemId == R.id.action_location) {
                navController.navigate(R.id.nav_location);
                return true;
            } else if (itemId == R.id.action_pronostico) {
                navController.navigate(R.id.nav_pronosticos);
                return true;
            } else if (itemId == R.id.action_deportes) {
                navController.navigate(R.id.nav_deportes);
                return true;
            }
            return false;
        });

        // Escuchar cambios de destino para actualizar el BottomNavigationView al usar el botón atrás
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            Menu menu = bottomNavigationView.getMenu();
            int destId = destination.getId();

            if (destId == R.id.nav_location) {
                menu.findItem(R.id.action_location).setChecked(true);
            } else if (destId == R.id.nav_pronosticos) {
                menu.findItem(R.id.action_pronostico).setChecked(true);
            } else if (destId == R.id.nav_deportes) {
                menu.findItem(R.id.action_deportes).setChecked(true);
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}
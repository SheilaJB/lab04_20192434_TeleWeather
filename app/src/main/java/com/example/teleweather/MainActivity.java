package com.example.teleweather;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button btnEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Inicializar componentes de la interfaz
        btnEnter = findViewById(R.id.btnEnter);

        // Chequear conexión a internet
        if(!comprobarConexion()){
            Toast.makeText(this, "No hay conexión a internet", Toast.LENGTH_SHORT).show();
            mostrarDialog();
        }

        // Ingresar a la aplicación
        btnEnter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (comprobarConexion()) {
                    Intent intent = new Intent(MainActivity.this, AppActivity.class);
                    startActivity(intent);
                } else {
                    mostrarDialog();
                }
            }
        });

    }

    private boolean comprobarConexion() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (capabilities != null) {
                return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            }
        }
        return false;
    }

    private  void mostrarDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sin conexión a Internet")
                .setMessage("Esta aplicación requiere conexión a Internet para funcionar.")
                .setPositiveButton("Configuración", (dialog, which) -> {
                    startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();


    }
}
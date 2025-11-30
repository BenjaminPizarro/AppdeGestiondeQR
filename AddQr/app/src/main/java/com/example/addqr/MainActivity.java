package com.example.addqr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.addqr.data.AssetDAO;
import com.example.addqr.model.Asset;

public class MainActivity extends AppCompatActivity {

    private AssetDAO assetDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assetDAO = new AssetDAO(this);

        Button btnScanQR = findViewById(R.id.btn_scan_qr);
        Button btnAddNewAsset = findViewById(R.id.btn_add_new_asset);
        Button btnViewList = findViewById(R.id.btn_view_list);
        Button btnSettings = findViewById(R.id.btn_settings);
        Button btnAbout = findViewById(R.id.btn_about);

        btnScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScanQRActivity.class);
                startActivity(intent);
            }
        });

        btnAddNewAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewAssetActivity.class);
                startActivity(intent);
            }
        });

        btnViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AssetListActivity.class);
                startActivity(intent);
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
        
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        try {
            if (assetDAO.getAllAssets().isEmpty()) {
                com.example.addqr.model.Asset testAsset = new com.example.addqr.model.Asset(
                    "QR000001",
                    "Laptop XYZ",
                    "Laptop corporativa para desarrollo",
                    "En Uso",
                    "Oficina Central",
                    System.currentTimeMillis()
                );
                assetDAO.insertAsset(testAsset);
                Log.d("MainActivity", "Activo de prueba insertado correctamente.");
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error al inicializar la base de datos", Toast.LENGTH_LONG).show();
            Log.e("MainActivity", "Error de BD: " + e.getMessage());
        }
    }
}
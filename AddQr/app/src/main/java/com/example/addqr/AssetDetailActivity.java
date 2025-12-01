package com.example.addqr;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.addqr.data.AssetDAO;
import com.example.addqr.model.Asset;
import com.example.addqr.model.LocationRecord;
import com.example.addqr.service.LocationService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AssetDetailActivity extends AppCompatActivity implements LocationService.LocationCallback {

    private static final int LOCATION_PERMISSION_CODE = 101;
    private AssetDAO assetDAO;
    private Asset currentAsset;
    private LocationService locationService;
    private int assetId = -1;
    private String qrCode = null;

    private TextView tvName, tvStatus, tvQrCode, tvDescription, tvLocation, tvTimestamp;
    private Button btnMoveAsset, btnViewHistory, btnViewOnMap, btnEditAsset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_detail);

        assetDAO = new AssetDAO(this);
        locationService = new LocationService(this);

        tvName = findViewById(R.id.tv_asset_name);
        tvStatus = findViewById(R.id.tv_asset_status);
        tvQrCode = findViewById(R.id.tv_asset_qr_code);
        tvDescription = findViewById(R.id.tv_asset_description);
        tvLocation = findViewById(R.id.tv_asset_location);
        tvTimestamp = findViewById(R.id.tv_asset_timestamp);
        btnMoveAsset = findViewById(R.id.btn_move_asset);
        btnViewHistory = findViewById(R.id.btn_view_history);
        btnViewOnMap = findViewById(R.id.btn_view_on_map);
        btnEditAsset = findViewById(R.id.btn_edit_asset);

        assetId = getIntent().getIntExtra("ASSET_ID", -1);
        qrCode = getIntent().getStringExtra("QR_CODE");

        loadAssetDetails();

        btnMoveAsset.setOnClickListener(v -> checkLocationPermissionsAndMove());

        btnViewHistory.setOnClickListener(v -> {
            if (currentAsset != null) {
                Intent intent = new Intent(this, LocationHistoryActivity.class);
                intent.putExtra("ASSET_ID", currentAsset.getId());
                startActivity(intent);
            }
        });
        
        btnViewOnMap.setOnClickListener(v -> {
            if (currentAsset != null) {
                Intent intent = new Intent(this, MapDisplayActivity.class);
                intent.putExtra("ASSET_ID", currentAsset.getId());
                startActivity(intent);
            }
        });
        
        btnEditAsset.setOnClickListener(v -> {
             if (currentAsset != null) {
                Intent intent = new Intent(this, EditAssetActivity.class);
                intent.putExtra("ASSET_ID", currentAsset.getId());
                startActivity(intent);
            }
        });
    }

    private void loadAssetDetails() {
        if (assetId != -1) {
            currentAsset = assetDAO.getAssetById(assetId);
        } else if (qrCode != null) {
            currentAsset = assetDAO.getAssetByQrCode(qrCode);
        }

        if (currentAsset == null) {
            if (qrCode != null) {
                Toast.makeText(this, "Activo no encontrado. Creando nuevo registro...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, NewAssetActivity.class);
                intent.putExtra("PREFILL_QR", qrCode);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Activo no válido.", Toast.LENGTH_LONG).show();
                finish();
            }
            return;
        }

        setTitle("Detalle: " + currentAsset.getName());
        tvName.setText(currentAsset.getName());
        tvStatus.setText(currentAsset.getStatus());
        tvQrCode.setText(currentAsset.getQrCode());
        tvDescription.setText(currentAsset.getDescription());
        tvLocation.setText(currentAsset.getLastLocation());
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String date = sdf.format(new Date(currentAsset.getCreationTimestamp()));
        tvTimestamp.setText("Fecha de Registro: " + date);
    }
    
    private void checkLocationPermissionsAndMove() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 
                    LOCATION_PERMISSION_CODE);
        } else {
            Toast.makeText(this, "Obteniendo ubicación actual...", Toast.LENGTH_SHORT).show();
            locationService.requestCurrentLocation(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationService.requestCurrentLocation(this);
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado. No se puede registrar la posición GPS.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onLocationResult(Location location) {
        if (currentAsset == null) return;
        
        LocationRecord record = new LocationRecord(
                currentAsset.getId(),
                location.getLatitude(),
                location.getLongitude(),
                System.currentTimeMillis()
        );
        assetDAO.addLocationRecord(record);
        
        currentAsset.setLastLocation(String.format(Locale.getDefault(), 
                "GPS: %.4f, %.4f", location.getLatitude(), location.getLongitude()));
        currentAsset.setStatus("En Uso (Ubicación Actualizada)");
        
        assetDAO.updateAsset(currentAsset);
        
        Toast.makeText(this, "Ubicación GPS registrada y activo actualizado.", Toast.LENGTH_LONG).show();
        loadAssetDetails();
    }

    @Override
    public void onLocationFailed(String error) {
        Toast.makeText(this, "Error al obtener GPS: " + error, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAssetDetails();
    }
}
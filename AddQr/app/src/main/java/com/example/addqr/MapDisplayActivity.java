package com.example.addqr;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.addqr.data.AssetDAO;
import com.example.addqr.model.Asset;
import com.example.addqr.model.LocationRecord;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class MapDisplayActivity extends AppCompatActivity implements OnMapReadyCallback {

    private AssetDAO assetDAO;
    private TextView tvAssetInfo, tvApiStatus;
    private int assetId;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_display);
        setTitle("Ubicación en Mapa (API)");

        assetDAO = new AssetDAO(this);
        tvAssetInfo = findViewById(R.id.tv_asset_map_info);
        tvApiStatus = findViewById(R.id.tv_api_status);

        assetId = getIntent().getIntExtra("ASSET_ID", -1);

        checkApiKeyStatus();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        if (assetId != -1) {
            loadMapAndLocation();
        } else {
            // Si no hay ID de activo, centrar en una ubicación predeterminada o mostrar un mensaje
            LatLng defaultLocation = new LatLng(37.4220, -122.0840); // GooglePlex
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f));
            tvApiStatus.setText("Mostrando ubicación predeterminada");
            tvAssetInfo.setText("No se ha seleccionado ningún activo. Selecciona uno desde la lista para ver su ubicación.");
        }
    }
    
    private void checkApiKeyStatus() {
        SharedPreferences prefs = getSharedPreferences(SettingsActivity.PREFS_NAME, MODE_PRIVATE);
        String apiKey = prefs.getString(SettingsActivity.API_KEY_MAPS, "");
        
        if (apiKey.isEmpty() || apiKey.equals("TU_CLAVE_DE_API_AQUI")) {
            tvApiStatus.setText("🔴 Clave de API de Mapas Faltante. Ve a Configuración.");
            Log.w("MapDisplayActivity", "API Key is missing or default.");
        } else {
            tvApiStatus.setText("🟢 Clave de API de Mapas Cargada.");
            Log.i("MapDisplayActivity", "API Key successfully loaded.");
        }
    }

    private void loadMapAndLocation() {
        Asset asset = assetDAO.getAssetById(assetId);
        if (asset == null) return;

        List<LocationRecord> history = assetDAO.getLocationHistory(assetId);

        if (!history.isEmpty()) {
            LocationRecord latestRecord = history.get(0);
            LatLng assetLocation = new LatLng(latestRecord.getLatitude(), latestRecord.getLongitude());

            googleMap.clear(); // Limpiar marcadores anteriores
            googleMap.addMarker(new MarkerOptions().position(assetLocation).title(asset.getName()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(assetLocation, 15f));

            String info = String.format(Locale.getDefault(),
                    "Activo: %s\nLat: %.4f, Lon: %.4f",
                    asset.getName(), latestRecord.getLatitude(), latestRecord.getLongitude());
            tvAssetInfo.setText(info);
            tvApiStatus.setText("Mostrando última ubicación registrada");

        } else {
            tvAssetInfo.setText(asset.getName() + "\nNo hay coordenadas GPS registradas aún.");
            tvApiStatus.setText("Sin historial de GPS");
            // Opcional: mover el mapa a una ubicación por defecto
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.4220, -122.0840), 10f));
        }
    }
}
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

import java.util.List;
import java.util.Locale;

public class MapDisplayActivity extends AppCompatActivity {

    private AssetDAO assetDAO;
    private TextView tvAssetInfo, tvApiStatus;
    private int assetId;

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

        if (assetId != -1) {
            loadMapAndLocation();
        } else {
            Toast.makeText(this, "Activo no especificado.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    
    private void checkApiKeyStatus() {
        SharedPreferences prefs = getSharedPreferences(SettingsActivity.PREFS_NAME, MODE_PRIVATE);
        String apiKey = prefs.getString(SettingsActivity.API_KEY_MAPS, "");
        
        if (apiKey.isEmpty()) {
            tvApiStatus.setText("🔴 Clave de API de Mapas Faltante. Ve a Configuración.");
            tvApiStatus.setBackgroundColor(0xFFFFCCCC);
            Log.w("MapDisplayActivity", "API Key is missing.");
        } else {
            tvApiStatus.setText("🟢 Clave de API de Mapas Cargada.");
            tvApiStatus.setBackgroundColor(0xFFCCFFCC);
            Log.i("MapDisplayActivity", "API Key successfully loaded.");
        }
    }

    private void loadMapAndLocation() {
        Asset asset = assetDAO.getAssetById(assetId);
        List<LocationRecord> history = assetDAO.getLocationHistory(assetId);

        if (asset == null) return;

        if (!history.isEmpty()) {
            LocationRecord latestRecord = history.get(0);
            
            String info = String.format(Locale.getDefault(),
                    "Activo: %s\nÚltima Posición: %s\nLatitud: %.4f\nLongitud: %.4f",
                    asset.getName(), asset.getLastLocation(), latestRecord.getLatitude(), latestRecord.getLongitude());
            
            tvAssetInfo.setText(info);

            Toast.makeText(this, "Simulando marcador en el mapa.", Toast.LENGTH_SHORT).show();
        } else {
            tvAssetInfo.setText(asset.getName() + "\nNo hay coordenadas GPS registradas aún.");
        }
    }
}
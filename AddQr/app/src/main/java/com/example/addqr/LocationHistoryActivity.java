package com.example.addqr;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.addqr.data.AssetDAO;
import com.example.addqr.model.Asset;
import com.example.addqr.model.LocationRecord;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class LocationHistoryActivity extends AppCompatActivity {

    private AssetDAO assetDAO;
    private ListView listViewHistory;
    private TextView tvAssetTitle;
    private int assetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_history);
        
        tvAssetTitle = findViewById(R.id.tv_history_asset_title);
        listViewHistory = findViewById(R.id.list_view_history);
        assetDAO = new AssetDAO(this);

        assetId = getIntent().getIntExtra("ASSET_ID", -1);

        if (assetId != -1) {
            loadHistory();
        } else {
            Toast.makeText(this, "Error: ID de activo no proporcionado.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadHistory() {
        Asset asset = assetDAO.getAssetById(assetId);
        if (asset != null) {
            tvAssetTitle.setText("Historial de Ubicación de: " + asset.getName());
            setTitle("Historial GPS");
        } else {
            tvAssetTitle.setText("Historial de Activo Desconocido");
        }

        List<LocationRecord> history = assetDAO.getLocationHistory(assetId);

        if (history.isEmpty()) {
            Toast.makeText(this, "No hay registros de ubicación GPS para este activo.", Toast.LENGTH_LONG).show();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());

        List<String> historyEntries = history.stream()
                .map(record -> {
                    String date = sdf.format(new Date(record.getTimestamp()));
                    return String.format(Locale.getDefault(),
                            "%s - Lat: %.4f, Lon: %.4f",
                            date, record.getLatitude(), record.getLongitude());
                })
                .collect(Collectors.toList());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                historyEntries
        );
        listViewHistory.setAdapter(adapter);
    }
}
package com.example.addqr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.addqr.data.AssetDAO;
import com.example.addqr.model.Asset;

import java.util.List;
import java.util.stream.Collectors;

public class AssetListActivity extends AppCompatActivity {

    private AssetDAO assetDAO;
    private ListView listViewAssets;
    private List<Asset> assets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_list);

        setTitle("Inventario de Activos");

        assetDAO = new AssetDAO(this);
        listViewAssets = findViewById(R.id.list_view_assets);
        
        findViewById(R.id.fab_add_asset).setOnClickListener(v -> {
            Intent intent = new Intent(AssetListActivity.this, NewAssetActivity.class);
            startActivity(intent);
        });


        loadAssets();

        listViewAssets.setOnItemClickListener((parent, view, position, id) -> {
            Asset selectedAsset = assets.get(position);
            Intent intent = new Intent(AssetListActivity.this, AssetDetailActivity.class);
            intent.putExtra("ASSET_ID", selectedAsset.getId());
            startActivity(intent);
        });
    }

    private void loadAssets() {
        assets = assetDAO.getAllAssets();
        if (assets.isEmpty()) {
            Toast.makeText(this, "No hay activos registrados.", Toast.LENGTH_LONG).show();
            return;
        }

        List<String> assetNames = assets.stream()
                .map(a -> a.getName() + " (" + a.getStatus() + ")")
                .collect(Collectors.toList());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                assetNames
        );
        listViewAssets.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAssets();
    }
}
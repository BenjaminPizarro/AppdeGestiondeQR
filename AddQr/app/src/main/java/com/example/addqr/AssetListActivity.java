package com.example.addqr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.addqr.data.AssetDAO;
import com.example.addqr.model.Asset;

import java.util.List;

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
            listViewAssets.setAdapter(null); // Limpiar la lista si no hay activos
            return;
        }

        AssetAdapter adapter = new AssetAdapter(this, assets);
        listViewAssets.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAssets();
    }

    /**
     * Adaptador personalizado para mostrar los activos en la lista.
     */
    private class AssetAdapter extends ArrayAdapter<Asset> {

        public AssetAdapter(@NonNull Context context, @NonNull List<Asset> assets) {
            super(context, 0, assets);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            // Obtener el activo para esta posición
            Asset asset = getItem(position);

            // Reutilizar vistas si es posible (para un rendimiento óptimo)
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_asset, parent, false);
            }

            // Referencias a las vistas del layout del item
            TextView tvName = convertView.findViewById(R.id.text_asset_name);
            TextView tvDetails = convertView.findViewById(R.id.text_asset_details);
            TextView tvStatus = convertView.findViewById(R.id.text_asset_status);

            // Poblar las vistas con los datos del activo
            if (asset != null) {
                tvName.setText(asset.getName());
                tvDetails.setText("Ubicación: " + asset.getLastLocation() + " | Código: " + asset.getQrCode());
                tvStatus.setText("Estado: " + asset.getStatus());
            }

            return convertView;
        }
    }
}
package com.example.addqr;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.addqr.data.AssetDAO;
import com.example.addqr.model.Asset;

public class EditAssetActivity extends AppCompatActivity {

    private EditText editName, editDescription, editStatus, editLocation;
    private TextView tvQrCode;
    private Button btnUpdateAsset, btnDeleteAsset;
    private AssetDAO assetDAO;
    private Asset currentAsset;
    private int assetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_asset);

        assetDAO = new AssetDAO(this);
        assetId = getIntent().getIntExtra("ASSET_ID", -1);

        tvQrCode = findViewById(R.id.tv_edit_qr_code);
        editName = findViewById(R.id.edit_edit_name);
        editDescription = findViewById(R.id.edit_edit_description);
        editStatus = findViewById(R.id.edit_edit_status);
        editLocation = findViewById(R.id.edit_edit_location);
        btnUpdateAsset = findViewById(R.id.btn_update_asset);
        btnDeleteAsset = findViewById(R.id.btn_delete_asset);

        loadAssetData();

        btnUpdateAsset.setOnClickListener(v -> updateAsset());
        btnDeleteAsset.setOnClickListener(v -> deleteAsset());
    }

    private void loadAssetData() {
        if (assetId != -1) {
            currentAsset = assetDAO.getAssetById(assetId);
        }

        if (currentAsset == null) {
            Toast.makeText(this, "Error: Activo no encontrado.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        setTitle("Editar: " + currentAsset.getName());
        tvQrCode.setText(currentAsset.getQrCode());
        editName.setText(currentAsset.getName());
        editDescription.setText(currentAsset.getDescription());
        editStatus.setText(currentAsset.getStatus());
        editLocation.setText(currentAsset.getLastLocation());
    }

    private void updateAsset() {
        if (currentAsset == null) return;

        String name = editName.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String status = editStatus.getText().toString().trim();
        String location = editLocation.getText().toString().trim();

        if (name.isEmpty() || status.isEmpty()) {
            Toast.makeText(this, "Nombre y Estado son obligatorios.", Toast.LENGTH_SHORT).show();
            return;
        }

        currentAsset.setName(name);
        currentAsset.setDescription(description);
        currentAsset.setStatus(status);
        currentAsset.setLastLocation(location);

        int rows = assetDAO.updateAsset(currentAsset);

        if (rows > 0) {
            Toast.makeText(this, "Activo actualizado con éxito.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Error al actualizar el activo.", Toast.LENGTH_LONG).show();
        }
    }

    private void deleteAsset() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Confirmar Eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar este activo y todo su historial de ubicaciones?")
                .setPositiveButton("Sí, Eliminar", (dialog, which) -> {
                    int rows = assetDAO.deleteAsset(assetId);
                    if (rows > 0) {
                        Toast.makeText(EditAssetActivity.this, "Activo eliminado permanentemente.", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(EditAssetActivity.this, "Error al eliminar.", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
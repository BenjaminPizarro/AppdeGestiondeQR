package com.example.addqr;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.addqr.data.AssetDAO;
import com.example.addqr.model.Asset;

import java.util.UUID;

public class NewAssetActivity extends AppCompatActivity {

    private EditText editQrCode, editName, editDescription, editStatus, editLocation;
    private Button btnSaveAsset;
    private AssetDAO assetDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_asset);
        setTitle("Registrar Nuevo Activo");

        assetDAO = new AssetDAO(this);

        editQrCode = findViewById(R.id.edit_qr_code);
        editName = findViewById(R.id.edit_name);
        editDescription = findViewById(R.id.edit_description);
        editStatus = findViewById(R.id.edit_status);
        editLocation = findViewById(R.id.edit_location);
        btnSaveAsset = findViewById(R.id.btn_save_asset);

        String prefilledQr = getIntent().getStringExtra("PREFILL_QR");
        if (prefilledQr != null && !prefilledQr.isEmpty()) {
            editQrCode.setText(prefilledQr);
        } else {
            editQrCode.setText(UUID.randomUUID().toString().substring(0, 15));
        }

        btnSaveAsset.setOnClickListener(v -> saveAsset());
    }

    private void saveAsset() {
        String qrCode = editQrCode.getText().toString().trim();
        String name = editName.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String status = editStatus.getText().toString().trim();
        String location = editLocation.getText().toString().trim();

        if (name.isEmpty() || qrCode.isEmpty() || status.isEmpty()) {
            Toast.makeText(this, "El Nombre, Código QR y Estado son obligatorios.", Toast.LENGTH_SHORT).show();
            return;
        }

        Asset newAsset = new Asset(
                qrCode,
                name,
                description,
                status,
                location,
                System.currentTimeMillis()
        );

        long id = assetDAO.insertAsset(newAsset);

        if (id > 0) {
            Toast.makeText(this, "Activo registrado con éxito. ID: " + id, Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Error al registrar el activo. El QR podría estar duplicado.", Toast.LENGTH_LONG).show();
        }
    }
}
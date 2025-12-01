package com.example.addqr;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ScanQRActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final String TAG = "ScanQRActivity";
    private boolean isFlashOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        setTitle("Escanear Código QR");

        Button btnToggleFlash = findViewById(R.id.btn_toggle_flash);
        btnToggleFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFlashOn = !isFlashOn;
                if (isFlashOn) {
                    // Lógica para encender el flash (depende de la librería de cámara)
                    Toast.makeText(ScanQRActivity.this, "Linterna Encendida (simulación)", Toast.LENGTH_SHORT).show();
                } else {
                    // Lógica para apagar el flash
                    Toast.makeText(ScanQRActivity.this, "Linterna Apagada (simulación)", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (checkCameraPermission()) {
            startQRScanner();
        } else {
            requestCameraPermission();
        }
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION_CODE);
    }

    private void startQRScanner() {
        Log.i(TAG, "Escáner iniciado. Simulación en 3 segundos...");
        getWindow().getDecorView().postDelayed(() -> onScanResult("QR000001"), 3000);
    }

    public void onScanResult(String rawResult) {
        Toast.makeText(this, "QR Escaneado: " + rawResult, Toast.LENGTH_LONG).show();
        
        Intent intent = new Intent(this, AssetDetailActivity.class);
        intent.putExtra("QR_CODE", rawResult);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startQRScanner();
            } else {
                Toast.makeText(this, "Permiso de cámara denegado. No se puede escanear.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
package com.example.addqr;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "AssetManagerPrefs";
    public static final String API_KEY_MAPS = "MapsApiKey";

    private EditText editApiKey;
    private Button btnSaveSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Configuración de API");

        editApiKey = findViewById(R.id.edit_api_key);
        btnSaveSettings = findViewById(R.id.btn_save_settings);

        loadApiKey();

        btnSaveSettings.setOnClickListener(v -> saveApiKey());
    }

    private void loadApiKey() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedKey = prefs.getString(API_KEY_MAPS, "");
        editApiKey.setText(savedKey);
    }

    private void saveApiKey() {
        String apiKey = editApiKey.getText().toString().trim();

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(API_KEY_MAPS, apiKey);
        editor.apply();

        Toast.makeText(this, "Clave de API guardada correctamente.", Toast.LENGTH_SHORT).show();
        finish();
    }
}
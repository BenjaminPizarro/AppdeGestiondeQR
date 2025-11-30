package com.example.addqr;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("Acerca De Asset Manager PRO");

        TextView tvVersion = findViewById(R.id.tv_version);
        TextView tvAuthor = findViewById(R.id.tv_author);

        tvVersion.setText(String.format("Versión: %s", "1.0.0 (Build 20251129)"));
        tvAuthor.setText(String.format("Desarrollado por: %s", "[Tu Nombre/Equipo]"));
    }
}
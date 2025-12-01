package com.example.addqr.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.addqr.model.Asset;
import com.example.addqr.model.LocationRecord;

import java.util.ArrayList;
import java.util.List;

public class AssetDAO {
    private static final String TAG = "AssetDAO";
    private SQLiteDatabase database;
    private DbHelper dbHelper;

    public AssetDAO(Context context) {
        dbHelper = new DbHelper(context);
    }

    public void open() {
        try {
            database = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            Log.e(TAG, "Error al abrir la base de datos", e);
        }
    }

    public void close() {
        dbHelper.close();
    }

    public long insertAsset(Asset asset) {
        open();
        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_QR_CODE, asset.getQrCode());
        values.put(DbHelper.COLUMN_NAME, asset.getName());
        values.put(DbHelper.COLUMN_DESCRIPTION, asset.getDescription());
        values.put(DbHelper.COLUMN_STATUS, asset.getStatus());
        values.put(DbHelper.COLUMN_LAST_LOCATION, asset.getLastLocation());
        values.put(DbHelper.COLUMN_CREATION_TIMESTAMP, asset.getCreationTimestamp());

        long insertId = database.insert(DbHelper.TABLE_ASSETS, null, values);
        if (insertId == -1) {
            Log.e(TAG, "Error al insertar activo: " + asset.getName());
        } else {
            Log.i(TAG, "Activo insertado con ID: " + insertId);
        }
        close();
        return insertId;
    }

    public int updateAsset(Asset asset) {
        open();
        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_NAME, asset.getName());
        values.put(DbHelper.COLUMN_DESCRIPTION, asset.getDescription());
        values.put(DbHelper.COLUMN_STATUS, asset.getStatus());
        values.put(DbHelper.COLUMN_LAST_LOCATION, asset.getLastLocation());
        
        int rowsAffected = database.update(DbHelper.TABLE_ASSETS, values,
                DbHelper.COLUMN_ASSET_ID + " = ?",
                new String[]{String.valueOf(asset.getId())});
        
        Log.i(TAG, "Activo actualizado. ID: " + asset.getId() + ", Filas afectadas: " + rowsAffected);
        close();
        return rowsAffected;
    }
    
    public int deleteAsset(int assetId) {
        open();
        int rowsAffected = database.delete(DbHelper.TABLE_ASSETS, 
                DbHelper.COLUMN_ASSET_ID + " = ?", 
                new String[]{String.valueOf(assetId)});
        Log.i(TAG, "Activo eliminado. ID: " + assetId + ", Filas afectadas: " + rowsAffected);
        close();
        return rowsAffected;
    }

    public Asset getAssetByQrCode(String qrCode) {
        open();
        Asset asset = null;
        Cursor cursor = database.query(DbHelper.TABLE_ASSETS,
                null,
                DbHelper.COLUMN_QR_CODE + " = ?",
                new String[]{qrCode},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            asset = cursorToAsset(cursor);
        }

        if (cursor != null) {
            cursor.close();
        }
        close();
        return asset;
    }
    
    public Asset getAssetById(int assetId) {
        open();
        Asset asset = null;
        Cursor cursor = database.query(DbHelper.TABLE_ASSETS,
                null, 
                DbHelper.COLUMN_ASSET_ID + " = ?",
                new String[]{String.valueOf(assetId)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            asset = cursorToAsset(cursor);
        }

        if (cursor != null) {
            cursor.close();
        }
        close();
        return asset;
    }

    public List<Asset> getAllAssets() {
        open();
        List<Asset> assets = new ArrayList<>();
        Cursor cursor = database.query(DbHelper.TABLE_ASSETS,
                null, null, null, null, null, DbHelper.COLUMN_NAME + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                assets.add(cursorToAsset(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        close();
        return assets;
    }

    private Asset cursorToAsset(Cursor cursor) {
        Asset asset = new Asset();
        asset.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_ASSET_ID)));
        asset.setQrCode(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_QR_CODE)));
        asset.setName(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_NAME)));
        asset.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_DESCRIPTION)));
        asset.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_STATUS)));
        asset.setLastLocation(cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LAST_LOCATION)));
        asset.setCreationTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_CREATION_TIMESTAMP)));
        return asset;
    }

    public long addLocationRecord(LocationRecord record) {
        open();
        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_ASSET_FK, record.getAssetId());
        values.put(DbHelper.COLUMN_LATITUDE, record.getLatitude());
        values.put(DbHelper.COLUMN_LONGITUDE, record.getLongitude());
        values.put(DbHelper.COLUMN_TIMESTAMP, record.getTimestamp());

        long insertId = database.insert(DbHelper.TABLE_LOCATION_RECORDS, null, values);
        if (insertId == -1) {
            Log.e(TAG, "Error al insertar registro de ubicación.");
        } else {
            Log.i(TAG, "Registro de ubicación insertado con ID: " + insertId);
        }
        close();
        return insertId;
    }
    
    public List<LocationRecord> getLocationHistory(int assetId) {
        open();
        List<LocationRecord> records = new ArrayList<>();
        Cursor cursor = database.query(DbHelper.TABLE_LOCATION_RECORDS,
                null,
                DbHelper.COLUMN_ASSET_FK + " = ?",
                new String[]{String.valueOf(assetId)},
                null, null, DbHelper.COLUMN_TIMESTAMP + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                records.add(cursorToLocationRecord(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        close();
        return records;
    }
    
    private LocationRecord cursorToLocationRecord(Cursor cursor) {
        LocationRecord record = new LocationRecord();
        record.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_RECORD_ID)));
        record.setAssetId(cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_ASSET_FK)));
        record.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LATITUDE)));
        record.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_LONGITUDE)));
        record.setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_TIMESTAMP)));
        return record;
    }
}
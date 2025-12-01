package com.example.addqr.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "AssetManagerPro.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_ASSETS = "assets";
    public static final String COLUMN_ASSET_ID = "_id";
    public static final String COLUMN_QR_CODE = "qr_code";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_LAST_LOCATION = "last_location";
    public static final String COLUMN_CREATION_TIMESTAMP = "creation_timestamp";

    public static final String TABLE_LOCATION_RECORDS = "location_records";
    public static final String COLUMN_RECORD_ID = "_id";
    public static final String COLUMN_ASSET_FK = "asset_id";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private static final String SQL_CREATE_ASSETS =
            "CREATE TABLE " + TABLE_ASSETS + " (" +
                    COLUMN_ASSET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_QR_CODE + " TEXT UNIQUE NOT NULL," +
                    COLUMN_NAME + " TEXT NOT NULL," +
                    COLUMN_DESCRIPTION + " TEXT," +
                    COLUMN_STATUS + " TEXT," +
                    COLUMN_LAST_LOCATION + " TEXT," +
                    COLUMN_CREATION_TIMESTAMP + " INTEGER)";

    private static final String SQL_CREATE_LOCATION_RECORDS =
            "CREATE TABLE " + TABLE_LOCATION_RECORDS + " (" +
                    COLUMN_RECORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_ASSET_FK + " INTEGER NOT NULL," +
                    COLUMN_LATITUDE + " REAL," +
                    COLUMN_LONGITUDE + " REAL," +
                    COLUMN_TIMESTAMP + " INTEGER," +
                    "FOREIGN KEY (" + COLUMN_ASSET_FK + ") REFERENCES " + TABLE_ASSETS + "(" + COLUMN_ASSET_ID + ") ON DELETE CASCADE)";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ASSETS);
        db.execSQL(SQL_CREATE_LOCATION_RECORDS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION_RECORDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSETS);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
}
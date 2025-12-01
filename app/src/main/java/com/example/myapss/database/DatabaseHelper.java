package com.example.myapss.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapss.models.InspectionModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "InspectionAPK.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_INSPECTIONS = "inspections";
    private static final String TABLE_FILES = "files";

    // Inspection columns
    private static final String COL_ID = "id";
    private static final String COL_VESSEL_NAME = "vessel_name";
    private static final String COL_IMO = "imo";
    private static final String COL_CALL_SIGN = "call_sign";
    private static final String COL_VDR_MAKE = "vdr_make";
    private static final String COL_ENGINEER_NAME = "engineer_name";
    private static final String COL_ENGINEER_COMPANY = "engineer_company";
    private static final String COL_INSPECTION_DATE = "inspection_date";
    private static final String COL_STATUS = "status";
    private static final String COL_CREATED_AT = "created_at";
    private static final String COL_UPDATED_AT = "updated_at";

    // File columns
    private static final String COL_FILE_ID = "file_id";
    private static final String COL_INSPECTION_ID = "inspection_id";
    private static final String COL_FILE_NAME = "file_name";
    private static final String COL_FILE_PATH = "file_path";
    private static final String COL_FILE_TYPE = "file_type";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createInspectionsTable = "CREATE TABLE " + TABLE_INSPECTIONS + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_VESSEL_NAME + " TEXT," +
                COL_IMO + " TEXT," +
                COL_CALL_SIGN + " TEXT," +
                COL_VDR_MAKE + " TEXT," +
                COL_ENGINEER_NAME + " TEXT," +
                COL_ENGINEER_COMPANY + " TEXT," +
                COL_INSPECTION_DATE + " TEXT," +
                COL_STATUS + " TEXT," +
                COL_CREATED_AT + " LONG," +
                COL_UPDATED_AT + " LONG" +
                ")";

        String createFilesTable = "CREATE TABLE " + TABLE_FILES + "(" +
                COL_FILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_INSPECTION_ID + " INTEGER," +
                COL_FILE_NAME + " TEXT," +
                COL_FILE_PATH + " TEXT," +
                COL_FILE_TYPE + " TEXT," +
                "FOREIGN KEY(" + COL_INSPECTION_ID + ") REFERENCES " + TABLE_INSPECTIONS + "(" + COL_ID + ")" +
                ")";

        db.execSQL(createInspectionsTable);
        db.execSQL(createFilesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSPECTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILES);
        onCreate(db);
    }

    public long addInspection(InspectionModel inspection) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_VESSEL_NAME, inspection.vesselName);
        values.put(COL_IMO, inspection.imo);
        values.put(COL_CALL_SIGN, inspection.callSign);
        values.put(COL_VDR_MAKE, inspection.vdrMake);
        values.put(COL_ENGINEER_NAME, inspection.engineerName);
        values.put(COL_ENGINEER_COMPANY, inspection.engineerCompany);
        values.put(COL_INSPECTION_DATE, inspection.inspectionDate);
        values.put(COL_STATUS, inspection.status);
        values.put(COL_CREATED_AT, inspection.createdAt);
        values.put(COL_UPDATED_AT, inspection.updatedAt);

        long id = db.insert(TABLE_INSPECTIONS, null, values);
        db.close();
        return id;
    }

    public List<InspectionModel> getAllInspections() {
        List<InspectionModel> inspections = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_INSPECTIONS, null, null, null, null, null,
                COL_CREATED_AT + " DESC");

        if (cursor.moveToFirst()) {
            do {
                InspectionModel inspection = new InspectionModel();
                inspection.id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                inspection.vesselName = cursor.getString(cursor.getColumnIndexOrThrow(COL_VESSEL_NAME));
                inspection.imo = cursor.getString(cursor.getColumnIndexOrThrow(COL_IMO));
                inspection.callSign = cursor.getString(cursor.getColumnIndexOrThrow(COL_CALL_SIGN));
                inspection.vdrMake = cursor.getString(cursor.getColumnIndexOrThrow(COL_VDR_MAKE));
                inspection.engineerName = cursor.getString(cursor.getColumnIndexOrThrow(COL_ENGINEER_NAME));
                inspection.engineerCompany = cursor.getString(cursor.getColumnIndexOrThrow(COL_ENGINEER_COMPANY));
                inspection.inspectionDate = cursor.getString(cursor.getColumnIndexOrThrow(COL_INSPECTION_DATE));
                inspection.status = cursor.getString(cursor.getColumnIndexOrThrow(COL_STATUS));
                inspection.createdAt = cursor.getLong(cursor.getColumnIndexOrThrow(COL_CREATED_AT));
                inspection.updatedAt = cursor.getLong(cursor.getColumnIndexOrThrow(COL_UPDATED_AT));
                inspections.add(inspection);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return inspections;
    }

    public void updateInspectionStatus(int inspectionId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_STATUS, status);
        values.put(COL_UPDATED_AT, System.currentTimeMillis());
        db.update(TABLE_INSPECTIONS, values, COL_ID + "=?", new String[]{String.valueOf(inspectionId)});
        db.close();
    }

    public void deleteInspection(int inspectionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INSPECTIONS, COL_ID + "=?", new String[]{String.valueOf(inspectionId)});
        db.delete(TABLE_FILES, COL_INSPECTION_ID + "=?", new String[]{String.valueOf(inspectionId)});
        db.close();
    }
}
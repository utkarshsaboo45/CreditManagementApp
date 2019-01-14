package com.sparksfoundation.creditmanagementapp.Helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

public class DatabaseManager {

    private static DatabaseManager instance;
    private static SQLiteOpenHelper mDatabaseHelper;
    private AtomicInteger mOpenCounter = new AtomicInteger();
    private SQLiteDatabase mDatabase;

    public static synchronized void initializeInstance(SQLiteOpenHelper sqLiteOpenHelper) {
        if(instance == null) {
            instance = new DatabaseManager();
            mDatabaseHelper = sqLiteOpenHelper;
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if(instance == null) {
            throw new IllegalStateException(
                    DatabaseManager.class.getSimpleName() +
                            " is not initialized, call initializeInstance() first.");
        }
        return instance;
    }

    public synchronized void openDatabase() {
        if(mOpenCounter.incrementAndGet() == 1)
        {
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
    }

    public synchronized void closeDatabase() {
        if(mOpenCounter.decrementAndGet() == 0)
        {
            mDatabase.close();
        }
    }

    public Cursor getDetails(String query) {
        return mDatabase.rawQuery(query, null);
    }

    public boolean insert(String tableName, ContentValues values) {
        long l = -1;
        try {
            l = mDatabase.insert(tableName, null, values);
            Log.i("DatabaseManager", "Insert Working in the DatabaseManager class");
        } catch(SQLException e)
        {
            Log.i("DatabaseManager", "Insert Not working in the DatabaseManager class");
            e.printStackTrace();
        }

        return l != -1;
    }

    public int update(String tableName, ContentValues values, int id) {
        int l = 0;
        try {
            l = mDatabase.update(tableName, values, DatabaseHelper.KEY_ID + " = " + id, null);
            Log.i("DatabaseManager", "Update Working in the DatabaseManager class");
        } catch(SQLException e)
        {
            Log.i("DatabaseManager", "Update Not working in the DatabaseManager class");
            e.printStackTrace();
        }

        return l;
    }

    public int delete(String tableName, int id)
    {
        int l = 0;
        try {
            l = mDatabase.delete(tableName, DatabaseHelper.KEY_ID + " = " + id, null);
            Log.i("DatabaseManager", "Delete Working in the DatabaseManager class");
        } catch(SQLException e)
        {
            Log.i("DatabaseManager", "Delete Not working in the DatabaseManager class");
            e.printStackTrace();
        }
        return l;
    }

    public int deleteAll(String tableName)
    {
        int l = 0;
        try {
            l = mDatabase.delete(tableName, null, null);
            Log.i("DatabaseManager", "Delete All Working in the DatabaseManager class");
        } catch(SQLException e)
        {
            Log.i("DatabaseManager", "Delete All Not working in the DatabaseManager class");
            e.printStackTrace();
        }
        return l;
    }

    public boolean deleteTable(String tableName) {
        try {
            mDatabase.delete(tableName, null, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
package com.sparksfoundation.creditmanagementapp.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "CreditManagement.sqlite";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USERS = "users";
    public static final String TABLE_TRANSACTIONS = "transactions";

    // Common column names
    public static final String KEY_ID = BaseColumns._ID;

    // Users Table - column names
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_CURRENT_CREDITS = "current_credits";

    // Transactions Table - column names
    public static final String KEY_DATE_TIME = "date_time";
    public static final String KEY_SENDER_ID = "sender";
    public static final String KEY_RECEIVER_ID = "receiver";
    public static final String KEY_SENDER_OPENING_BALANCE = "sender_opening_balance";
    public static final String KEY_SENDER_CLOSING_BALANCE = "sender_closing_balance";
    public static final String KEY_RECEIVER_OPENING_BALANCE = "receiver_opening_balance";
    public static final String KEY_RECEIVER_CLOSING_BALANCE = "receiver_closing_balance";
    public static final String KEY_ORDER_STATUS = "order_status";

    private final String TABLE_USER_DROP = "DROP TABLE IF EXISTS " + TABLE_USERS;
    private final String TABLE_TRANSACTION_DROP = "DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS;

    private String TABLE_CREATE_USER = "CREATE TABLE " + TABLE_USERS + " (" +
            KEY_ID + " INTEGER PRIMARY KEY, " +
            KEY_NAME + " TEXT UNIQUE, " +
            KEY_EMAIL + " TEXT UNIQUE, " +
            KEY_CURRENT_CREDITS + " INTEGER);";

    private String TABLE_CREATE_TRANSACTION = "CREATE TABLE " + TABLE_TRANSACTIONS + " (" +
            KEY_ID + " INTEGER PRIMARY KEY, " +
            KEY_DATE_TIME + " TEXT, " +
            KEY_SENDER_ID + " INTEGER, " +
            KEY_RECEIVER_ID + " INTEGER, " +
            KEY_SENDER_OPENING_BALANCE + " INTEGER, " +
            KEY_SENDER_CLOSING_BALANCE + " INTEGER, " +
            KEY_RECEIVER_OPENING_BALANCE + " INTEGER, " +
            KEY_RECEIVER_CLOSING_BALANCE + " INTEGER, " +
            KEY_ORDER_STATUS + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE_USER);
        sqLiteDatabase.execSQL(TABLE_CREATE_TRANSACTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void dropDatabase(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(TABLE_USER_DROP);
        sqLiteDatabase.execSQL(TABLE_CREATE_USER);
        sqLiteDatabase.execSQL(TABLE_TRANSACTION_DROP);
        sqLiteDatabase.execSQL(TABLE_CREATE_TRANSACTION);
    }
}

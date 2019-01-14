package com.sparksfoundation.creditmanagementapp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.sparksfoundation.creditmanagementapp.Helper.DatabaseHelper;
import com.sparksfoundation.creditmanagementapp.Helper.DatabaseManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TransferUserSelectionActivity extends AppCompatActivity {

    private ListView receiverUsersListView;
    private SendCreditsCursorAdapter mSendCreditsCursorAdapter;
    private String creditsToSendString;
    private int idReceiver;
    private int idSender;
    private int senderOpeningBalance, senderClosingBalance, receiverOpeningBalance, receiverClosingBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_user_selection);

        idSender = Integer.parseInt(getIntent().getStringExtra(DatabaseHelper.KEY_ID));

        receiverUsersListView = findViewById(R.id.receiver_users_list);

        mSendCreditsCursorAdapter = new SendCreditsCursorAdapter(this, null);//, senderId);
        receiverUsersListView.setAdapter(mSendCreditsCursorAdapter);

        try {
            String dataFetchQuery = "SELECT * FROM " + DatabaseHelper.TABLE_USERS + " WHERE " + DatabaseHelper.KEY_ID + " != " + idSender + ";";

            DatabaseManager.getInstance().openDatabase();
            Cursor cursor = DatabaseManager.getInstance().getDetails(dataFetchQuery);
            mSendCreditsCursorAdapter.swapCursor(cursor);

        } catch (Exception e) {
            showToast("Catch Block, error in fetching data");
            Log.i("TransferUserSelection", "Catch block error : ");
            e.printStackTrace();
        }


        receiverUsersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                idReceiver = (int)l;
                showCreditDialog();
            }
        });
    }

    public void showCreditDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(TransferUserSelectionActivity.this);
        builder.setTitle("Transfer funds");

        builder.setMessage("Enter the amount you want to transfer");

        // Set up the input
        final EditText input = new EditText(getApplicationContext());

//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        lp.setMargins(convertPixelsToDp(100,this), 0, convertPixelsToDp(100,this), 0);
//        input.setLayoutParams(lp);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_NUMBER);// | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("SEND", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                creditsToSendString = input.getText().toString();
                if (TextUtils.isEmpty(creditsToSendString)) {
                    Toast.makeText(getApplicationContext(), "Please enter the number of credits to send", Toast.LENGTH_SHORT).show();
                    return;
                }
                transferCredits();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void transferCredits()
    {
        String senderDataFetchQuery = "SELECT " + DatabaseHelper.KEY_CURRENT_CREDITS + " FROM " + DatabaseHelper.TABLE_USERS + " WHERE " + DatabaseHelper.KEY_ID + " = " + idSender + ";";
        DatabaseManager.getInstance().openDatabase();
        Cursor cursor = DatabaseManager.getInstance().getDetails(senderDataFetchQuery);
        cursor.moveToFirst();
        senderOpeningBalance = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CURRENT_CREDITS));

        String receiverDataFetchQuery = "SELECT " + DatabaseHelper.KEY_CURRENT_CREDITS + " FROM " + DatabaseHelper.TABLE_USERS + " WHERE " + DatabaseHelper.KEY_ID + " = " + idReceiver + ";";
        DatabaseManager.getInstance().openDatabase();
        cursor = DatabaseManager.getInstance().getDetails(receiverDataFetchQuery);
        cursor.moveToFirst();
        receiverOpeningBalance = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CURRENT_CREDITS));

        if(senderOpeningBalance - Integer.parseInt(creditsToSendString) > 0)
        {
            senderClosingBalance = senderOpeningBalance - Integer.parseInt(creditsToSendString);
            receiverClosingBalance = receiverOpeningBalance + Integer.parseInt(creditsToSendString);
        }
        else
        {
            senderClosingBalance = senderOpeningBalance;
            receiverClosingBalance = receiverOpeningBalance;
            showToast("You don't have enough credits!");
            createTransaction("Failed");
            return;
        }

        updateUser();
    }

    private void updateUser() {
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.KEY_CURRENT_CREDITS, senderClosingBalance);

        int noOfRowsUpdatedSender = DatabaseManager.getInstance().update(DatabaseHelper.TABLE_USERS, values, idSender);

        values.put(DatabaseHelper.KEY_CURRENT_CREDITS, receiverClosingBalance);

        int noOfRowsUpdatedReceiver = DatabaseManager.getInstance().update(DatabaseHelper.TABLE_USERS, values, idReceiver);

        // Show a toast message depending on whether or not the update was successful.
        if (noOfRowsUpdatedSender == 0 || noOfRowsUpdatedReceiver == 0) {
            // If no rows were affected, then there was an error with the update.
            showToast(getString(R.string.editor_update_user_failed));
        } else {
            // Otherwise, the update was successful and we can display a toast.
            showToast(getString(R.string.editor_update_user_successful));
            createTransaction("Success");
        }
//        Intent intent = new Intent(TransferUserSelectionActivity.this, ViewUsersActivity.class);
//        finish();
//        startActivity(intent);
        NavUtils.navigateUpFromSameTask(this);
    }

    public void createTransaction(String orderStatus)
    {
        try {
            ContentValues values = new ContentValues();

            values.put(DatabaseHelper.KEY_DATE_TIME, getDateTime());
            values.put(DatabaseHelper.KEY_SENDER_ID, idSender);
            values.put(DatabaseHelper.KEY_RECEIVER_ID, idReceiver);
            values.put(DatabaseHelper.KEY_SENDER_OPENING_BALANCE, senderOpeningBalance);
            values.put(DatabaseHelper.KEY_SENDER_CLOSING_BALANCE, senderClosingBalance);
            values.put(DatabaseHelper.KEY_RECEIVER_OPENING_BALANCE, receiverOpeningBalance);
            values.put(DatabaseHelper.KEY_RECEIVER_CLOSING_BALANCE, receiverClosingBalance);
            values.put(DatabaseHelper.KEY_ORDER_STATUS, orderStatus);

            DatabaseManager.getInstance().openDatabase();
            boolean inserted = DatabaseManager.getInstance().insert(DatabaseHelper.TABLE_TRANSACTIONS, values);

            if (inserted) {
                showToast("Transaction created");
            } else {
                showToast("Transaction not created");
            }
        } catch (Exception e)
        {
            showToast("Catch Block Executed, user not inserted");
            Log.i("EditUserActivity", "Catch block error here : ");
            e.printStackTrace();
        }
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

//    public static int convertPixelsToDp(float px, Context context){
//        Resources resources = context.getResources();
//        DisplayMetrics metrics = resources.getDisplayMetrics();
//        int dp = Math.round(px / (metrics.densityDpi / 160f));
//        return dp;
//    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showLongToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}

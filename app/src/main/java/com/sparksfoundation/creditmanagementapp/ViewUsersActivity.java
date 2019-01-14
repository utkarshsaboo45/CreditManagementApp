package com.sparksfoundation.creditmanagementapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sparksfoundation.creditmanagementapp.Helper.DatabaseHelper;
import com.sparksfoundation.creditmanagementapp.Helper.DatabaseManager;

public class ViewUsersActivity extends AppCompatActivity {

    private Button addUserButton, showTransactionsButton;
    private ListView usersListView;
    private View emptyView;

    private UserCursorAdapter mUserCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        DatabaseManager.initializeInstance(new DatabaseHelper(this));

        addUserButton = findViewById(R.id.add_user_button);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewUsersActivity.this, EditUserActivity.class);
                startActivity(intent);
            }
        });

        showTransactionsButton = findViewById(R.id.show_transactions_button);
        showTransactionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewUsersActivity.this, TransactionsActivity.class);
                startActivity(intent);
            }
        });

        usersListView = findViewById(R.id.users_list);
        emptyView = findViewById(R.id.empty_view);

        usersListView.setEmptyView(emptyView);

        mUserCursorAdapter = new UserCursorAdapter(this, null);
        usersListView.setAdapter(mUserCursorAdapter);

        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showToast("User Item Clicked");
                Intent intent = new Intent(getApplicationContext(), EditUserActivity.class);

                String dataFetchQuery = "SELECT * FROM " + DatabaseHelper.TABLE_USERS +
                        " WHERE " + DatabaseHelper.KEY_ID + " = " + id + ";";
                DatabaseManager.getInstance().openDatabase();
                Cursor cursor = DatabaseManager.getInstance().getDetails(dataFetchQuery);

                cursor.moveToFirst();

                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_NAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_EMAIL));
                String credits = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CURRENT_CREDITS));
                String userId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ID));

                intent.putExtra(DatabaseHelper.KEY_NAME, name);
                intent.putExtra(DatabaseHelper.KEY_EMAIL, email);
                intent.putExtra(DatabaseHelper.KEY_CURRENT_CREDITS, credits);
                intent.putExtra(DatabaseHelper.KEY_ID, userId);
                startActivity(intent);
            }
        });

        try {
            String dataFetchQuery = "SELECT * FROM " + DatabaseHelper.TABLE_USERS + ";";

            DatabaseManager.getInstance().openDatabase();
            Cursor cursor = DatabaseManager.getInstance().getDetails(dataFetchQuery);

            if (cursor != null && cursor.getCount() > 0)
            {
                showToast("No. of items :" + cursor.getCount());
            }
            else if(cursor.getCount() == 0)
            {
                showToast("Add users to continue");
            }
            mUserCursorAdapter.swapCursor(cursor);

        } catch (Exception e) {
            showToast("Catch Block, error in fetching data");
            Log.i("ViewUsersActivity", "Catch block error : ");
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertDummyData();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllEntries();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertDummyData()
    {
        insertUser("Utkarsh", "utkarsh@gmail.com", 100);
        insertUser("Shriyansh", "shriyansh@gmail.com", 100);
        insertUser("Harsh", "harsh@gmail.com", 100);
        insertUser("Divyansh", "divyansh@gmail.com", 100);
        insertUser("Jay", "jay@gmail.com", 100);
        insertUser("Mohak", "mohak@gmail.com", 100);
        insertUser("Abhilasha", "abhilasha@gmail.com", 100);
        insertUser("Prachika", "prachika@gmail.com", 100);
        insertUser("Debayan", "debayan@gmail.com", 100);
        insertUser("Abhishek", "abhishek@gmail.com", 100);
        showToast("Users inserted");
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    private void deleteAllEntries()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(ViewUsersActivity.this).create();
        alertDialog.setMessage(getString(R.string.delete_all_users_dialog_msg));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.delete),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        int noOfRowsDeleted = DatabaseManager.getInstance().deleteAll(DatabaseHelper.TABLE_USERS);
                        showToast(noOfRowsDeleted + " entries deleted!");
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no_delete),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void insertUser(String name, String email, int credits) {

        try {
            ContentValues values = new ContentValues();

            values.put(DatabaseHelper.KEY_NAME, name);
            values.put(DatabaseHelper.KEY_EMAIL, email);
            values.put(DatabaseHelper.KEY_CURRENT_CREDITS, credits);

            DatabaseManager.getInstance().openDatabase();
            DatabaseManager.getInstance().insert(DatabaseHelper.TABLE_USERS, values);

        } catch (Exception e)
        {
            showToast("Catch Block Executed, user not inserted");
            Log.i("ViewUsersActivity", "Catch block error here : ");
            e.printStackTrace();
        }
    }

    private void showToast(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showLongToast(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}

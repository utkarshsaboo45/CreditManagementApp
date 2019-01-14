package com.sparksfoundation.creditmanagementapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sparksfoundation.creditmanagementapp.Helper.DatabaseHelper;
import com.sparksfoundation.creditmanagementapp.Helper.DatabaseManager;

public class EditUserActivity extends AppCompatActivity {

    private static final String LOG_TAG = "EditUserActivity";

    private EditText mNameEditText;
    private EditText mEmailEditText;
    private EditText mCreditsEditText;

    private DatabaseHelper mDbHelper;

    private boolean mUserHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mUserHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        mNameEditText = findViewById(R.id.edit_user_name);
        mEmailEditText = findViewById(R.id.edit_user_email);
        mCreditsEditText = findViewById(R.id.edit_user_credits);

        mNameEditText.setOnTouchListener(mTouchListener);
        mEmailEditText.setOnTouchListener(mTouchListener);
        mCreditsEditText.setOnTouchListener(mTouchListener);

        if(getIntent().getExtras() != null)
        {
            getSupportActionBar().setTitle("Edit a user");
            String name = getIntent().getStringExtra(DatabaseHelper.KEY_NAME);
            String email = getIntent().getStringExtra(DatabaseHelper.KEY_EMAIL);
            String credits = getIntent().getStringExtra(DatabaseHelper.KEY_CURRENT_CREDITS);

            mNameEditText.setText(name);
            mEmailEditText.setText(email);
            mCreditsEditText.setText(credits);
        }
        else
        {
            getSupportActionBar().setTitle("Add a user");
        }
    }


    public void insertUser() {
        String name = mNameEditText.getText().toString().trim();
        String email = mEmailEditText.getText().toString().trim();
        String tempCredits = mCreditsEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "User not entered!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (TextUtils.isEmpty(tempCredits)) {
            tempCredits = "0";
        }

        int credits = Integer.parseInt(tempCredits);

        try {
            ContentValues values = new ContentValues();

            values.put(DatabaseHelper.KEY_NAME, name);
            values.put(DatabaseHelper.KEY_EMAIL, email);
            values.put(DatabaseHelper.KEY_CURRENT_CREDITS, credits);

            DatabaseManager.getInstance().openDatabase();
            boolean inserted = DatabaseManager.getInstance().insert(DatabaseHelper.TABLE_USERS, values);

            if (inserted) {
                showToast("User Inserted");
            } else {
                showToast("Not inserted");
            }
        } catch (Exception e)
        {
            showToast("Catch Block Executed, user not inserted");
            Log.i("EditUserActivity", "Catch block error here : ");
            e.printStackTrace();
        }
    }

    private void updateUser(int id) {
        String name = mNameEditText.getText().toString().trim();
        String email = mEmailEditText.getText().toString().trim();
        String tempCredits = mCreditsEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, R.string.user_not_entered, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (TextUtils.isEmpty(tempCredits)) {
            tempCredits = "0";
        }

        int credits = Integer.parseInt(tempCredits);

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.KEY_NAME, name);
        values.put(DatabaseHelper.KEY_EMAIL, email);
        values.put(DatabaseHelper.KEY_CURRENT_CREDITS, credits);

        int noOfRowsUpdated = DatabaseManager.getInstance().update(DatabaseHelper.TABLE_USERS, values, id);

        // Show a toast message depending on whether or not the update was successful.
        if (noOfRowsUpdated == 0) {
            // If no rows were affected, then there was an error with the update.
            showToast(getString(R.string.editor_update_user_failed));
        } else {
            // Otherwise, the update was successful and we can display a toast.
            showToast(getString(R.string.editor_update_user_successful));
        }
    }

    private void deleteUser(int id) {
        int noOfRowsDeleted = DatabaseManager.getInstance().delete(DatabaseHelper.TABLE_USERS, id);

        if (noOfRowsDeleted == 0) {
            // If no rows were affected, then there was an error with the update.
            showToast(getString(R.string.editor_delete_user_failed));
        } else {
            // Otherwise, the update was successful and we can display a toast.
            showToast(getString(R.string.editor_delete_user_successful));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_add_user.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_add_user, menu);
        if (getIntent().getExtras() == null)
            invalidateOptionsMenu();
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new user, hide the "Delete" menu item.
        if (getIntent().getExtras() == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu

        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                if (getIntent().getExtras() == null) {
                    insertUser();
//                    Intent intent = new Intent(EditUserActivity.this, ViewUsersActivity.class);
//                    finish();
//                    startActivity(intent);
                    NavUtils.navigateUpFromSameTask(this);
                } else {
                    updateUser(Integer.parseInt(getIntent().getStringExtra(DatabaseHelper.KEY_ID)));
//                    Intent intent = new Intent(EditUserActivity.this, ViewUsersActivity.class);
//                    finish();
//                    startActivity(intent);
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                if (getIntent().getExtras() != null) {
                    deleteUser(Integer.parseInt(getIntent().getStringExtra(DatabaseHelper.KEY_ID)));
                    Intent intent = new Intent(EditUserActivity.this, ViewUsersActivity.class);
                    startActivity(intent);
                }
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                if (mUserHasChanged) {
                    AlertDialog alertDialog = new AlertDialog.Builder(EditUserActivity.this).create();
                    alertDialog.setMessage("Discard your changes and quit editing?");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Discard",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    // Navigate back to parent activity (MainActivity)
                                    NavUtils.navigateUpFromSameTask(EditUserActivity.this);
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Keep Editing",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else
                    // Navigate back to parent activity (MainActivity)
                    NavUtils.navigateUpFromSameTask(EditUserActivity.this);

                return true;
        }
        return super.onOptionsItemSelected(item);
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

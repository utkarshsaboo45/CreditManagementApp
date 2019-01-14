package com.sparksfoundation.creditmanagementapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.sparksfoundation.creditmanagementapp.Helper.DatabaseHelper;

public class UserCursorAdapter extends CursorAdapter {

    public UserCursorAdapter(Context context, Cursor c)
    {
        super(context, c, 0);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        TextView nameTextView = view.findViewById(R.id.user_name);
        TextView emailTextView = view.findViewById(R.id.user_email);
        TextView creditsTextView = view.findViewById(R.id.credits);
        Button transferButton = view.findViewById(R.id.transfer_button);

        final String userId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ID));
        final String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_NAME));
        final String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_EMAIL));
        final String credits = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CURRENT_CREDITS));

        nameTextView.setText(name);
        creditsTextView.setText(credits);
        emailTextView.setText(email);

        transferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TransferUserSelectionActivity.class);
                intent.putExtra(DatabaseHelper.KEY_ID, userId);
                Toast.makeText(context, "Select the user to transfer credits to!", Toast.LENGTH_LONG).show();
                context.startActivity(intent);
            }
        });
    }
}

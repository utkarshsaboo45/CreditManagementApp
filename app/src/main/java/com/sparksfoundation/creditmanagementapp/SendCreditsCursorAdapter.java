package com.sparksfoundation.creditmanagementapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.sparksfoundation.creditmanagementapp.Helper.DatabaseHelper;

public class SendCreditsCursorAdapter extends CursorAdapter {

    public SendCreditsCursorAdapter(Context context, Cursor c)
    {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.receiver_user_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        TextView nameTextView = view.findViewById(R.id.user_name);
        TextView emailTextView = view.findViewById(R.id.user_email);

        final String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_NAME));
        final String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_EMAIL));

        nameTextView.setText(name);
        emailTextView.setText(email);
    }
}

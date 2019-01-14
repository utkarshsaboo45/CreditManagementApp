package com.sparksfoundation.creditmanagementapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.sparksfoundation.creditmanagementapp.Helper.DatabaseHelper;

public class TransactionCursorAdapter extends CursorAdapter {

    public TransactionCursorAdapter(Context context, Cursor c)
    {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.transaction_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        TextView transactionIdTextView = view.findViewById(R.id.transaction_id);
        TextView transactionTimeTextView = view.findViewById(R.id.transaction_time);
        TextView senderIdTextView = view.findViewById(R.id.sender_id);
        TextView receiverIdTextView = view.findViewById(R.id.receiver_id);
        TextView senderOpeningBalanceTextView = view.findViewById(R.id.sender_opening_balance);
        TextView senderClosingBalanceTextView = view.findViewById(R.id.sender_closing_balance);
        TextView receiverOpeningBalanceTextView = view.findViewById(R.id.receiver_opening_balance);
        TextView receiverClosingBalanceTextView = view.findViewById(R.id.receiver_closing_balance);
        TextView orderStatusTextView = view.findViewById(R.id.order_status);

        final int transactionId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ID));
        final String transactionTime = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_DATE_TIME));
        final int senderId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_SENDER_ID));
        final int receiverId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_RECEIVER_ID));
        final int senderOpeningBalance = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_SENDER_OPENING_BALANCE));
        final int senderClosingBalance = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_SENDER_CLOSING_BALANCE));
        final int receiverOpeningBalance = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_RECEIVER_OPENING_BALANCE));
        final int receiverClosingBalance = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_RECEIVER_CLOSING_BALANCE));
        final String order_status = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ORDER_STATUS));

        transactionIdTextView.setText("Transaction ID: \t" + transactionId);
        transactionTimeTextView.setText("Transaction Time: \t" + transactionTime);
        senderIdTextView.setText("Sender ID : \t\t" + senderId);
        receiverIdTextView.setText("Receiver ID : \t\t" + receiverId);
        senderOpeningBalanceTextView.setText("Sender Opening Balance : " + senderOpeningBalance);
        senderClosingBalanceTextView.setText("Sender Closing Balance : " + senderClosingBalance);
        receiverOpeningBalanceTextView.setText("Receiver Opening Balance : " + receiverOpeningBalance);
        receiverClosingBalanceTextView.setText("Receiver Closing Balance : " + receiverClosingBalance);
        orderStatusTextView.setText(order_status);
    }
}

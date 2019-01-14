package com.sparksfoundation.creditmanagementapp;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.sparksfoundation.creditmanagementapp.Helper.DatabaseHelper;
import com.sparksfoundation.creditmanagementapp.Helper.DatabaseManager;

public class TransactionsActivity extends AppCompatActivity {

    private ListView transactionsListView;
    private View emptyView;

    private TransactionCursorAdapter mTransactionCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        transactionsListView = findViewById(R.id.transactions_list);
        emptyView = findViewById(R.id.empty_view);

        transactionsListView.setEmptyView(emptyView);

        mTransactionCursorAdapter = new TransactionCursorAdapter(this, null);
        transactionsListView.setAdapter(mTransactionCursorAdapter);

        try {
            String dataFetchQuery = "SELECT * FROM " + DatabaseHelper.TABLE_TRANSACTIONS + ";";

            DatabaseManager.getInstance().openDatabase();
            Cursor cursor = DatabaseManager.getInstance().getDetails(dataFetchQuery);

            if (cursor != null && cursor.getCount() > 0)
            {
                showToast("No. of transactions :" + cursor.getCount());
            }
            else if(cursor.getCount() == 0)
            {
                showToast("No Transaction");
            }
            mTransactionCursorAdapter.swapCursor(cursor);

        } catch (Exception e) {
            showToast("Catch Block, error in fetching data");
            Log.i("TransactionsActivity", "Catch block error : ");
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

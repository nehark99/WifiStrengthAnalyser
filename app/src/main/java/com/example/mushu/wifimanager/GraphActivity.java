package com.example.mushu.wifimanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class GraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Context context = this;
        // Create DatabaseHelper instance
            String wname = getIntent().getStringExtra("ssid");
            String strength = getIntent().getStringExtra("ss");
            // Insert sample data
            DatabaseHelper dataHelper = new DatabaseHelper(context);
            dataHelper.insertData(wname, strength);
            // Reference to TableLayout
            TableLayout tableLayout = findViewById(R.id.tablelayout);
            // Add header row
            TableRow rowHeader = new TableRow(context);
            rowHeader.setBackgroundColor(Color.parseColor("#c0c0c0"));
            rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            String[] headerText = {"ID", "NAME", "STRENGTH"};
            for (String c : headerText) {
                TextView tv = new TextView(this);
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(18);
                tv.setPadding(5, 5, 5, 5);
                tv.setText(c);
                rowHeader.addView(tv);
            }
            tableLayout.addView(rowHeader);

            // Get data from sqlite database and add them to the table
            // Open the database for reading
            SQLiteDatabase db = dataHelper.getReadableDatabase();
            // Start the transaction.
            db.beginTransaction();

            try {
                String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_OUTLET;
                Cursor cursor = db.rawQuery(selectQuery, null);
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        // Read columns data
                        int outlet_id = cursor.getInt(cursor.getColumnIndex("outlet_id"));
                        String outlet_name = cursor.getString(cursor.getColumnIndex("outlet_name"));
                        String outlet_type = cursor.getString(cursor.getColumnIndex("outlet_type"));

                        // dara rows
                        TableRow row = new TableRow(context);
                        row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT));
                        String[] colText = {outlet_id + "", outlet_name, outlet_type};
                        for (String text : colText) {
                            TextView tv = new TextView(this);
                            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT));
                            tv.setGravity(Gravity.CENTER);
                            tv.setTextSize(16);
                            tv.setPadding(5, 5, 5, 5);
                            tv.setText(text);
                            row.addView(tv);
                        }
                        tableLayout.addView(row);

                    }

                }
                db.setTransactionSuccessful();

            } catch (SQLiteException e) {
                e.printStackTrace();
            }
    }
}



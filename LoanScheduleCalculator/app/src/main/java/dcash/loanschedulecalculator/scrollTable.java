package dcash.loanschedulecalculator;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by student on 1/27/2018.
 */

public class scrollTable extends Activity
{

    ArrayList<String> dateAr = new ArrayList<>();
    ArrayList<String> payAr = new ArrayList<>();
    ArrayList<String> intAr = new ArrayList<>();
    ArrayList<String> prinAr = new ArrayList<>();
    ArrayList<String> balAr = new ArrayList<>();
    TableLayout tableLayout;
    TableLayout table;
    TableRow hRow;
    TableRow tableRow;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        SQLiteDatabaseHandler mDbHelper = new SQLiteDatabaseHandler(getApplicationContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //Retrieve the data
        String query = "SELECT  * FROM " + dataContract.DataEntry.TABLE_NAME;

        Cursor cursor = db.rawQuery(query, null);

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        Type DB = new TypeToken<ArrayList<BigDecimal>>() {}.getType();

        //Parse and store the data
        while(cursor.moveToNext()) {
            dateAr = gson.fromJson(cursor.getString(1), type);
            payAr = gson.fromJson(cursor.getString(2), DB);
            intAr = gson.fromJson(cursor.getString(3), DB);
            prinAr = gson.fromJson(cursor.getString(4), DB);
            balAr = gson.fromJson(cursor.getString(5), DB);
        }
           cursor.close();

        String[] column = { "Date", "Payment", "Interest", "Principal",
                "Balance"
        };
        int rl=balAr.size() + 1;
        int cl=column.length + 1;

        //Create the view
        table = new TableLayout(this);
        hRow = new TableRow(this);
        TableRow.LayoutParams hRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < column.length; i++) {
            autoSizeTextView textView = new autoSizeTextView(this);
            textView.setBackgroundColor(Color.GREEN);
            textView.setGravity(Gravity.CENTER);
            textView.setSingleLine(true);
            textView.setPadding(5,5,5,5);
            textView.setText(column[i]);
            hRow.addView(textView, hRowParams);
        }
        table.addView(hRow);

        ScrollView sv = new ScrollView(this);
        sv.setFillViewport(true);
        tableLayout = createTableLayout(dateAr, column,rl, cl, payAr, intAr, prinAr, balAr);
        sv.addView(tableLayout);
        table.addView(sv);
        setContentView(table);
        table.post(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < hRow.getChildCount(); i++){
                    TableRow.LayoutParams hRowParams = new TableRow.LayoutParams(tableRow.getChildAt(i).getMeasuredWidth(), tableRow.getChildAt(i).getMeasuredHeight());
                    hRow.getChildAt(i).setLayoutParams(hRowParams);
                    hRowParams.setMargins(0, 1, 1, 0);
                    hRowParams.weight = 1;
                }
                hRow.setBackgroundColor(Color.BLACK);
            }
        });
    }

    //Create the table on the screen
    private TableLayout createTableLayout(ArrayList rv, String [] cv,int rowCount, int columnCount, ArrayList pay, ArrayList intr, ArrayList prin, ArrayList bal)
    {
        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT);
        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setBackgroundColor(Color.BLACK);

        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        tableRowParams.setMargins(0, 1, 1, 0);
        tableRowParams.weight = 1;

        for (int i = 0; i < rowCount - 1; i++)
        {
            tableRow = new TableRow(this);
            tableRow.setBackgroundColor(Color.BLACK);

            for (int j= 0; j < columnCount - 1; j++)
            {
                autoSizeTextView textView = new autoSizeTextView(this);
                textView.setBackgroundColor(Color.WHITE);
                textView.setGravity(Gravity.CENTER);
                textView.setSingleLine(true);
                textView.setPadding(5,5,5,5);

                if( j==0)
                {
                    textView.setText(rv.get(i).toString());
                }
                else if (j == 1)
                {
                    textView.setText(NumberFormat.getCurrencyInstance().format(pay.get(i)));
                }
                else if (j == 2)
                {
                    textView.setText(NumberFormat.getCurrencyInstance().format(intr.get(i)));

                }
                else if (j == 3)
                {
                    textView.setText(NumberFormat.getCurrencyInstance().format(prin.get(i)));

                }
                else if (j == 4)
                {
                    textView.setText(NumberFormat.getCurrencyInstance().format(bal.get(i)));

                }
                tableRow.addView(textView, tableRowParams);
            }
            tableLayout.addView(tableRow, tableLayoutParams);
        }
        return tableLayout;
    }
}
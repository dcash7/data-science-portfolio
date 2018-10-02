package dcash.loanschedulecalculator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by student on 1/30/2018.
 */

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Data.db";

    private static final int DATABASE_VERSION = 1;

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //The helper manages the database and defines the structure and behavior
    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_DATA_TABLE =

                " CREATE TABLE " + dataContract.DataEntry.TABLE_NAME + " (" +
                        dataContract.DataEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        dataContract.DataEntry.COLUMN_DATE + " TEXT, " +
                        dataContract.DataEntry.COLUMN_Pay + " TEXT, " +
                        dataContract.DataEntry.COLUMN_Int + " TEXT, " +
                        dataContract.DataEntry.COLUMN_Prin + " TEXT, " +
                        dataContract.DataEntry.COLUMN_Bal + " TEXT); ";

                db.execSQL(SQL_CREATE_DATA_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + dataContract.DataEntry.TABLE_NAME);
        onCreate(db);
    }

    public void refresh(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + dataContract.DataEntry.TABLE_NAME);
        onCreate(db);
    }
}

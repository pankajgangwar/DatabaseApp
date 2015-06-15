package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by iriemo on 20/5/15.
 */
public class MyDataBaseHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "message.db";

    public MyDataBaseHelper(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_TABLE_MESSAGE = "CREATE TABLE " + MyDataBaseContract.MessageEntry.TABLE_NAME + " (" +
                MyDataBaseContract.MessageEntry._ID + " INTEGER PRIMARY KEY, " +
                MyDataBaseContract.MessageEntry.COLUMN_MESSAGE_ID + " TEXT NOT NULL, " +
                MyDataBaseContract.MessageEntry.COLUMN_MESSAGE_TYPE + " TEXT NOT NULL, " +
                MyDataBaseContract.MessageEntry.COLUMN_MESSAGE_DATA + " TEXT NOT NULL, " +
                MyDataBaseContract.MessageEntry.COLUMN_MESSAGE_DATE + " TEXT NOT NULL );";

        db.execSQL(SQL_CREATE_TABLE_MESSAGE);
    }

    public boolean getItems(){
        SQLiteDatabase db = getReadableDatabase();
        final String SQL_QUERY = "SELECT * FROM " + MyDataBaseContract.MessageEntry.TABLE_NAME;
        Cursor cursor = db.rawQuery(SQL_QUERY, null);
        if(cursor.getCount() > 0){
            cursor.close();
            return true;
        } else{
            return  false;
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MyDataBaseContract.MessageEntry.TABLE_NAME);
        onCreate(db);
    }
}

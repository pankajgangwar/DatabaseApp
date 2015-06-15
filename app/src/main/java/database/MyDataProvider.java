package database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;

/**
 * Created by iriemo on 20/5/15.
 */
public class MyDataProvider extends ContentProvider {

    private static final int MESSAGE = 100;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MyDataBaseHelper dataBaseHelper;
    private static final String TAG = MyDataProvider.class.getSimpleName();

    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MyDataBaseContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, MyDataBaseContract.PATH_MESSAGE, MESSAGE);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        dataBaseHelper = new MyDataBaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor returnedCursor;

        switch (sUriMatcher.match(uri)) {
            case MESSAGE:
                returnedCursor = dataBaseHelper.getReadableDatabase().query(
                        MyDataBaseContract.MessageEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        returnedCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnedCursor;

    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MESSAGE:
                return MyDataBaseContract.MessageEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case MESSAGE: {
                long _id = db.insert(MyDataBaseContract.MessageEntry.TABLE_NAME, null, values);

                Log.i(TAG, "ID: " + _id);
                if (_id > 0) {
                    returnUri = MyDataBaseContract.MessageEntry.buildMessageUri(_id);
                } else {
                    throw new SQLiteException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(returnUri, null);
        return returnUri;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MESSAGE: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MyDataBaseContract.MessageEntry.TABLE_NAME, null, value);
                        Log.i(TAG, "ID: " + _id);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            }

            default:
                return super.bulkInsert(uri, values);
        }

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}

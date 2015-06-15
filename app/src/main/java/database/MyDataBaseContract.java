package database;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by iriemo on 20/5/15.
 */
public class MyDataBaseContract {

    public static final String CONTENT_AUTHORITY = "test.flatchat.myapplication";
    public static final Uri BASE_CONTENT_URI =Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MESSAGE = "message";

    public static final class MessageEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MESSAGE).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_MESSAGE;

        public static final String TABLE_NAME = "message";
        public static final String COLUMN_MESSAGE_TYPE = "message_type";
        public static final String COLUMN_MESSAGE_DATA = "message_data";
        public static final String COLUMN_MESSAGE_DATE = "message_date";
        public static final String COLUMN_MESSAGE_ID = "message_Id";


        public static Uri buildMessageUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }



}

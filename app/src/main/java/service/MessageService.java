package service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

import database.MyDataBaseContract;
import database.MyDataProvider;
import test.flatchat.myapplication.AppController;

/**
 * Created by iriemo on 20/5/15.
 */
public class MessageService extends IntentService {

    private static final String url = "http://pastebin.com/raw.php?i=aqziuquq";

    private static final String TAG = MessageService.class.getSimpleName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public MessageService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final Vector<ContentValues> contentValuesVector = new Vector<ContentValues>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONArray chatArray = jsonObject.getJSONArray("chats");
                            for (int i = 0; i < chatArray.length(); i++) {

                                JSONObject messageObject = chatArray.getJSONObject(i);

                                String timeStamp = messageObject.getString("timestamp");
                                String msg_id = messageObject.getString("msg_id");
                                String msg_data = messageObject.getString("msg_data");
                                String msg_type = messageObject.getString("msg_type");

                                ContentValues contentValues = new ContentValues();
                                contentValues.put(MyDataBaseContract.MessageEntry.COLUMN_MESSAGE_DATA, msg_data);
                                contentValues.put(MyDataBaseContract.MessageEntry.COLUMN_MESSAGE_DATE, timeStamp);
                                contentValues.put(MyDataBaseContract.MessageEntry.COLUMN_MESSAGE_ID, msg_id);
                                contentValues.put(MyDataBaseContract.MessageEntry.COLUMN_MESSAGE_TYPE, msg_type);

                                contentValuesVector.add(contentValues);

                            }

                            if (contentValuesVector.size() > 0) {
                                ContentValues[] cvArray = new ContentValues[contentValuesVector.size()];
                                contentValuesVector.toArray(cvArray);
                                int count = getContentResolver().bulkInsert(MyDataBaseContract.MessageEntry.CONTENT_URI, cvArray);

                                Log.i(TAG, "Count: " + count);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }


}

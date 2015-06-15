package test.flatchat.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import database.MyDataBaseContract;
import database.MyDataBaseHelper;
import service.MessageService;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int MESSAGE_LOADER = 0;
    MessageAdapter messageAdapter;
    ListView listView;
    MyDataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.listview);

        dataBaseHelper = new MyDataBaseHelper(this);

        if(!dataBaseHelper.getItems()) {
            startService(new Intent(this, MessageService.class));
        }

        messageAdapter = new MessageAdapter(this,null,0);
        listView.setAdapter(messageAdapter);

        getSupportLoaderManager().initLoader(MESSAGE_LOADER,null,this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(MESSAGE_LOADER,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        CursorLoader cursorLoader = new CursorLoader(this, MyDataBaseContract.MessageEntry.CONTENT_URI,null,null,null,null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        messageAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        messageAdapter.swapCursor(null);
    }
}

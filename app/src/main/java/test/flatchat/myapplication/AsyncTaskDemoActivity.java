package test.flatchat.myapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import service.Server;

/**
 * Created by iriemo on 2/6/15.
 */
public class AsyncTaskDemoActivity extends AppCompatActivity {

    TextView mTextView;
    Button startserviceBtn,registerBtn;
    Messenger messenger;
    boolean mBounded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_asyntaskdemo);
        mTextView = (TextView) findViewById(R.id.textView);
        startserviceBtn = (Button) findViewById(R.id.startserviceBtn);
        registerBtn = (Button)findViewById(R.id.registerserviceBtn);

        startserviceBtn.setOnClickListener(onClickListener);
        registerBtn.setOnClickListener(onClickListener);
//        new MyAsyncTaskDemo().execute();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            switch (v.getId()){

                case R.id.startserviceBtn:
                    Message message = Message.obtain(null, Server.SystemTime, null);
                    try {
                        messenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;

                case R.id.registerserviceBtn:
                    Message message1 = Message.obtain(null, Server.AddHandler, new ClientHandler());
                    try {
                        messenger.send(message1);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        bindService(new Intent(this, Server.class), connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
    }

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messenger = new Messenger(service);
            mBounded = true;
            Toast.makeText(AsyncTaskDemoActivity.this,"Service is connected",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBounded = false;
            messenger = null;
        }
    };

    public class ClientHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case Server.SystemTime:
                    mTextView.setText(msg.obj.toString());
                    break;

                case Server.AddHandler:
                    mTextView.setText(msg.obj.toString());
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }



   /* private class MyAsyncTaskDemo extends AsyncTask<Void,Void,Void>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            mTextView.setText("onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {

            Log.i(MyAsyncTaskDemo.class.getSimpleName(), "doInBackground");

            new MyAsyncTaskDemo().execute();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

//            mTextView.append("onPostExecute");

        }
    }*/


}

package service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by iriemo on 4/6/15.
 */
public class Server extends Service {

    Messenger messenger = new Messenger(new LocalHandler());
    Messenger clientMessenger;
   public static final int SystemTime = 0;
    public static final int AddHandler = 1;
    List<Handler> mHandlers;

    @Override
    public void onCreate() {
        super.onCreate();
        mHandlers = new ArrayList<Handler>();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }


    public class LocalHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case SystemTime:
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try{
                        clientMessenger.send(Message.obtain(null, SystemTime, dateFormat.format(new Date())));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;

                case AddHandler:

                    clientMessenger = new Messenger((Handler)msg.obj);
                    try {
                        clientMessenger.send(Message.obtain(null, AddHandler, "Registered messenger"));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    break;

                default:
                    break;
            }

            super.handleMessage(msg);
        }
    }
}

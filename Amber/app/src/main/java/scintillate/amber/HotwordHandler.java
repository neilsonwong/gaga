package scintillate.amber;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * Created by rin on 2/17/2017.
 */

public class HotwordHandler {
    private Context context;
    private Looper looper;
    private static final int HOTWORD_DETECTED = 1;
    private static final int LOOPER_STARTED = 0;
    private static final int LOGIN_FINISHED = 1;

    private static final String TAG = "LoginHandlerManager";

    public HotwordHandler(Context context, Looper looper) {
        this.context = context;
        this.looper = looper;
    }

    // Here's the interface that helps LoginService
    // behave as a callback object
    public interface HotwordCallback {
        public void onHotwordDetected();
    }

    public void start(final HotwordCallback callback) {
        // And here's that Handler.
        final Handler handler = new Handler(looper) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case HOTWORD_DETECTED:
                        callback.onHotwordDetected();
                        break;
//                    case LOOPER_STARTED:
//                        //Time to start the first step.
//                        // When it's done,
//                        // either synchronously or
//                        // asynchronously, send
//                        // the Handler a message
//                        // to run the next (or final) step.
//
//                        // whatever code you like, then...
//
//                        Message message = Message.obtain(h, LOGIN_FINISHED);
//                        // If your next step requires
//                        // any inputs from this step,
//                        // you can assign data to a Bundle
//                        // and send it with your message
//                        Bundle optionalData = new Bundle();
//                        optionalData.putString("MESSAGE_KEY", "Done!");
//                        message.setData(optionalData);
//
//                        // Now send it.
//                        sendMessage(message);
//
//                    case LOGIN_FINISHED:
//                        // Do we need any data
//                        // from the last message
//                        // to continue our work?
//                        // If so, grab it here.
//                        Bundle msgData = msg.getData();
//                        Log.i(TAG, msgData.getString("MESSAGE_KEY"));
//
//                        final Intent intent = new Intent();
//                        // You could bundle some result data with the Intent here, if you like!
//
//                        callback.onLoginSuccess(intent);
//                        break;
                }
            }
        };

    /*
     * Nothing will happen until
     * you post a message to start the Handler.
     */
        handler.post(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(LOOPER_STARTED);
            }
        });
    }
}
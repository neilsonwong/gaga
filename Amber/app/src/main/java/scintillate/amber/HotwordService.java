package scintillate.amber;

import android.Manifest;
import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ai.kitt.snowboy.SnowboyDetect;

/**
 * Created by rin on 2/11/2017.
 */

public class HotwordService extends Service {
    private static final String TAG = "Hotword Service";
    private boolean isRunning  = false;
    private SnowboyDetector detector;

    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");
        isRunning = true;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        handleStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //start message service
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        Log.i(TAG, "Service onStartCommand");
        //Creating new thread for my service
        //Always write your long running tasks in a separate thread, to avoid ANR
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Your logic that service will perform will be placed here
                //In this example we are just looping and waits for 1000 milliseconds in each loop.
                detector = SnowboyDetector.getInstance();
                detector.startRecord();
            }
        }).start();

        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // Not bound.
        return null;
    }

    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        //handler.removeCallbacks(runnable);
        super.onDestroy();
        //send message to activity saying stopped
        EventBus.getDefault().post(new MessageEvent("Hotword Service Destroyed", MessageEvent.MAIN_ACTIVITY));
        EventBus.getDefault().unregister(this);

    }

    private void handleStart(Intent intent, int startId){
        detector.startRecord();
        //send message to activity saying started
        EventBus.getDefault().post(new MessageEvent("Hotword Service Started", MessageEvent.MAIN_ACTIVITY));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.recipient == MessageEvent.HOTWORD_SERVICE) {
            Log.i(TAG, "event bus msg: " + event.message);
            Log.i(TAG, "event bus msg: " + event.eventCode);
            if (event.eventCode == MessageEvent.START_HOTWORD_EVENT){
                //start listening
                Log.i(TAG, "we heard it");
                detector.startRecord();
            }
        }
    }
}

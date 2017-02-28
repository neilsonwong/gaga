package scintillate.amber;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Locale;

//reference: https://github.com/WksKing/Android_Demo_Tools/blob/master/DemoTools/snowboy_demo/src/main/java/com/wistron/demo/tool/snowboy_demo/MainActivity.java

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main Activity";
    private static final String[] neededPermissions = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private SayStuff speaker;
    boolean permissionsYes = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        Log.i("Main", "onCreate");
        speaker = SayStuff.getInstance(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //check permissions
            int i = 0;
            for (; i < neededPermissions.length; i++) {
                if (checkSelfPermission(neededPermissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(neededPermissions, REQUEST_CODE_ASK_PERMISSIONS);
                    break;
                }
            }
            if (i >= neededPermissions.length) {
                permissionsYes = true;
            }
        } else {
            permissionsYes = true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        connectToHotwordService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speaker.shutdown();
        EventBus.getDefault().unregister(this);
    }

    private void connectToHotwordService(){
        // Bind to LocalService
        if (permissionsYes == true) {
            Intent intent = new Intent(this, HotwordService.class);
            startService(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i("Main", "onRequestPermissionsResult");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "You should agree all of the permissions, force exit! please retry", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
            }
            permissionsYes = true;
        }
    }

    public void listen(){
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toString());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, new Locale("yue", "HK").toString());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.JAPAN.toString());

        try {
            startActivityForResult(i, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(MainActivity.this, "Your device doesn't support Speech Recognition", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String inSpeech = res.get(0);
                if (ActionHandler.handle(inSpeech) == 1){
                    Log.e(TAG, "could not handle: " + inSpeech);
                    speaker.speak("i don't know how to handle " + inSpeech);
                }
                EventBus.getDefault().post(new MessageEvent("Plz start the hotwordz", MessageEvent.HOTWORD_SERVICE, MessageEvent.START_HOTWORD_EVENT));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.recipient == MessageEvent.MAIN_ACTIVITY) {
            Log.i(TAG, "event bus msg: " + event.message);
            if (event.eventCode == MessageEvent.HOTWORD_EVENT){
                hotwordDetected();
            }
        }
        else if (event.recipient == MessageEvent.ACTION_HANDLER){
            if (event.eventCode == MessageEvent.COMPUTER_AWAKE_EVENT){
                ActionHandler.computerAwake();
            }
        }
    };

    public void hotwordDetected() {
        //start the voice recognition
        //speak("Amber is awake");
        //speak("大家好");
        //speak("真木ちゃん凄い");

        listen();
    }
}
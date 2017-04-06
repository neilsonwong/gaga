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
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Locale;

import scintillate.amber.SpeechContext.Magic;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main Activity";
    private static final String[] neededPermissions = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private SayStuff speaker;
    private SoundPlayer soundPlayer;
    private Listener listener;
    private TextView t;
//    AudioManager mAudioManager;
    boolean permissionsYes = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        Log.i("Main", "onCreate");
        speaker = SayStuff.getInstance(this);
        soundPlayer = SoundPlayer.getInstance(this);
        listener = new Listener(this);
//        mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        Magic.init(this);
        t = (TextView)findViewById(R.id.txtBox_message);

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
        Log.i("Main Activity", "onStart");
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
    public void listen() {
        listen("amber");
    }

    public void listen(String hotword) {

        SpeechRecognizer speech = SpeechRecognizer.createSpeechRecognizer(this);
        String language;

        if (hotword.equals("asuka")){
            language = "ja_JP";
        }
        else {
//            language = "zh_HK";
            language = "en_US";
        }

        listener.setLang(language);
        speech.setRecognitionListener(listener);

        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, language);
        i.putExtra(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES, language);
        i.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, language);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, language);
        i.putExtra(RecognizerIntent.EXTRA_RESULTS, language);

        try {
//            mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            speech.startListening(i);
//            startActivityForResult(i, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(MainActivity.this, "Your device doesn't support Speech Recognition", Toast.LENGTH_SHORT).show();
        }

//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //check if intent has resolved
//                EventBus.getDefault().post(new MessageEvent("Plz start the hotwordz", MessageEvent.HOTWORD_SERVICE, MessageEvent.START_HOTWORD_EVENT));
//            }
//        }, 5000);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.recipient == MessageEvent.MAIN_ACTIVITY) {
            Log.i(TAG, "event bus msg: " + event.message);
            if (event.eventCode == MessageEvent.HOTWORD_EVENT){
                hotwordDetected(event.message.charAt(0));
            }
            else if (event.eventCode == MessageEvent.PRINT_TEXT){
                showText(event.message);
            }
        }
        else if (event.recipient == MessageEvent.ACTION_HANDLER){
            if (event.eventCode == MessageEvent.COMPUTER_AWAKE_EVENT){
                ActionHandler.computerAwake(event.message);
            }
        }
    };

    public void hotwordDetected(char lang) {
        //start the voice recognition
        //speak("Amber is awake");
        //speak("大家好");
        //speak("真木ちゃん凄い");
        if (lang == '2') {
            listen("asuka");
        }
        else {
            listen();
        }

//        Log.i(TAG, "Hotword Dectected!!!");
//        EventBus.getDefault().post(new MessageEvent("Plz start the hotwordz", MessageEvent.HOTWORD_SERVICE, MessageEvent.START_HOTWORD_EVENT));
    }

    private void showText(String text){
        t.setText(text);
    }


    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    private static boolean activityVisible;

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "main activity resumed");
        MainActivity.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "main activity paused");
        MainActivity.activityPaused();
    }
}
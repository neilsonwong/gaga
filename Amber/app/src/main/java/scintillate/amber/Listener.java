package scintillate.amber;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by rin on 3/22/2017.
 */

public class Listener implements RecognitionListener {
    private final String TAG = "LISTENER";
    private String language;
    private Context context;

    public Listener(Context c){
        context = c;
    }

    public void setLang(String lang){
        language = lang;
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
//        AudioManager mAudioManager = (AudioManager) this.context.getSystemService(Context.AUDIO_SERVICE);
//        mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
//        if (language.equals("ja_JP")) {
//            SoundPlayer.playThis("maki_yonda.mp3");
//        }
//        else {
//            SoundPlayer.playThis("maki_yes.mp3");
//        }
    }
    public void onBeginningOfSpeech()
    {
        Log.d(TAG, "onBeginningOfSpeech");
    }
    public void onRmsChanged(float rmsdB)
    {
        Log.d(TAG, "onRmsChanged");
    }
    public void onBufferReceived(byte[] buffer)
    {
        Log.d(TAG, "onBufferReceived");
    }
    public void onEndOfSpeech()
    {
        Log.d(TAG, "onEndofSpeech");
    }
    public void onError(int error)
    {
        Log.d(TAG,  "error " +  error);
        SoundPlayer.playThis("maki_nani.mp3");
        //error occurred, we will start over from the beginning
        EventBus.getDefault().post(new MessageEvent("Plz start the hotwordz", MessageEvent.HOTWORD_SERVICE, MessageEvent.START_HOTWORD_EVENT));
    }
    public void onResults(Bundle results)
    {
        /*
        ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String inSpeech = res.get(0);
                int runStatus = ActionHandler.handle(inSpeech);
                if (runStatus > 0){
                    Log.e(TAG, "could not handle: " + inSpeech);
//                    speaker.speak("i don't know how to handle " + inSpeech, SayStuff.getLang(runStatus));
                    Log.e(TAG, "cur lang " + SayStuff.getLang(runStatus));
                    speaker.speak(inSpeech, SayStuff.getLang(runStatus));
                }
                EventBus.getDefault().post(new MessageEvent("Plz start the hotwordz", MessageEvent.HOTWORD_SERVICE, MessageEvent.START_HOTWORD_EVENT));
         */


        String str = new String();
        Log.d(TAG, "results  are in!");
        ArrayList res = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (res.size() > 0){
            //we have a result
            String inSpeech = (String)res.get(0);
            Log.i(TAG, "i heard : " + inSpeech);
            int runStatus = ActionHandler.handle(inSpeech);
            if (runStatus > 0){
                Log.e(TAG, "could not handle: " + inSpeech);
                Log.e(TAG, "cur lang " + SayStuff.getLang(runStatus));
                Log.e(TAG, "char0: " + inSpeech.charAt(0));

                int cp = (int)inSpeech.charAt(0);
                if (cp >= 97 && cp <= 122 || cp >= 65 && cp <= 90){
                    //english
                    SayStuff.justSayThis("Sorry, I don't understand " + inSpeech, SayStuff.getLang(1));
                }
                else if (language == "ja_JP"){
                    //japanese
                    SayStuff.justSayThis(inSpeech + "がわかりません", SayStuff.getLang(3));
                }
                else {
                    //canto
                    SayStuff.justSayThis("我唔明" + inSpeech, SayStuff.getLang(2));
                }
            }
        }

        EventBus.getDefault().post(new MessageEvent("Plz start the hotwordz", MessageEvent.HOTWORD_SERVICE, MessageEvent.START_HOTWORD_EVENT));
        /*
        for (int i = 0; i < res.size(); i++)
        {
            Log.d(TAG, "result " + res.get(i));
            str += res.get(i);
        }
        */
    }

    public void onPartialResults(Bundle partialResults)
    {
        Log.d(TAG, "onPartialResults");
    }

    public void onEvent(int eventType, Bundle params)
    {
        Log.d(TAG, "onEvent " + eventType);
    }
}

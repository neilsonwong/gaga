package scintillate.amber;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

/**
 * Created by rin on 2/18/2017.
 */

public class SayStuff {
    private TextToSpeech[] tts = new TextToSpeech[3];
    private static SayStuff instance = null;

    public SayStuff(Context context){
        Log.i("SR", "init tts");
        tts[0] = initTTS(context, Locale.US, 0);
        tts[1] = initTTS(context, new Locale("yue", "HK"), 1);
        tts[2] = initTTS(context, Locale.JAPANESE, 2);
    }

    public static SayStuff getInstance(Context context){
        if (instance == null){
            instance = new SayStuff(context);
        }
        return instance;
    }

    public static void justSayThis(String sayit, String language){
        if(instance != null){
            instance.speak(sayit, language);
        }
    }

    private TextToSpeech initTTS(Context context, final Locale language, final int ref){
        return new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts[ref].setLanguage(language);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }
                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });
    }

    public void speak(String text){
        speak(text, "EN");
    }

    public void speak(String text, String language){
        TextToSpeech correctTTS;
        switch(language){
            case "HK":
                correctTTS = tts[1];
                break;
            case "JP":
                correctTTS = tts[2];
                break;
            case "EN":
            default:
                correctTTS = tts[0];
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            correctTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }else{
            correctTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public void shutdown(){
        tts[0].shutdown();
        tts[1].shutdown();
        tts[2].shutdown();
    }
}

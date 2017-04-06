package scintillate.amber.dispatchers;

import scintillate.amber.SayStuff;
import scintillate.amber.SoundPlayer;
import scintillate.amber.SpeechContext.Command;

/**
 * Created by rin on 2/26/2017.
 */

public class FunDispatcher {

    public static String getApp(){
        return "fun";
    }

    public static int handle(String cmd){
        int runStatus = 0;
        switch(cmd){
            case "amelia":
            case "emilia":
                emilia();
                break;
            case "hello":
                hkHello();
                break;
            case "maki":
                maki();
                break;
            case "eugene":
                eugene();
                break;
            case "必殺":
                hissatsu();
                break;
            case "sing a song":
            case "sing me a song":
                sing();
                break;
            default:
                runStatus = 1;
        }
        return runStatus;
    }

    public static void emilia(){
        SayStuff.justSayThis("Emilia is the best, yea I know", "EN");
    }

    public static void hkHello() {
        SayStuff.justSayThis("大家好", "HK");
    }

    public static void maki() {
        SayStuff.justSayThis("真木ちゃん凄い", "JP");
    }

    public static void eugene() {
        SoundPlayer.playThis("genji.ogg");
    }

    public static void hissatsu() {
        SoundPlayer.playThis("kamina.mp3");
    }

    public static void sing() {
        SoundPlayer.playThis("soybean.mp3");
    }
}

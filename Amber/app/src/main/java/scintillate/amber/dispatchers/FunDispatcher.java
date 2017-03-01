package scintillate.amber.dispatchers;

import scintillate.amber.SayStuff;
import scintillate.amber.SpeechContext.Command;

/**
 * Created by rin on 2/26/2017.
 */

public class FunDispatcher {

    public static String getApp(){
        return "fun";
    }

    public static int handle(Command c){
        return 0;
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
}

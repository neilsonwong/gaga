package scintillate.amber.SpeechContext;

import java.util.HashMap;

/**
 * Created by rin on 3/2/2017.
 */

public class CantoHandler {
    public static HashMap<String, String> simple = new HashMap<String, String>();
    public static boolean init = false;

    public static String handle(String input){
        if (!init){
            init();
        }
        String fakeTranslate = "";
        String word = "";
        String twoWords;

        int i = 0;
        for (i = 0; i < input.length()-1; ++i){
            word = input.substring(i, i+1);
            twoWords = input.substring(i, i+2);

            if (simple.containsKey(word)){
                fakeTranslate += simple.get(word) + " ";
            }

            if (simple.containsKey(twoWords)){
                fakeTranslate += simple.get(twoWords) + " ";
            }
        }

        word = input.substring(input.length()-1, input.length());
        if (simple.containsKey(word)){
            fakeTranslate += simple.get(word) + " ";
        }

        return fakeTranslate.trim();
    }

    public static boolean isLang(String input){
        if (!init){
            init();
        }
        int i;
        String word;
        for (i = 0; i < input.length()-1; ++i) {
            word = input.substring(i, i + 1);
            if (simple.containsKey(word)) {
                return true;
            }
        }
        return false;
    }

    private static void init(){
        simple.put("播", "play");
        simple.put("音樂", "music");
        simple.put("停", "pause");
        simple.put("下", "next");
        simple.put("上", "previous");
        simple.put("歌", "song");
        simple.put("大聲", "louder");
        simple.put("細聲", "softer");
        simple.put("開機", "turn on computer");
        simple.put("閂機", "turn off computer");
        simple.put("生機", "turn off computer");
        init = true;
    }
}

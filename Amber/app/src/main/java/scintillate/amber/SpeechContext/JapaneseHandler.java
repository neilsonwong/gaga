package scintillate.amber.SpeechContext;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by rin on 3/18/2017.
 */

public class JapaneseHandler {
    public static HashMap<String, String> simple = new HashMap<String, String>();
    public static HashSet<Character> kana = new HashSet<Character>();
    public static boolean init = false;

    public static String handle(String input){
        if (!init){
            init();
        }
        String fakeTranslate = input.replaceAll("プレイ", "play ");

        return fakeTranslate.trim();
    }

    public static boolean isLang(String input){
        if (!init){
            init();
        }
        int i;
        Character word;
        for (i = 0; i < input.length(); ++i) {
            word = input.charAt(i);
            if (kana.contains(word)){
                return true;
            }
        }
        return false;
    }

    private static void init(){
        simple.put("プレイ", "play");

        //init kana
        String kanaString = "ぁあぃいぅうぇえぉおかがきぎくぐけげこごさざしじすずせぜそぞただちぢっつづてでとど" +
                "なにぬねのはばぱひびぴふぶぷへべぺほぼぽまみむめもゃやゅゆょよらりるれろゎわゐゑをんゔ" +
                "ゕゖァアィイゥウェエォオカガキギクグケゲコゴサザシジスズセゼソゾタダチヂッツヅテデトド" +
                "ナニヌネノハバパヒビピフブプヘベペホボポマミムメモャヤュユョヨラリルレロヮワ" +
                "ヰヱヲンヴヵヶヷヸヹヺ";
        int i;
        for (i = 0; i < kanaString.length(); ++i){
            kana.add(kanaString.charAt(i));
        }

        init = true;
    }
}

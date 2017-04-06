package scintillate.amber.SpeechContext;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

/**
 * Created by rin on 3/27/2017.
 */

public class Magic {
    //magic doesn't exist in this world
    //this is just a load of hack

    static boolean initialized = false;
    static HashSet<String> titles;
    static HashSet<String> artists;

    public static void init(Context c){
        initialized = true;
        titles = new HashSet<String>();
        artists = new HashSet<String>();

        AssetManager am = c.getAssets();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(am.open("anime-titles.min.txt"), "UTF-8"));

            // do reading, usually loop until end of file reading
            String mLine;
            String title;
            String[] parts;
            int i;
            while ((mLine = reader.readLine()) != null) {
                //process line
                if (mLine.charAt(0) == '#'){
                    continue;
                }
//                title = standardize(mLine.split("\\|")[3]);
                title = mLine;
//                System.out.println(mLine);
//                titles.add(standardize(title));
                titles.add(title);
                if (title.indexOf(" ") > 0){
                    parts = title.split(" ");
                    for (i = 0; i < parts.length; ++i){
                        title = parts[i].trim();
                        if (title.length() > 3){
                            titles.add(title);
                        }
                    }
                }

            }
        } catch (Exception e) {
            //log the exception
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }

        try {
            reader = new BufferedReader(
                    new InputStreamReader(am.open("artists.txt"), "UTF-8"));

            // do reading, usually loop until end of file reading
            String artist;
            while ((artist = reader.readLine()) != null) {
                artists.add(artist.toLowerCase());
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }

        if (artists.contains("supercell")){
            System.out.println("supercell is here");
        }
        if (titles.contains("escaflowne")){
            System.out.println("Escaflowne is here");
        }
    }

    public static MagicalObject resolve(String input){
        if (!initialized){
            return null;
        }
        //filter garbage
        String filtered = filterGarbage(input);
        System.out.println("f '" + filtered + "'");
        if (titles.contains(standardize(filtered))){
            System.out.println("found '" + filtered + "' in titles");
            return new MagicalObject("video", fixNumbers(input));
        }
        else if (artists.contains(filtered)){
            System.out.println("found " + filtered + " in artists");
            return new MagicalObject("music", input);
        }
        return null;
    }

    private static String standardize(String input){
        return input.toLowerCase().replaceAll("[_-]", " ");
    }

    //super crappy filter lol
    private static String filterGarbage(String input){
        //handle extra words
        String[] splits = input.split(" ");
        String filtered = "";
        int i = 0;
        if (splits[0].equals("some")){
            i = 1;
        }

        for (; i < splits.length; ++i){
            if (splits[i].equals("episode") ||  splits[i].equals("ep")){
                break;
            }
            else {
                filtered += splits[i] + " ";
            }
        }
        return stripNumbers(filtered.trim());
    }

    private static String stripNumbers(String input){
        String good = "";
        String[] splits = input.split(" ");
        int i = 0;
        for (; i < splits.length; ++i){
            if (splits[i].matches("\\d+")){
                continue;
            }
            switch(splits[i]){
                case "one":
                case "two":
                case "three":
                case "four":
                case "for":
                case "five":
                case "six":
                case "seven":
                case "eight":
                case "nine":
                case "zero":
                    continue;
                default:
                    good += splits[i] + " ";
            }
        }
        return good.trim();
    }

    private static String fixNumbers(String input){
        String[] splits = input.split(" ");
        String good = "";
        int i = 0;
        boolean epFlag = false;
        for (; i < splits.length; ++i){
            switch(splits[i]){
                case "episode":
                case "ep":
                    epFlag = true;
                    break;
                case "one":
                    if (epFlag)
                        good += 1 + " ";
                    break;
                case "two":
                    if (epFlag)
                        good += 2 + " ";
                    break;
                case "three":
                    if (epFlag)
                        good += 3 + " ";
                    break;
                case "four":
                case "for":
                    if (epFlag)
                        good += 4 + " ";
                    break;
                case "five":
                    if (epFlag)
                        good += 5 + " ";
                    break;
                case "six":
                    if (epFlag)
                        good += 6 + " ";
                    break;
                case "seven":
                    if (epFlag)
                        good += 7 + " ";
                    break;
                case "eight":
                    if (epFlag)
                        good += 8 + " ";
                    break;
                case "nine":
                    if (epFlag)
                        good += 9 +  " ";
                    break;
                case "zero":
                    if (epFlag)
                        good += 0 + " ";
                    break;
                default:
                    epFlag = false;
                    good += splits[i] + " ";
            }
        }
        return good;
    }

    static class MagicalObject{
        //lol this is just a fancy tuple
        String app;
        String target;
        public MagicalObject(String a, String t){
            app = a;
            target = t;
        }
    }
}

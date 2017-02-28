package scintillate.amber.dispatchers;

import scintillate.amber.HttpTask;

/**
 * Created by rin on 2/26/2017.
 */

public class ClementineDispatcher {
    public static String getApp(){
        return "Clementine";
    }

    public static void play(){
        exec("play");
    }

    public static void pause(){
        exec("pause");
    }

    public static void next(){
        exec("next");
    }

    public static void prev(){
        exec("prev");
    }

    public static void volUp(){
        exec("vol%20up");
    }

    public static void volDown(){
        exec("vol%20down");
    }

    private static void exec(String task){
        new HttpTask("http://192.168.0.113:3000/clementine/"+task).execute();
    }
}

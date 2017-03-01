package scintillate.amber.dispatchers;

import java.util.HashMap;

import scintillate.amber.HttpTask;
import scintillate.amber.SpeechContext.Command;

/**
 * Created by rin on 2/26/2017.
 */

public class MediaPlayerClassicDispatcher {
    //SET_VOLUME: -2,       later
    //SEEK: -1,         later

    public static String getApp(){
        return "video";
    }

    public static int handle(Command c){
        return 0;
    }

    public static void openFile(String file){
        HashMap<String, String> extras = new HashMap<String, String>();
        extras.put("filename", file);
        exec("open_file", extras);
    }

    public static void playPause(){
        exec("play_pause");
    }

    public static void stop(){
        exec("stop");
    }

    public static void nextFile(){
        exec("next_file");
    }

    public static void previousFile(){
        exec("previous_file");
    }

    public static void close(){
        exec("close");
    }

    public static void exit(){
        exec("exit");
    }

    public static void fullscreen(){
        exec("fullscreen");
    }

    public static void screencap(){
        exec("save_image");
    }

    public static void jumpToStart(){
        exec("jump_to_beginning");
    }

    public static void jumpForwardSmall(){
        exec("jump_forward_small");
    }

    public static void jumpBackwardSmall(){
        exec("jump_backward_small");
    }

    public static void jumpForwardMedium(){
        exec("jump_forward_medium");
    }

    public static void jumpBackwardMedium(){
        exec("jump_backward_medium");
    }

    public static void jumpForwardBig(){
        exec("jump_forward_big");
    }

    public static void jumpBackwardBig(){
        exec("jump_backward_big");
    }

    public static void next_subtitle(){
        exec("goto_next_subtitle");
    }

    public static void next_audio(){
        exec("next_audio");
    }


    private static void exec(String task){
        new HttpTask("http://192.168.0.113:3000/mpc/"+task).execute();
    }

    private static void exec(String task, HashMap<String, String> extras){
        new HttpTask("http://192.168.0.113:3000/mpc/"+task, extras).execute();
    }
}

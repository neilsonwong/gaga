package scintillate.amber;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;

import scintillate.amber.SpeechContext.Command;
import scintillate.amber.SpeechContext.FragmentChain;
import scintillate.amber.dispatchers.*;

import static scintillate.amber.ActionHandler.ACTION_WORDS;
import static scintillate.amber.ActionHandler.IGNORE;
import static scintillate.amber.ActionHandler.NOUN;

/**
 * Created by rin on 2/18/2017.
 */

public class ActionHandler {
    //contextual properties
    private static String TAG = "Action Handler";
    private static HandlerContext currentContext = null;

    public static int handle(String command) {
        Log.i(TAG, "handling: " + command);
        FragmentChain fc = new FragmentChain(command);
        Command c = fc.getCommand(currentContext);
        return interpretCommand(c);
    }

    public static int interpretCommand(Command c){
        int res = 0;
        switch (c.getApp()){
            case "music":
                res = ClementineDispatcher.handle(c);
                currentContext.currentApplication = ClementineDispatcher.getApp();
                break;
            case "computer":
                res = ComputerDispatcher.handle(c);
                currentContext.currentApplication = ComputerDispatcher.getApp();
                break;
            case "video":
                res = MediaPlayerClassicDispatcher.handle(c);
                currentContext.currentApplication = MediaPlayerClassicDispatcher.getApp();
                break;
            case "fun":
                res = FunDispatcher.handle(c);
                currentContext.currentApplication = FunDispatcher.getApp();
                break;
            default:
                res = 0;
        }
        return res;
    }

    public class HandlerContext {
        public String currentApplication;

        public HandlerContext(){}
    }
}



/*
class CommandContext {
    public String commandText;
    public String action = null;
    public String target = null;
    public ArrayList<String> extras = null;

    public CommandContext(String cmd){
        this.commandText = cmd;
        extras = new ArrayList<String>();
        understand();
    }

    private void understand(){
        String[] fragments = this.commandText.split(" ");

        String curPart = "";
        String word = "";
        int i = 0;
        int actionable, targetable;
        Log.i("CC", "len: "+ fragments.length);
        while (i < fragments.length){
            Log.i("CC", "index: "+ i);
            //for each of the fragments try to understand it
            word = fragments[i];
            if (isIgnore(word)){
                Log.i("CC", "ignored: "+ word);
                ++i;
                if (!curPart.isEmpty()){
                    extras.add(curPart.trim());
                    curPart = "";
                }
                continue;
            }

            actionable = isAction(word, i, fragments);
            Log.i("CC", "actionable: "+ word + " > " + actionable);
            if (actionable > -1){
                //it's an action, deal with it
                i += actionable;
                ++i;
                if (!curPart.isEmpty()){
                    extras.add(curPart.trim());
                    curPart = "";
                }
                continue;
            }

            //handle targets
            targetable = isTarget(word, i, fragments);
            Log.i("CC", "targetable: "+ word + " > " + targetable);
            if (targetable > -1){
                //it's a target, deal with it
                i += targetable;
                ++i;
                if (!curPart.isEmpty()){
                    extras.add(curPart.trim());
                    curPart = "";
                }
                continue;
            }

            //handle misc
            curPart += " " + word;
            ++i;
        }
        if (!curPart.isEmpty()){
            extras.add(curPart.trim());
            curPart = "";
        }
    }

    //return -1 if not
    //return 0 if yes
    //return shift i if multipart yes
    private int isAction(String word, int i, String[] fragments){
        int ret = -1;
        if (ACTION_WORDS.contains(word)){
            switch(word){
                //deal with multiWord
                case "shut":
                    if (i+1 < fragments.length){
                        if (fragments[i+1].equals("down")) {
                            setAction(word + " " + "down");
                            ret = 1;
                        }
                    }
                    break;
                case "turn":
                case "power":
                    if (i+1 < fragments.length) {
                        if (fragments[i + 1].equals("on")){
                            setAction(word + " " + "on");
                            ret = 1;
                        }
                        else if (fragments[i + 1].equals("off")){
                            setAction(word + " " + "off");
                            ret = 1;
                        }
                    }
                    break;
                case "full":
                    if (i+1 < fragments.length && fragments[i+1].equals("screen")){
                        setAction("fullscreen");
                        ret = 1;
                    }
                    break;
                case "go":
                    if (i+1 < fragments.length) {
                        if (fragments[i + 1].equals("back")){
                            setAction(word + " " + "back");
                            ret = 1;
                        }
                    }
                    else if (i+3 < fragments.length &&
                            fragments[i+1].equals("to") &&
                            fragments[i+1].equals("the") &&
                            fragments[i+2].equals("beginning")){
                        setAction(word + " to the beginning");
                        ret = 3;
                    }
                    else if (i+3 < fragments.length &&
                            fragments[i+1].equals("to") &&
                            fragments[i+1].equals("the") &&
                            fragments[i+2].equals("start")) {
                        setAction(word + " to the start");
                        ret = 3;
                    }
                    break;
                case "volume":
                    if (i+1 < fragments.length) {
                        if (fragments[i + 1].equals("up")){
                            setAction(word + " " + "up");
                            ret = 1;
                        }
                        else if (fragments[i + 1].equals("down")){
                            setAction(word + " " + "down");
                            ret = 1;
                        }
                    }
                    break;
                case "switch":
                    if (i+1 < fragments.length && fragments[i+1].equals("audio")){
                        setAction("switch audio");
                        ret = 1;
                    }
                    break;
                default:
                    setAction(word);
                    ret = 0;
            }
        }
        return ret;
    }

    private int isTarget(String word, int i, String[] fragments){
        int ret = -1;
        if (NOUN.contains(word)){
            switch(word){
                case "last":
                    if (i+1 < fragments.length) {
                        if (fragments[i + 1].equals("song")){
                            setTarget(word + " " + "song");
                            ret = 1;
                        }
                    }
                    break;
                case "music":
                case "song":
                    setTarget("clementine");
                    ret = 0;
                    break;
                case "ep":
                case "episode":
                case "subtitle":
                case "audio":
                    setTarget("mpc");
                    ret = 0;
                    break;
                case "media":
                    //mpc
                    if (i+2 < fragments.length &&
                            fragments[i+1].equals("player") &&
                            fragments[i+2].equals("classic")){
                        setTarget("mpc");
                        ret = 2;
                    }
                    break;
                case "computer":
                    setTarget("computer");
                    ret = 0;
                    break;
                case "file":
                case "playing":
                    //ambiguous
                    break;
                default:
                    ret = 0;
            }
        }
        return ret;
    }

    private boolean isIgnore(String word) {
        return IGNORE.contains(word);
    }

    public boolean setAction(String action){
        if (this.action == null){
            this.action = action;
            return true;
        }
        return false;
    }

    public boolean setTarget(String target){
        if (this.target == null){
            this.target = target;
            return true;
        }
        return false;
    }
}
*/
package scintillate.amber;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

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
    public static String mostRecentApplication;
    public static LinkedList<CommandContext> commandQueue = new LinkedList<CommandContext>();

    public static int handle(String command){
        Log.i(TAG, "handling: " + command);
        //get the context of the command
        CommandContext cc = new CommandContext(command);
        Log.i(TAG, "extras: " + cc.extras.size());

        //dispatch to appropriate command
        return dispatch(cc);
    }


    private static String findTargetFromExtras(ArrayList<String> extras){
        //if nothing in target, try to use current application
        if (extras.isEmpty()){
            return mostRecentApplication;
        }
        return null;
    }

    public static void computerAwake(){
        CommandContext cc = commandQueue.poll();
        if (cc != null){
            dispatch(cc);
        }
    }

    private static int dispatch(CommandContext cc){
        Log.i(TAG, "dispatching " + cc.action);
        Log.i(TAG, "dispatching " + cc.target);
//        Log.i(TAG, "dispatching " + cc.extras.get(0));
        if (cc.target == null){
            String newTarget = findTargetFromExtras(cc.extras);
            cc.setTarget(newTarget == null ? "nothing" : newTarget);
        }
        switch(cc.action){
            case "play":
                //play what
                if (cc.target.equals("clementine")){
                    if (cc.extras.isEmpty()){
                        ClementineDispatcher.play();
                    }
                    //deal with artists, files later
                }
                else if (cc.target.equals("mpc")){
                    if (cc.extras.isEmpty()){
                        MediaPlayerClassicDispatcher.playPause();
                    }
                    else if (cc.extras.size() == 1){
                        MediaPlayerClassicDispatcher.openFile(cc.extras.get(0));
                    }
                }
                else if (cc.target.equals("last song")){
                    ClementineDispatcher.prev();
                }
                break;
            case "stop":
            case "pause":
                if (cc.target.equals("clementine")){
                    ClementineDispatcher.pause();
                }
                else if (cc.target.equals("mpc")){
                    MediaPlayerClassicDispatcher.playPause();
                }
                break;
            case "next":
            case "skip":
                if (cc.target.equals("clementine")){
                    ClementineDispatcher.next();
                }
                else if (cc.target.equals("mpc")){
                    MediaPlayerClassicDispatcher.nextFile();
                }
                break;
            case "last":
            case "previous":
                if (cc.target.equals("clementine")){
                    ClementineDispatcher.prev();
                }
                else if (cc.target.equals("mpc")){
                    MediaPlayerClassicDispatcher.previousFile();
                }
                break;
            case "go back":
                ClementineDispatcher.prev();
                break;
            case "louder":
            case "volume up":
                if (cc.target.equals("clementine")){
                    ClementineDispatcher.volUp();
                }
                else if (cc.target.equals("mpc")){
                    //too bad
                }
                break;
            case "softer":
            case "volume down":
                if (cc.target.equals("clementine")){
                    ClementineDispatcher.volDown();
                }
                else if (cc.target.equals("mpc")){
                    //too bad
                }
                break;
            case "open":
                //we are depending on find target from extras
//                if (cc.target.equals("clementine")){
                      //too bad
//                }
//                if (cc.target.equals("mpc")){
                if (cc.extras.size() > 0){
                    MediaPlayerClassicDispatcher.openFile(cc.extras.get(0));
                }
                break;
            case "go to the beginning":
            case "go to the start":
                if (cc.target.equals("clementine")){
                    ClementineDispatcher.play();
                }
                else if (cc.target.equals("mpc")){
                    MediaPlayerClassicDispatcher.jumpToStart();
                }
                break;
            case "jump ahead":
                //TODO: LATER
                break;
            case "screenshot":
                MediaPlayerClassicDispatcher.screencap();
                break;
            case "fullscreen":
            case "full screen":
                MediaPlayerClassicDispatcher.fullscreen();
                break;
            case "close":
            case "exit":

                break;
            case "shutdown":
            case "shut down":
            case "turn off":
            case "power off":
                if (cc.target.equals("computer")){
                    ComputerDispatcher.turnOff();
                }
                break;
            case "turn on":
            case "power on":
                if (cc.target.equals("computer")){
                    ComputerDispatcher.turnOn();
                }
                break;
            case "switch audio":
                ComputerDispatcher.switchAudio();
                break;
            default:
                return 1;
        }
        return 0;
    }

    public static ArrayList<String> ACTION_WORDS = new ArrayList<String>(Arrays.asList(
        "play",
        "pause",
        "stop",
        "next",
        "skip",
        "previous",
        "go",
        "louder",
        "softer",
        "volume",
        "open",
        "jump",
        "screenshot",
        "fullscreen",
        "close",
        "exit",
        "shutdown",
        "switch",

        //commented means multipart
        "shut", //down
        "turn", //on, off
        "power",    //on
        "full" //screen
    ));

    public static ArrayList<String> MODIFIER = new ArrayList<String>(Arrays.asList(
        "down",
        "up",
        "on",
        "off",
        "track"
    ));

    public static ArrayList<String> NOUN = new ArrayList<String>(Arrays.asList(
        "music",
        "last",
        "song",
        "ep",
        "episode",
        "computer",
        "file",
        "media", // player classic
        "playing",
        "subtitle",
        "audio" //track
    ));

    public static ArrayList<String> IGNORE = new ArrayList<String>(Arrays.asList(
        "my",
        "the",
        "this",
        "some"
    ));

    public static ArrayList<String> MISC_WORDS = new ArrayList<String>(Arrays.asList(
        "amelia",
        "emilia",
        "hello"
    ));
}

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
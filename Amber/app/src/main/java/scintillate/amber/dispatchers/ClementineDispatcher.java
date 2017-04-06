package scintillate.amber.dispatchers;

import android.os.Handler;

import scintillate.amber.ActionHandler;
import scintillate.amber.AliveTask;
import scintillate.amber.HttpTask;
import scintillate.amber.OnAlive;
import scintillate.amber.Settings;
import scintillate.amber.SpeechContext.Command;

/**
 * Created by rin on 2/26/2017.
 */

public class ClementineDispatcher implements OnAlive {
    public static String getApp(){
        return "music";
    }
    public static Command onDeck = null;

    public ClementineDispatcher(){}

    public static int handle(Command c){
        System.out.println("music");

        //check if command is valid
        if (runCommand(c, true)){
            //ensure computer is on
            computerAwake();
            onDeck = c;

            return 0;
        }
        return 1;
    }

    private static boolean runCommand(Command c){
        return runCommand(c, false);
    }
    private static boolean runCommand(Command c, boolean dummy){
        //run command
        switch (c.getAction()){
            case "play":
                if (!dummy)
                    play();
                break;
            case "stop":
            case "pause":
                if (!dummy)
                    pause();
                break;
            case "next":
                if (!dummy)
                    next();
                break;
            case "previous":
                if (!dummy)
                    prev();
                break;
            case "louder":
                if (!dummy)
                    volUp();
                break;
            case "softer":
                if (!dummy)
                    volDown();
                break;
            default:
                return false;
        }
        return true;
    }
    private static void play(){
        exec("play");
    }

    private static void pause(){
        exec("pause");
    }

    private static void next(){
        exec("next");
    }

    private static void prev(){
        exec("prev");
    }

    private static void volUp(){
        exec("vol%20up");
    }

    private static void volDown(){
        exec("vol%20down");
    }

    private static void exec(String task){
        new HttpTask(Settings.PC_COM_SERVER + "clementine/"+task).execute();
    }

    private static void computerAwake(){
        new AliveTask(new ClementineDispatcher()).execute();
    }

    @Override
    public void onAlive(String result) {
        System.out.println(result);
        if (result.indexOf("living") == 0){
            //turn off pending cancel or something
            if (onDeck != null)
                runCommand(onDeck);
            onDeck = null;
        }
        else if (result.equals("failed")) {
            //computer was not reachable
            ComputerDispatcher.turnOn();
            ActionHandler.pushPending(onDeck, "");
            onDeck = null;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    computerAwake();
                    System.out.println("waiting for computer to turn on");
                }
            }, 15000);
        }
    }
}

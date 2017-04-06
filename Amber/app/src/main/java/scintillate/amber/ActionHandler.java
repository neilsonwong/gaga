package scintillate.amber;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import scintillate.amber.SpeechContext.CantoHandler;
import scintillate.amber.SpeechContext.Command;
import scintillate.amber.SpeechContext.FragmentChain;
import scintillate.amber.SpeechContext.JapaneseHandler;
import scintillate.amber.dispatchers.*;

/**
 * Created by rin on 2/18/2017.
 */

public class ActionHandler {
    //contextual properties
    private static String TAG = "Action Handler";
    private static HandlerContext currentContext = new HandlerContext();
    private static LinkedList<PendingCommand> pending = new LinkedList<PendingCommand>();
    private static int curLang = 1;
    private static final ReentrantLock lock = new ReentrantLock();

    public static int handle(String command) {
        curLang = 1;
        Log.i(TAG, "recieved: " + command);
        String parseString = tryLangs(command);

        Log.i(TAG, "handling: " + parseString);


        FragmentChain fc = new FragmentChain(parseString);
        Command c = fc.getCommand(currentContext);
        Log.i(TAG, "command : " + c);

        EventBus.getDefault().post(new MessageEvent("Handling: " + parseString + "\n" + c, MessageEvent.MAIN_ACTIVITY, MessageEvent.PRINT_TEXT));

        return interpretCommand(c, command);
    }

    public static int interpretCommand(Command c, String originalText){
        int res = 1;
        if (c.getAction() != null && c.getApp() != null) {
            switch (c.getApp()) {
                case "music":
                    res = ClementineDispatcher.handle(c);
                    currentContext.currentApplication = ClementineDispatcher.getApp();
                    break;
                case "computer":
                    res = ComputerDispatcher.handle(c);
//                    currentContext.currentApplication = ComputerDispatcher.getApp();
                    break;
                case "video":
                    res = MediaPlayerClassicDispatcher.handle(c);
                    currentContext.currentApplication = MediaPlayerClassicDispatcher.getApp();
                    break;
                case "fun":
                default:
                    //fun handles everything we don't normally handle :D
                    res = FunDispatcher.handle(originalText);
            }
        }
        else {
            if (NetworkDispatcher.dry(originalText)){
                res = NetworkDispatcher.handle(originalText);
            }
            else {
                res = FunDispatcher.handle(originalText);
            }
        }
        Log.e(TAG, "interpretCommand: " + curLang);
        return res*curLang;
    }

    public static void computerAwake(String currentApplication){
        //do what i need to do when computer is awake
        lock.lock();  // block until condition holds
        try {
            ComputerDispatcher.turningOn = false;

            //set current application
            currentContext.currentApplication = currentApplication;
            PendingCommand pcmd;
            while (!pending.isEmpty()){
                pcmd = pending.pop();
                System.out.println("running pending command");
                //deal with race conditions
                if (pcmd != null)
                    interpretCommand(pcmd.c, pcmd.text);
            }
        }
        finally {
            lock.unlock();
        }
    }

    public static class HandlerContext {
        public String currentApplication;

        public HandlerContext(){}
    }

    public static void pushPending(Command c, String text){
        pending.add(new PendingCommand(text, c));
    }

    private static String tryLangs(String command){
        //try canto
        if (CantoHandler.isLang(command)) {
            String cantoString = CantoHandler.handle(command);
            curLang = 2;
            if (!cantoString.isEmpty()) {
                return cantoString;
            }
        }

        //try japanese
        if (JapaneseHandler.isLang(command)) {
            String japanString = JapaneseHandler.handle(command);
            curLang = 3;
            if (!japanString.isEmpty()) {
                return japanString;
            }
        }

        return command;
    }
}

class PendingCommand {
    Command c;
    String text;
    public PendingCommand(String input, Command cmd){
        this.c = cmd;
        this.text = input;
    }
}
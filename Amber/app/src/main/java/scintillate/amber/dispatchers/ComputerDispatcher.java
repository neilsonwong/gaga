package scintillate.amber.dispatchers;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import scintillate.amber.HttpTask;
import scintillate.amber.MagicPacketTask;
import scintillate.amber.SpeechContext.Command;
import scintillate.amber.Settings;

/**
 * Created by rin on 2/26/2017.
 */

public class ComputerDispatcher {
    public static boolean turningOn = false;

    public static int handle(Command c){
        System.out.println("computer");
        int runStatus = 0;
        //run command
        switch (c.getAction()) {
            case "turn":
                String mod = c.getMod();
                if (mod != null){
                    if (mod.equals("on")){
                        turnOn();
                    }
                    else if (mod.equals("off")){
                        turnOff();
                    }
                    else {
                        runStatus = 1;
                    }
                }
                break;
            case "sleep":
                sleep();
                break;
            case "switch":
                switchAudio();
                break;
            default:
                runStatus = 1;
        }
        return runStatus;
    }

    public static void turnOn(){
        if (!turningOn) {
            turningOn = true;
            String mac = Settings.devices.get("Neilson PC").MAC;
            new MagicPacketTask().execute(mac);
        }
    }

    public static void turnOff(){
        exec("computer", "shutdown");
    }

    public static void sleep(){
        exec("computer", "sleep");
    }

    public static void switchAudio(){
        exec("switch", "audio");
    }

    private static void exec(String application, String task){
        new HttpTask(Settings.PC_COM_SERVER + application + "/" + task).execute();
    }


}
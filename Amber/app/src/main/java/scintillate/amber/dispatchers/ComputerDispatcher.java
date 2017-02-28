package scintillate.amber.dispatchers;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import scintillate.amber.HttpTask;
import scintillate.amber.MagicPacketTask;

/**
 * Created by rin on 2/26/2017.
 */

public class ComputerDispatcher {
    public static void turnOn(){
        String mac = "BC-5F-F4-AE-61-0F";
        new MagicPacketTask().execute(mac);
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
        new HttpTask("http://192.168.0.113:3000/" + application + "/" + task).execute();
    }
}
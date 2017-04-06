package scintillate.amber;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rin on 3/22/2017.
 */

public class Settings {
    public static class Device {
        public String MAC;
        public String IP;
        public String OWNER;
        public Device(String ip, String mac, String owner){
            MAC = mac;
            IP = ip;
            OWNER = owner;
        }
    }

    public static String PC_COM_SERVER;
    public static HashMap<String, Device> devices = new HashMap<String, Device>();
    public static ArrayList<String> people = new ArrayList<String>();

    //static init
    static {
        //computer and phones only
        devices.put("Neilson PC", new Device("192.168.0.113", "BC-5F-F4-AE-61-0F", "Neilson"));
        devices.put("Neilson 6P", new Device("192.168.0.156", "24-DF-6A-4C-19-D5", "Neilson"));

        devices.put("Emily Mac", new Device("192.168.0.112", "80-E6-50-1A-F1-A8", "Emily"));
        devices.put("Emily PC", new Device("192.168.0.111", "C0-4A-00-13-8B-0A", "Emily"));
        devices.put("Emily 5X", new Device("192.168.0.155", "64-BC-0C-44-E3-84", "Emily"));

        devices.put("Raymond 1+", new Device("192.168.0.153", "C0-EE-FB-27-D9-2F", "Raymond"));

        devices.put("Siu Mei N6", new Device("192.168.0.154", "1C-56-FE-C7-17-78", "Siu Mei"));

        people.add("Neilson");
        people.add("Emily");
        people.add("Raymond");
        people.add("Siu Mei");

        PC_COM_SERVER = "http://" + devices.get("Neilson PC").IP + ":3000/";
    }
}


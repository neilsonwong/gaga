package scintillate.amber.dispatchers;

import android.os.AsyncTask;
import android.text.TextUtils;

import java.net.InetAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import scintillate.amber.SayStuff;
import scintillate.amber.Settings;

/**
 * Created by rin on 3/22/2017.
 */

public class NetworkDispatcher {
    public static int handle(String cmd){
        int runStatus = 0;
        switch(cmd){
            case "who is home":
            case "who's home":
            case "who is at home":
                getAtHome();
                runStatus = 0;
                break;
            default:
                runStatus = 1;
        }
        return runStatus;
    }

    public static boolean dry(String cmd){
        boolean canRun = false;
        switch(cmd){
            case "who is home":
            case "who's home":
            case "who is at home":
                canRun = true;
                break;
            default:
                canRun = false;
        }
        return canRun;
    }

    private static void getAtHome(){
        Collection<Settings.Device> ips = Settings.devices.values();
        new pingTask().execute(ips.toArray(new Settings.Device[ips.size()]));
    }
}

class pingTask extends AsyncTask<Settings.Device, Integer, String> {
    public String doInBackground(Settings.Device... ips) {
        Set<String> people = new HashSet<String>();
        for (Settings.Device d : ips){
            try {
                if (InetAddress.getByName(d.IP).isReachable(1000)) {
                    people.add(d.OWNER);
                    System.out.println(d.IP + " added");
                }
                else {
                    System.out.println(d.IP + " not reachable");
                }
            }
            catch (Exception e){
                System.out.println(d.IP + " exception " + e);
            }
        }
        if (people.isEmpty()){
            return "There's nobody home!";
        }
        else {
            String ppl = TextUtils.join(", ", people.toArray(new String[people.size()]));
            if (people.size() == 1){
                ppl += " is";
            }
            else {
                ppl += " are";
            }
            ppl += " at home right now";
            return ppl;
        }
    }
    protected void onPostExecute(String result) {
        SayStuff.justSayThis(result, "EN");
    }
}

package scintillate.amber;

/**
 * Created by rin on 3/5/2017.
 */

import android.os.AsyncTask;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import scintillate.amber.dispatchers.Dispatcher;

/**
 * Created by rin on 2/18/2017.
 */

public class AliveTask extends AsyncTask<String, Integer, String> {
    String inputURL;
    private OnAlive callback;

    public AliveTask(OnAlive dispatcher){
        this.callback = dispatcher;
        inputURL = Settings.PC_COM_SERVER + "alive";
        System.out.println(inputURL);
    }

    public AliveTask(String url){
        inputURL = url;
    }

    public String doInBackground(String ... urls) {
        HttpURLConnection urlConnection = null;
        StringBuilder builder = new StringBuilder();
        try {
            URL url = new URL(inputURL);
            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

            String inputString;
            while ((inputString = bufferedReader.readLine()) != null) {
                builder.append(inputString);
            }

        } catch (Exception e) {
//            e.printStackTrace();
            return "failed";
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return builder.toString();
    }

//        protected void onProgressUpdate(Integer... progress) {
//        }

    protected void onPostExecute(String result) {
        // this is executed on the main thread after the process is over
        // update your UI here{
        if (result.indexOf("living") == 0) {
            String currentApplication = result.substring(6);
            EventBus.getDefault().post(new MessageEvent(currentApplication, MessageEvent.ACTION_HANDLER, MessageEvent.COMPUTER_AWAKE_EVENT));
        }
        callback.onAlive(result);
    }
}

package scintillate.amber;

import android.os.AsyncTask;
import android.util.Log;

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

import org.greenrobot.eventbus.EventBus;

/**
 * Created by rin on 2/18/2017.
 */

public class HttpTask extends AsyncTask<String, Integer, String> {
    String inputURL;

    public HttpTask(String url, HashMap<String, String> qs){
        String queryString = "";
        String q;
        Iterator it = qs.entrySet().iterator();

        while (it.hasNext()) {
            if (queryString.isEmpty()){
                queryString += "?";
            }
            else {
                queryString += "&";
            }
            Map.Entry pair = (Map.Entry)it.next();
            try {
                q = URLEncoder.encode(pair.getKey().toString(), "UTF-8")
                    + "="
                    + URLEncoder.encode(pair.getValue().toString(), "UTF-8");
                queryString += q;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
        inputURL = url + queryString;
        System.out.println(inputURL);
    }

    public HttpTask(String url){
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
            e.printStackTrace();
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
            // update your UI here
            if (result.equals("living")) {
                EventBus.getDefault().post(new MessageEvent("Computer Host Alive", MessageEvent.ACTION_HANDLER, MessageEvent.COMPUTER_AWAKE_EVENT));
            }
        }
}

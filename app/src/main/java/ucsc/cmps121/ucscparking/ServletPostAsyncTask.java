package ucsc.cmps121.ucscparking;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by switkin on 6/1/2017.
 */

public class ServletPostAsyncTask extends AsyncTask<Map<String, String>, Void, String> {

    public interface AsyncResponse{
        void processFinish(String result);
    }

    public AsyncResponse delegate = null;

    ServletPostAsyncTask(AsyncResponse del){
        this.delegate = del;
    }

    @Override
    protected String doInBackground(Map<String, String>... params) {
        Map<String, String> pMap = params[0];

        try {
            // Set up the request
            URL url = new URL("https://ucscparking-1.appspot.com/_mysupersekretservletpath");
            //URL url = new URL("http://10.0.2.2:8080/_mysupersekretservletpath");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // Build name data request params
            Map<String, String> keyValuePairs = new HashMap<>();
            for (Map.Entry<String, String> entry : pMap.entrySet()) {
                keyValuePairs.put(entry.getKey(), entry.getValue());
            }
            String postParams = buildPostDataString(keyValuePairs);

            // Execute HTTP Post
            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(postParams);
            writer.flush();
            writer.close();
            outputStream.close();
            connection.connect();

            // Read response
            int responseCode = connection.getResponseCode();
            StringBuilder response = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                return response.toString();
            }
            return "Error: " + responseCode + " " + connection.getResponseMessage();

        } catch (IOException e) {
            return e.getMessage();
        }
    }

    private String buildPostDataString(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }
}

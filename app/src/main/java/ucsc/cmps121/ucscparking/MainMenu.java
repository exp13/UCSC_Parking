package ucsc.cmps121.ucscparking;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

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

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        new ServletPostAsyncTask().execute(new Pair<Context, String>(this, "Manfred"));
    }

    public void goAccountPrefs(View v){
        Intent intent = new Intent(this, AccountPrefs.class);
        startActivity(intent);
    }

    public void goMapList(View v){
        Intent intent = new Intent(this, MapList.class);
        startActivity(intent);
    }

    public void goCamera(View v) {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    class ServletPostAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
        private Context context;

        @Override
        protected String doInBackground(Pair<Context, String>... params) {
            context = params[0].first;
            String name = params[0].second;

            try {
                // Set up the request
                URL url = new URL("http://ucscparking-1.appspot.com/hello");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                // Build name data request params
                Map<String, String> nameValuePairs = new HashMap<>();
                nameValuePairs.put("name", name);
                String postParams = buildPostDataString(nameValuePairs);

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
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        }
    }
}

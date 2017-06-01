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

public class MainMenu extends AppCompatActivity implements ServletPostAsyncTask.AsyncResponse{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //new ServletPostAsyncTask().execute(new Pair<Context, String>(this, "Manfred"));
        Map<String, String> aMap = new HashMap<>();
        aMap.put("func", "TestFunc");
        aMap.put("testValue", "wtf is going on in hya");
        //aMap.put("name", "Manfred");
        new ServletPostAsyncTask(this).execute(aMap);
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

    @Override
    public void processFinish(String result){
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }

}

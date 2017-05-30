package ucsc.cmps121.ucscparking;

import android.content.Context;
import android.graphics.Path;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class DBLHandlerTestActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dblhandler_test);

        DBHandler db = new DBHandler(this);

    // Inserting AccountInfo/Rows
        Log.d("Insert(): ", "Inserting ..");
        AccountInfo test1 = new AccountInfo("ABC1234", "kachinna@ucsc.edu");
        db.addAccount(test1);
        db.addAccount(new AccountInfo("ZXY9876", "katja.russia678@gmail.com"));
        db.addAccount(new AccountInfo("ILOVEU2345", "rain@gmail.com"));
       

    // Reading all accounts
        Log.d("Reading(): ", "Reading all accounts..");
        List<AccountInfo> accountInfos = db.getAllAccounts();

        for (AccountInfo accountInfo : accountInfos) {
            String log = "Id: " + accountInfo.getFireBaseid() + " ,Email: " + accountInfo.getEmail();
            Log.d("Account:: ", log);
        }
        db.updateAccount(test1);
        for (AccountInfo accountInfo : accountInfos) {
            String log = "Id: " + accountInfo.getFireBaseid() + " ,Email: " + accountInfo.getEmail();
            Log.d("Account:: ", log);
        }
        db.getAllAccounts();

        Context ctx = this; // for Activity, or Service. Otherwise simply get the context.
        String dbname = "AccountInfo.db";//your db name
        String path = ctx.getDatabasePath("AccountInfo.db").getPath();
        Log.d("path ", path);

        String url = "https://ucsc-parking-fe31a.firebaseio.com/";
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                "AccountInfo.db");
        try {
            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost(url);

            InputStreamEntity reqEntity = new InputStreamEntity(
                    new FileInputStream(file), -1);
            reqEntity.setContentType("binary/octet-stream");
            reqEntity.setChunked(true); // Send in multiple parts if needed
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            //Do something with response...

        } catch (Exception e) {
            // show error
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
    }
}
package ucsc.cmps121.ucscparking;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

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
    }
}
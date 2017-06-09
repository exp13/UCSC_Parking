package ucsc.cmps121.ucscparking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class CheckOut extends AppCompatActivity implements ServletPostAsyncTask.AsyncResponse {

    private AppInfo appInfo;
    private TextView sMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        appInfo = AppInfo.getInstance(this);

        sMsg = (TextView) findViewById(R.id.spotMsg);

        sMsg.setText("You are checked in to spot "+appInfo.getSpot()+" in "+appInfo.getLot());
    }

    public void onCheckOutPressed(View v){
        Map<String, String> aMap = new HashMap<>();
        aMap.put("func", "CheckOut");
        aMap.put("userid", appInfo.getEmail());

        new ServletPostAsyncTask(this).execute(aMap);
    }

    @Override
    public void processFinish(String result){
        if(result.contains("CheckOut")){
            appInfo.setLot("none");
            appInfo.setSpot("none");

            Intent intent = new Intent(this, MainMenu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else{
            sMsg.setText("Server error: Please try again.");
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainMenu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}

package ucsc.cmps121.ucscparking;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class NewUser extends AppCompatActivity implements ServletPostAsyncTask.AsyncResponse {

    private Spinner timeDrop;
    private Spinner lot1Drop;
    private Spinner lot2Drop;
    private Spinner lot3Drop;

    private AppInfo appInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        appInfo = AppInfo.getInstance(this);

        timeDrop = (Spinner) findViewById(R.id.timeSpinner);
        String[] items = new String[] {"Choose a time...",
                "08:00", "08:15", "08:30", "08:45",
                "09:00", "09:15", "09:30", "09:45",
                "10:00", "10:15", "10:30", "10:45",
                "11:00", "11:15", "11:30", "11:45",
                "12:00", "12:15", "12:30", "12:45",
                "13:00", "13:15", "13:30", "13:45",
                "14:00", "14:15", "14:30", "14:45",
                "15:00", "15:15", "15:30", "15:45",
                "16:00", "16:15", "16:30"
        };
        ArrayAdapter<String> timeAdap = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        timeDrop.setAdapter(timeAdap);

        items = new String[]{
                "Choose a lot...",
                "North Remote",
                "East Remote",
                "West Remote",
                "Core West",
                "Performing Arts",
                "Kresge College",
                "Stevenson College",
                "Baskin 1",
                "Baskin 2",
                "Rachel Carson College",
                "Oakes College",
                "Merrill College",
                "Porter College",
                "College 10",
                "Hahn Building",
        };

        lot1Drop = (Spinner) findViewById(R.id.newLot1Spinner);
        lot2Drop = (Spinner) findViewById(R.id.newLot2Spinner);
        lot3Drop = (Spinner) findViewById(R.id.newLot3Spinner);
        ArrayAdapter<String> lotAdap1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        ArrayAdapter<String> lotAdap2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        ArrayAdapter<String> lotAdap3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        lot1Drop.setAdapter(lotAdap1);
        lot2Drop.setAdapter(lotAdap2);
        lot3Drop.setAdapter(lotAdap3);

    }

    public void saveClick(View v){
        String timeItem = (String) timeDrop.getSelectedItem();
        String lotItem1 = (String) lot1Drop.getSelectedItem();
        String lotItem2 = (String) lot2Drop.getSelectedItem();
        String lotItem3 = (String) lot3Drop.getSelectedItem();

        TextView tn = (TextView) findViewById(R.id.newSchNameView);
        TextView tv = (TextView) findViewById(R.id.newScText);

        if(timeItem.contentEquals("Choose a time...") ||
                lotItem1.contentEquals("Choose a lot...") ||
                lotItem2.contentEquals("Choose a lot...") ||
                lotItem3.contentEquals("Choose a lot...") ||
                tn.getText() == ""){
            tv.setText("Please choose options for each drop down\nand enter a name.");
            tn.setHint("Enter name");
            tn.setHintTextColor(Color.RED);

        }else if((lotItem1 == lotItem2) || (lotItem1 == lotItem3) || (lotItem2 == lotItem3)){
            tv.setText("Please choose 3 different lots.");
        }else{
            Map<String, String> cMap = new HashMap<>();
            Map<String, String> lMap = new HashMap<>();

            cMap.put("func", "CreateUser");
            cMap.put("userid", appInfo.getEmail());
            appInfo.setSpot("none");
            appInfo.setLot("none");
            new ServletPostAsyncTask(this).execute(cMap);

            lMap.put("func", "AddLotPref");
            lMap.put("userid", appInfo.getEmail());
            lMap.put("name", tn.getText().toString());
            lMap.put("time", timeItem);
            lMap.put("lot1", lotItem1);
            lMap.put("lot2", lotItem2);
            lMap.put("lot3", lotItem3);
            new ServletPostAsyncTask(this).execute(lMap);

        }
    }

    @Override
    public void processFinish(String result){

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();

        if(result.contains("Added pref")){
            Intent intent = new Intent(this, CameraActivity.class);
            intent.putExtra("nextClass", "MainMenu");
            startActivity(intent);
        }
    }
}

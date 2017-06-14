package ucsc.cmps121.ucscparking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EditPref extends AppCompatActivity implements ServletPostAsyncTask.AsyncResponse{

    private AppInfo appInfo;

    private Spinner timeDrop;
    private Spinner lot1Drop;
    private Spinner lot2Drop;
    private Spinner lot3Drop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pref);

        appInfo = AppInfo.getInstance(this);

        timeDrop = (Spinner) findViewById(R.id.editTimeSpinner);
        ArrayList<String> timeItems = new ArrayList<String>(Arrays.asList("Choose a time...",
                "08:00", "08:15", "08:30", "08:45",
                "09:00", "09:15", "09:30", "09:45",
                "10:00", "10:15", "10:30", "10:45",
                "11:00", "11:15", "11:30", "11:45",
                "12:00", "12:15", "12:30", "12:45",
                "13:00", "13:15", "13:30", "13:45",
                "14:00", "14:15", "14:30", "14:45",
                "15:00", "15:15", "15:30", "15:45",
                "16:00", "16:15", "16:30"
        ));
        ArrayAdapter<String> timeAdap = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, timeItems);
        timeDrop.setAdapter(timeAdap);

        ArrayList<String> lotItems = new ArrayList<String>(Arrays.asList(
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
                "Hahn Building"
        ));

        lot1Drop = (Spinner) findViewById(R.id.editLot1Spinner);
        lot2Drop = (Spinner) findViewById(R.id.editLot2Spinner);
        lot3Drop = (Spinner) findViewById(R.id.editLot3Spinner);
        ArrayAdapter<String> lotAdap1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, lotItems);
        ArrayAdapter<String> lotAdap2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, lotItems);
        ArrayAdapter<String> lotAdap3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, lotItems);
        lot1Drop.setAdapter(lotAdap1);
        lot2Drop.setAdapter(lotAdap2);
        lot3Drop.setAdapter(lotAdap3);

        TextView tn = (TextView) findViewById(R.id.editSchNameView);
        tn.setText(this.getIntent().getStringExtra("name"));
        timeDrop.setSelection(timeItems.indexOf(this.getIntent().getStringExtra("time")));
        lot1Drop.setSelection(lotItems.indexOf(this.getIntent().getStringExtra("lot1")));
        lot2Drop.setSelection(lotItems.indexOf(this.getIntent().getStringExtra("lot2")));
        lot3Drop.setSelection(lotItems.indexOf(this.getIntent().getStringExtra("lot3")));

    }

    public void onSaveClick(View v){
        String timeItem = (String) timeDrop.getSelectedItem();
        String lotItem1 = (String) lot1Drop.getSelectedItem();
        String lotItem2 = (String) lot2Drop.getSelectedItem();
        String lotItem3 = (String) lot3Drop.getSelectedItem();

        TextView tn = (TextView) findViewById(R.id.editSchNameView);
        TextView tv = (TextView) findViewById(R.id.editScText);

        if(timeItem.contentEquals("Choose a time...") ||
                lotItem1.contentEquals("Choose a lot...") ||
                lotItem2.contentEquals("Choose a lot...") ||
                lotItem3.contentEquals("Choose a lot...") ||
                tn.getText() == ""){
            tv.setText("Please choose options for each drop down.");
        }else if((lotItem1 == lotItem2) || (lotItem1 == lotItem3) || (lotItem2 == lotItem3)){
            tv.setText("Please choose 3 different lots.");
        }else{
            Map<String, String> lMap = new HashMap<>();

            lMap.put("func", "SaveLotPref");
            lMap.put("userid", appInfo.getEmail());
            lMap.put("name", tn.getText().toString());
            lMap.put("time", timeItem);
            lMap.put("lot1", lotItem1);
            lMap.put("lot2", lotItem2);
            lMap.put("lot3", lotItem3);
            new ServletPostAsyncTask(this).execute(lMap);
        }
    }

    public void onDeleteClick(View v){
        TextView tn = (TextView) findViewById(R.id.editSchNameView);

        Map<String, String> aMap = new HashMap<>();
        aMap.put("func", "DeleteLotPref");
        aMap.put("userid", appInfo.getEmail());
        aMap.put("name", tn.getText().toString());
        new ServletPostAsyncTask(this).execute(aMap);
    }

    @Override
    public void processFinish(String result){
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();

        if(result.contains("Saved") || result.contains("Deleted")){
            Intent intent = new Intent(this, PreferredLots.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, PreferredLots.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}

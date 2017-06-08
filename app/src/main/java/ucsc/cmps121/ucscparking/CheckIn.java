package ucsc.cmps121.ucscparking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class CheckIn extends AppCompatActivity implements ServletPostAsyncTask.AsyncResponse{

    private TextView spotMsg;
    private TextView checkMsg;
    private Spinner timeSpinner;
    private String rLot;
    private String spotNum;

    private  AppInfo appInfo;

    private int packetCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        appInfo = AppInfo.getInstance(this);
        packetCounter = 0;

        spotMsg = (TextView) findViewById(R.id.spotMsg);
        checkMsg = (TextView) findViewById(R.id.checkInMsg);
        timeSpinner = (Spinner) findViewById(R.id.timeSpinner);
        rLot = this.getIntent().getStringExtra("lot_name");

        String[] items = new String[] {"Time...",
                "30 min", "45 min", "1 hour", "1.25 hours",
                "1.5 hours", "1.75 hours", "2 hours"
        };
        ArrayAdapter<String> timeAdap = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        timeSpinner.setAdapter(timeAdap);

        Map<String, String> aMap = new HashMap<>();
        aMap.put("func", "ReserveSpot");
        aMap.put("userid", appInfo.getEmail());
        aMap.put("lotid", rLot);

        new ServletPostAsyncTask(this).execute(aMap);
    }


    @Override
    public void processFinish(String result){
        switch(packetCounter){
            case 0:
                int cursorB = 0;
                while(result.charAt(cursorB)!='!'){cursorB++;}

                spotNum = result.substring(0, cursorB);
                appInfo.setSpot(spotNum);

                Calendar cal = Calendar.getInstance();
                cal.setTimeZone(TimeZone.getDefault());
                int currentH = cal.get(Calendar.HOUR_OF_DAY);
                int currentM = cal.get(Calendar.MINUTE)+15;
                if(currentM > 59){
                    currentM -= 60;
                    currentH += 1;
                }
                String cH = Integer.toString(currentH);
                String cM = Integer.toString(currentM);
                if(currentM < 10){cM = "0" + cM;}
                spotMsg.setText("Spot "+spotNum+" is reserved until "+cH+":"+cM);
                break;
            case 1:
                if(result.contains("CheckIn")){
                    Intent intent = new Intent(this, CheckOut.class);
                    startActivity(intent);
                }
                else {
                    checkMsg.setText("Server error,\n please try again!");
                    packetCounter--;
                }

                break;
        }

        packetCounter++;
    }

    public void onCamPress(View v){
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra("nextClass", "report");
        intent.putExtra("lotid", rLot);
        intent.putExtra("spotid", spotNum);
        startActivity(intent);
    }

    public void onCheckInPressed(View v) {
        String time = (String) timeSpinner.getSelectedItem();
        if (!time.contains("Time")) {
            appInfo.setLot(rLot);
            appInfo.setSpot(spotNum);

            Map<String, String> aMap = new HashMap<>();
            aMap.put("func", "CheckIn");
            aMap.put("lotid", rLot);
            aMap.put("spotid", spotNum);

            if(time.contains("30")) {
                aMap.put("durationh", "0");
                aMap.put("durationm", "30");
            }
            else if(time.contains("45")) {
                aMap.put("durationh", "0");
                aMap.put("durationm", "45");
            }
            else if(time.contains("1")) {

                if(time.contains("1.25")) {
                    aMap.put("durationh", "1");
                    aMap.put("durationm", "15");
                }
                else if(time.contains("1.5")) {
                    aMap.put("durationh", "1");
                    aMap.put("durationm", "30");
                }
                else if(time.contains("1.75")) {
                    aMap.put("durationh", "1");
                    aMap.put("durationm", "45");
                }
                else {
                    aMap.put("durationh", "1");
                    aMap.put("durationm", "0");
                }
            }
            else if(time.contains("2")) {
                aMap.put("durationh", "2");
                aMap.put("durationm", "0");
            }

            new ServletPostAsyncTask(this).execute(aMap);

        }
        else {
            checkMsg.setText("Please select a duration.");
        }


    }

    @Override
    public void onBackPressed(){

    }

}

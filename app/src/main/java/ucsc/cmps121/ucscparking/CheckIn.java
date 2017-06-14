package ucsc.cmps121.ucscparking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
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

    private ArrayList<String> lotIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        lotIndex = new ArrayList<>();
        lotIndex.add(0, "empty");
        lotIndex.add(1, "North Remote");
        lotIndex.add(2, "East Remote");
        lotIndex.add(3, "West Remote");
        lotIndex.add(4, "Core West");
        lotIndex.add(5, "Performing Arts");
        lotIndex.add(6, "Kresge College");
        lotIndex.add(7, "Stevenson College");
        lotIndex.add(8, "Baskin 1");
        lotIndex.add(9, "Baskin 2");
        lotIndex.add(10, "Rachel Carson College");
        lotIndex.add(11, "Oakes College");
        lotIndex.add(12, "Merrill College");
        lotIndex.add(13, "Porter College");
        lotIndex.add(14, "College 10");
        lotIndex.add(15, "Hahn Building");

        rLot = this.getIntent().getStringExtra("lot_name");
        loadImage();

        appInfo = AppInfo.getInstance(this);
        packetCounter = 0;

        spotMsg = (TextView) findViewById(R.id.spotMsg);
        checkMsg = (TextView) findViewById(R.id.checkInMsg);
        timeSpinner = (Spinner) findViewById(R.id.timeSpinner);

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

    private void loadImage(){
        int index = 0;
        for(int i=0; i<lotIndex.size(); i++){
            if(rLot.contains(lotIndex.get(i))){
                index = i;
            }
        }

        ImageView img = (ImageView) findViewById(R.id.imageView);

        switch(index){
            case 1:
                img.setImageResource(R.drawable.north_remote);
                break;
            case 2:
                img.setImageResource(R.drawable.east_remote);
                break;
            case 3:
                img.setImageResource(R.drawable.west_remote);
                break;
            case 4:
                img.setImageResource(R.drawable.core_west);
                break;
            case 5:
                img.setImageResource(R.drawable.performing_arts);
                break;
            case 6:
                img.setImageResource(R.drawable.kresge);
                break;
            case 7:
                img.setImageResource(R.drawable.stevenson);
                break;
            case 8:
                img.setImageResource(R.drawable.baskin1);
                break;
            case 9:
                img.setImageResource(R.drawable.baskin2);
                break;
            case 10:
                img.setImageResource(R.drawable.rachel_carson);
                break;
            case 11:
                img.setImageResource(R.drawable.oakes);
                break;
            case 12:
                img.setImageResource(R.drawable.merrill);
                break;
            case 13:
                img.setImageResource(R.drawable.porter);
                break;
            case 14:
                img.setImageResource(R.drawable.college10);
                break;
            case 15:
                img.setImageResource(R.drawable.hahn);
                break;
        }
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
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
            checkMsg.setText("Please select\n a duration.");
        }


    }

    @Override
    public void onBackPressed(){

    }

}

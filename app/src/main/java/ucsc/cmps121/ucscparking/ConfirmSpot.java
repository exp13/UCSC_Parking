package ucsc.cmps121.ucscparking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ConfirmSpot extends AppCompatActivity {

    private boolean proceed;
    private String lotResult;
    private String spotCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_spot);

        proceed = false;
        lotResult = this.getIntent().getStringExtra("lot_name");
        spotCount = this.getIntent().getStringExtra("spot_count");

        TextView lotMsg = (TextView) findViewById(R.id.lotMsg);
        TextView butMsg = (TextView) findViewById(R.id.butMsg);
        Button finBut = (Button) findViewById(R.id.finishBut);

        if(lotResult.contains("none")){
            lotMsg.setText("There are no spots available");
            butMsg.setText("Please choose a different preference or select a lot");
            finBut.setText("Return");
        }else{
            lotMsg.setText("Parking lot: "+lotResult+"\nSpots Available: "+spotCount);
            butMsg.setText("Press confirm to reserve a spot or press back to return");
            finBut.setText("Confirm");
            proceed = true;
        }

    }

    public void onButPress(View v){
        if(proceed){
            Intent intent = new Intent(this, CheckIn.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("lot_name", lotResult);
            startActivity(intent);
        }else{
            proceed = true;
            onBackPressed();
        }
    }


    @Override
    public void onBackPressed(){
        if(proceed){
            Intent intent = new Intent(this, FindSpot.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

}

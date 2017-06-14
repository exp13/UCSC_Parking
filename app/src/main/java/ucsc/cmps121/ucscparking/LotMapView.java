package ucsc.cmps121.ucscparking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;

public class LotMapView extends AppCompatActivity {

    private String rLot;
    private ArrayList<String> lotIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lot_map_view);

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

}

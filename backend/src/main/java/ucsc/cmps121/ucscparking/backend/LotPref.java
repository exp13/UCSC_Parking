package ucsc.cmps121.ucscparking.backend;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by switkin on 6/1/2017.
 */

public class LotPref{
    public String name;
    public  Time time;
    public int lots[];

    LotPref(){lots = new int[3];}
}

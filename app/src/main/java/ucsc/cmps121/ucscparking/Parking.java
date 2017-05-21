package ucsc.cmps121.ucscparking;

import android.content.Intent;

import java.util.ArrayList;

public class Parking {

    private ArrayList<lot> lotPrefs = null;

    public void initialize() {
        lotPrefs = new ArrayList<>();
        lotPrefs.add(new lot("143", "Kresge", 50, 50));
        lotPrefs.add(new lot("Merrill", "Merrill", 50, 50));
        lotPrefs.add(new lot("North Remote", "Kresge", 50, 50));
        lotPrefs.add(new lot("100", "Kresge", 50, 50));
    }

    public lot grabData(int index) {
        if(lotPrefs != null && index < lotPrefs.size()) {
            return this.lotPrefs.get(index);
        }
        else {
            return null;
        }
    }

    public int getSize() {
        return lotPrefs.size();
    }

    public class lot {
        public String name;
        public String location;
        public int capacity;
        public int available;

        lot(){};

        lot(String _name, String _location, int _capacity, int _available){
            name = _name;
            location = _location;
            capacity = _capacity;
            available = _available;
        }
    }
}

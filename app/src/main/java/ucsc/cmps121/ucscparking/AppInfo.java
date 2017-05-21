package ucsc.cmps121.ucscparking;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by luca on 18/1/2016.
 */
public class AppInfo {

    private static AppInfo instance = null;


    // Here are some values we want to keep global.
    public String sharedString;

    // static private member for storing strings
    private String[] sharedStrings;

    private Context my_context;

    protected AppInfo() {
        // initialise empty string array
        sharedStrings = new String[3];
        sharedStrings[0] = "";
        sharedStrings[1] = "";
        sharedStrings[2] = "";
    }


    public static AppInfo getInstance(Context context) {
        if(instance == null) {
            instance = new AppInfo();
            instance.my_context = context;
            /*SharedPreferences settings = context.getSharedPreferences(MainActivity.MYPREFs, 0);
            instance.sharedStrings[0] = settings.getString("one", null);
            instance.sharedStrings[1] = settings.getString("two", null);
            instance.sharedStrings[2] = settings.getString("three", null);*/
        }
        return instance;
    }


    // get string method for shared strings
    public String getString(int index){
        return instance.sharedStrings[index];
    }

    // set string method for shared strings
    public void setString(String s, int index){
        instance.sharedStrings[index] = s;
        /*SharedPreferences settings = my_context.getSharedPreferences(MainActivity.MYPREFS, 0);
        SharedPreferences.Editor editor = settings.edit();
        switch(index){
            case 0:
                editor.putString("one", s);
                break;
            case 1:
                editor.putString("two", s);
                break;
            case 2:
                editor.putString("three", s);
                break;
        }
        editor.commit();*/
    }
}

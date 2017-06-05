package ucsc.cmps121.ucscparking;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by luca on 18/1/2016.
 */
public class AppInfo {

    private static AppInfo instance = null;

    private Context my_context;

    private String userEmail;


    protected AppInfo() {

    }

    public static AppInfo getInstance(Context context) {
        if(instance == null) {
            instance = new AppInfo();
            //instance.my_context = context;
            //SharedPreferences settings = context.getSharedPreferences("UCSCParking", 0);
        }
        return instance;
    }

    public void setEmail(String e){
        userEmail = e;
    }

    public String getEmail(){
        return userEmail;
    }


    // set string method for shared strings
    // saving this for example code
    /*public void setString(String s, int index){
        instance.sharedStrings[index] = s;
        SharedPreferences settings = my_context.getSharedPreferences(MainActivity.MYPREFS, 0);
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
        editor.commit();
    }*/
}

package ucsc.cmps121.ucscparking;
import android.accounts.Account;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by katia on 5/24/2017.
 */

public class DBHandler extends SQLiteOpenHelper{
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "AccountInfo";
    // Accounts table name
    private static final String TABLE_ACCOUNTS = "accounts";
    // Accounts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_EMAIL = "email";
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_ACCOUNTS + "("
        + KEY_ID + "INTEGER PRIMARY KEY," + KEY_EMAIL + "TEXT," + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
// Creating tables again
        onCreate(db);
    }
}

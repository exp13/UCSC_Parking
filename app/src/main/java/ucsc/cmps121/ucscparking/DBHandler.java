package ucsc.cmps121.ucscparking;
import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


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
        String CREATE_ACCOUNTS_TABLE = "CREATE TABLE " + TABLE_ACCOUNTS + "("
        + KEY_ID + " TEXT PRIMARY KEY," + KEY_EMAIL + " TEXT" + ")";
        db.execSQL(CREATE_ACCOUNTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
    // Creating tables again
        onCreate(db);
    }
    // Adding new account
    public void addAccount(AccountInfo accountInfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, accountInfo.getFireBaseid()); // id
        values.put(KEY_EMAIL, accountInfo.getEmail()); // email

    // Inserting Row
        db.insert(TABLE_ACCOUNTS, null, values);
        db.close(); // Closing database connection
    }

    // Getting one account
    public AccountInfo getAccount(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ACCOUNTS, new String[] { KEY_ID,
                        KEY_EMAIL}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        AccountInfo accountInfo = new AccountInfo(cursor.getString(0), cursor.getString(1));
        // return account
        return accountInfo;
    }

    // Getting ALL Accounts
    public List<AccountInfo> getAllAccounts() {
        List<AccountInfo> accountList = new ArrayList<AccountInfo>();
    // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_ACCOUNTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
    // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AccountInfo accountInfo = new AccountInfo();
                accountInfo.setFireBaseid(cursor.getString(0));
                accountInfo.setEmail(cursor.getString(1));
    // Adding accountInfo to list
                accountList.add(accountInfo);
            } while (cursor.moveToNext());
        }
    // return accountInfo list
        return accountList;
    }

    // Updating an account
    public int updateAccount(AccountInfo accountInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, accountInfo.getEmail());
    // updating row
        return db.update(TABLE_ACCOUNTS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(accountInfo.getFireBaseid())});
    }

    // Deleting an account
    public void deleteAccount(AccountInfo accountInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ACCOUNTS, KEY_ID + " = ?",
                new String[] { String.valueOf(accountInfo.getFireBaseid()) });
        db.close();
    }
}

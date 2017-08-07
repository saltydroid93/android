package com.example.materialstudioapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Quad on 8/7/2017.
 */

public class GeneralDBHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "UserDB";

    public GeneralDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create user table
        String CREATE_BOOK_TABLE = "CREATE TABLE users ( " +
                "userid INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, "+
                "password TEXT )";

        // create users table
        db.execSQL(CREATE_BOOK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older users table if existed
        db.execSQL("DROP TABLE IF EXISTS users");

        // create fresh users table
        this.onCreate(db);
    }

    // Users table name
    private static final String TABLE_USERS = "users";

    // Users Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    private static final String[] COLUMNS = {KEY_ID,KEY_USERNAME,KEY_PASSWORD};

    public User getUser(int userid){
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_USERS, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(userid) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build user object
        User user = new User();
        user.setUserid((Integer.parseInt(cursor.getString(0))));
        user.setUsername(cursor.getString(1));
        user.setPassword(cursor.getString(2));

        //log
        Log.d("getUser("+userid+")", user.toString());

        // 5. return user
        return user;
    }

    public void insertUser(User user){
        //for logging
        Log.d("addUser", user.toString());

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getUsername()); // get title
        values.put(KEY_PASSWORD, user.getPassword()); // get author

        // 3. insert
        db.insert(TABLE_USERS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public int updateUser(User user){
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getUsername()); // get title
        values.put(KEY_PASSWORD, user.getPassword()); // get author

        // 3. updating row
        int i = db.update(TABLE_USERS, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(user.getUserid()) }); //selection args

        // 4. close
        db.close();

        return i;

    }

    public void deleteUser(User user){
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_USERS, //table name
                KEY_ID+" = ?",  // selections
                new String[] { String.valueOf(user.getUserid()) }); //selections args

        // 3. close
        db.close();

        //log
        Log.d("deleteUser", user.toString());
    }

    public List<User> getAllUsers(){
        List<User> users = new LinkedList<User>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_USERS;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build user and add it to list
        User user = null;
        if (cursor.moveToFirst()) {
            do {
                user = new User();
                user.setUserid((Integer.parseInt(cursor.getString(0))));
                user.setUsername(cursor.getString(1));
                user.setPassword(cursor.getString(2));

                // Add user to users
                users.add(user);
            } while (cursor.moveToNext());
        }

        Log.d("getAllUsers()", users.toString());

        // return users
        return users;
    }

    
}

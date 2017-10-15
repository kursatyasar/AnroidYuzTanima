package com.example.yasrk.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TableLayout;

/**
 * Created by yasrk on 14/10/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME="UserDB.sqlite";
    private static final String TABLE_USER = "USER";
    private static final String COLUMN_USER_ID =  "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_PASSWORD = "user_password";
    private static final String COLUMN_NAME = "imagename";
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT, "
            + COLUMN_USER_PASSWORD + " TEXT, " + COLUMN_NAME +" BLOB NOT NULL " + ")";
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    public DatabaseHelper(Context context){

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }
    public void queryData(String sql){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }

    public void insertContact(String name,String password, byte[] image){
        SQLiteDatabase db = getWritableDatabase();
        String abc = "INSERT INTO USER VALUES (NULL, ?, ?, ?)";
        SQLiteStatement statement = db.compileStatement(abc);
        statement.clearBindings();
        statement.bindString(1,name);
        statement.bindString(2,password);
        statement.bindBlob(3,image);
        statement.executeInsert();



       /* ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getUsername());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_NAME, image);
        db.insert(TABLE_USER, null, values);
        db.close();*/
    }
    public Cursor getData(String sql){

        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(sql,null);
    }

    public boolean checkUser(String username) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_NAME + " = ?";

        // selection argument
        String[] selectionArgs = {username};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }
    public Bitmap getImage(String name){

        SQLiteDatabase db = this.getReadableDatabase();
        String qu = "SELECT imagename FROM USER WHERE";
        Cursor cur =null;
        cur= db.rawQuery(qu,new String[]{});
        if(cur.moveToFirst()){
            int index=cur.getColumnIndexOrThrow("image");
            byte[] imgByte = cur.getBlob(index);
            cur.close();
            return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        }
        if (cur != null && !cur.isClosed()) {
            cur.close();
        }
        return null;

    }


    public boolean checkUser(String username, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_NAME + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {username, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }



    public boolean checkregister(String uname){
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select user_name, user_password from" +TABLE_USER;
        Cursor cursor = db.rawQuery(query, null);
        String a;
        if(cursor.moveToFirst()){
            do{
                a=cursor.getString(0);

                if(a.equals(uname)){
                    return true;
                }
            }
            while (cursor.moveToNext());

        }
        return false;
    }
   /* public String searchPass(String uname){
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select username, pass from" + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String a,b;
        b= "not found";
        if(cursor.moveToFirst()){
            do {
                a=cursor.getString(0);

                if (a.equals(uname)){
                    b=cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        return b;
    }
*/

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);

        // Create tables again
        onCreate(db);

    }
}

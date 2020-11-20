package com.project.covid_19.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.project.covid_19.DataModel.userdatamodel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DBAdapter extends SQLiteOpenHelper {
    public static final String KEY_ID = "CountryId";
    public static final String KEY_NAME = "CountryName";
    public static final String KEY_CASES = "Cases";
    public static final String KEY_STATES = "States";



    private static final String DATABASE_NAME = "Covid";
    private static final String DATABASE_MARKSTABLE = "CovidData";
    private static final int DATABASE_VERSION = 1;
    public static String updatecomment = "";
    public static int userid = 0;
    static String name = "ticket.sqlite";
    static String path = "";
    static ArrayList<userdatamodel> a;
    static ArrayList<userdatamodel> arr;
    static SQLiteDatabase sdb;

    private DBAdapter(Context v) {

        super(v, name, null, 1);
        path = "/data/data/" + v.getApplicationContext().getPackageName()
                + "/databases";
    }

    public static synchronized DBAdapter getDBAdapter(Context v) {
        return (new DBAdapter(v));
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + DATABASE_MARKSTABLE + " (" +
                KEY_ID + " INTEGER PRIMARY KEY  NOT NULL  UNIQUE, " +
                KEY_NAME + " VARCHAR NOT NULL, " +
                KEY_CASES + " INTEGER NOT NULL, " +
                KEY_STATES + " VARCHAR NOT NULL);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    public boolean checkDatabase() {
        SQLiteDatabase db = null;
        try {
            db = SQLiteDatabase.openDatabase(path + "/" + name, null,
                    SQLiteDatabase.OPEN_READWRITE);
        } catch (Exception e) {

        }

        if (db == null) {
            return false;
        } else {
            db.close();
            return true;
        }
    }

    public void createDatabase(Context v) {
        this.getReadableDatabase();
        try {
            InputStream myInput = v.getAssets().open(name);
            // Path to the just created empty db
            String outFileName = path + "/" + name;
            // Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);
            // transfer bytes from the inputfile to the outputfile
            byte[] bytes = new byte[1024];
            int length;
            while ((length = myInput.read(bytes)) > 0) {
                myOutput.write(bytes, 0, length);
            }
            // Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();


            InputStream is = v.getAssets().open("Covid.sqlite");
            System.out.println(new File(path + "/" + name).getAbsolutePath());
            FileOutputStream fos = new FileOutputStream(path + "/" + name);
            int num = 0;
            while ((num = is.read()) > 0) {
                fos.write((byte) num);
            }
            fos.close();
            is.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void openDatabase() {
        try {
            sdb = SQLiteDatabase.openDatabase(path + "/" + name, null,
                    SQLiteDatabase.OPEN_READWRITE);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void insertAData(Integer cases,String country, String states) {
        ContentValues cv = new ContentValues();
        cv.put("Cases",cases);
        cv.put("CountryName", country);
        cv.put("States", states);

        sdb.insert("CovidData", null, cv);
    }

    public ArrayList<userdatamodel> getData() {

        Cursor c1 = sdb.rawQuery("select * from CovidData", null);
        a = new ArrayList<userdatamodel>();
        while (c1.moveToNext()) {
            userdatamodel q1 = new userdatamodel();
            q1.setCountry(c1.getString(1));
            q1.setStates(c1.getString(3));
            q1.setCases(c1.getInt(2));
            a.add(q1);
        }
        return a;
    }


    public void getComment(int id) {
        Cursor c1 = sdb.rawQuery("select id,lat,lan,date,time,photo,status,comment,type,videouri from photoDetails where id='" + id + "'", null);
        while (c1.moveToNext()) {
            updatecomment = c1.getString(7);
        }
    }

    public void Updateime(int id, String stu) {
        sdb.execSQL("UPDATE photoDetails set status='" + stu + "' WHERE id='" + id + "'");
    }

    public void UpdateComment(int id, String stu) {
        sdb.execSQL("UPDATE photoDetails set comment='" + stu + "' WHERE id='" + id + "'");
    }

    public void DeleteData() {
        sdb.execSQL("DELETE FROM CovidData");
    }

}

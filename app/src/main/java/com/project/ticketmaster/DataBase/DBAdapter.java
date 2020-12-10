package com.project.ticketmaster.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;


import com.project.ticketmaster.DataModel.userdatamodel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DBAdapter extends SQLiteOpenHelper {
    public static final String KEY_ID = "EventId";
    public static final String KEY_NAME = "EventName";
    public static final String KEY_LOCATION = "EventLocation";
    public static final String KEY_DATE = "EventDate";
    public static final String KEY_CONTACT = "EventContact";
    public static final String KEY_PRICE = "EventPrice";
    public static final String KEY_CITY = "CityName";



    private static final String DATABASE_NAME = "ticket";
    private static final String DATABASE_MARKSTABLE = "Events";
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
                KEY_LOCATION + " VARCHAR NOT NULL, " +
                KEY_DATE + " VARCHAR NOT NULL, " +
                KEY_CONTACT + " VARCHAR NOT NULL, " +
                KEY_PRICE + " VARCHAR NOT NULL, " +
                KEY_CITY + " VARCHAR NOT NULL);"
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


            InputStream is = v.getAssets().open("ticket.sqlite");
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

    public void insertAssignaturas(Integer numAss,String group, String assignature, String profesor) {

        ContentValues cv = new ContentValues();
        cv.put("NumAssignatura",numAss);
        cv.put("Grupo", group);
        cv.put("Assignatura", assignature);
        cv.put("Profesor", profesor);

        sdb.insert("Assignaturas", null, cv);
    }

    public void insertHorarios(Integer numHorarios,String dia, String Hora_inicio, String Hora_fin) {

        ContentValues cv = new ContentValues();
        cv.put("NumHorario",numHorarios);
        cv.put("Dia", dia);
        cv.put("Hora_inicio", Hora_inicio);
        cv.put("Hora_fin", Hora_fin);

        sdb.insert("Horarios", null, cv);
    }

    public void insertEvent(String eventName,String location,String date,String contact,String price,String city) {

        ContentValues cv = new ContentValues();
        cv.put("EventName",eventName);
        cv.put("EventLocation", location);
        cv.put("EventDate", date);
        cv.put("EventContact", contact);
        cv.put("EventPrice", price);
        cv.put("CityName", city);

        sdb.insert("Events", null, cv);
    }

    public void insertUserData(String name, String address, String city, String area, String mobile, String rent, String ava, String cat, ArrayList<Bitmap> img) {
        byte[] byteArray = null;
        for (int i = 0; i < img.size(); i++) {
            if (img.get(i) != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                img.get(i).compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byteArray = stream.toByteArray();
            }
        }
        ContentValues cv = new ContentValues();
        cv.put("username", name);
        cv.put("useraddress", address);
        cv.put("usercity", city);
        cv.put("userarea", area);
        cv.put("usermobile", mobile);
        cv.put("userrent", rent);
        cv.put("userava", ava);
        cv.put("usercat", cat);
        cv.put("userimages", byteArray);

        sdb.insert("userdata", null, cv);
    }

//        public ArrayList<userdatamodel> getData(String startTime,String day) {
//        try {
//            Cursor c1 = sdb.rawQuery("select A.*  from Assignaturas  A , Horarios H , Clases C where  C.IdAssignatura = A.NumAssignatura and H.Hora_inicio='"+startTime+"' and  H.Dia='"+day+"' and A.Grupo ='GrupoB' and H.NumHorario=C.IdHorario", null);
//            a = new ArrayList<userdatamodel>();
//            while (c1.moveToNext()) {
//                userdatamodel q1 = new userdatamodel();
//                q1.setSub(c1.getString(2));
//                q1.setProfesor(c1.getString(3));
//                a.add(q1);
//                Log.d("Birbal", "Hello");
//            }
//            return a;
//        } catch (Exception e) {
//            Log.d("DBAdapter", e + "");
//        }
//        return a;
//    }
    public ArrayList<userdatamodel> getData(String cityName) {

        Cursor c1 = sdb.rawQuery("select * from Events where EventName ='"+cityName+"'", null);
        a = new ArrayList<userdatamodel>();
        while (c1.moveToNext()) {
            userdatamodel q1 = new userdatamodel();
            q1.setEvent(c1.getString(1));
            q1.setLocation(c1.getString(2));
            q1.setDate(c1.getString(3));
            q1.setContact(c1.getString(4));
            q1.setPrice(c1.getString(5));
            a.add(q1);
        }
        return a;
    }

    public ArrayList<userdatamodel> getEvent(String cityName, String all) {

        Cursor c1 = sdb.rawQuery("select * from Events where CityName ='"+cityName+"' OR CityName='"+all+"'", null);
        a = new ArrayList<userdatamodel>();
        while (c1.moveToNext()) {
            userdatamodel q1 = new userdatamodel();
            q1.setEvent(c1.getString(1));
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

    public void DeleteData(int id) {
        sdb.execSQL("DELETE FROM photoDetails WHERE id='" + id + "'");
    }

}

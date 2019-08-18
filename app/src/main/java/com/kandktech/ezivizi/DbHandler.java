package com.kandktech.ezivizi;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kandktech.ezivizi.model_class.SavedUserDetailModelClass;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

public class DbHandler extends SQLiteOpenHelper {

    /*
    database name
     */
    public static final String database_name="ezivizi";

    /*
    define cart table
     */

    public static final String table_name="qr_code_data";
    public static final String _id=" _id";
    public static final String user_name="user_name";
    public static final String user_address="user_address";
    public static final String user_phone="user_phone";
    public static final String user_website="user_website";
    public static final String user_email="user_email";
    public static final String user_position = "user_position";
    public static final String user_device_id = "user_device_id";
    public static final String user_logo = "user_logo";
    public static final String color_code = "color_code";
    public static final String used_layout = "used_layout";
    public static final String company = "company";
    public static final String color_code_second  = "color_code_second";
    public static final String fax_no = "fax_no";
    public static final String po_box_no = "po_box_no";

    /*
    creating database
     */

    public DbHandler(Context context) {
        super(context, database_name, null,1);
    }

    /*
    creating tables
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + table_name + "( _id integer primary key autoincrement ,user_name text,user_address text,user_phone text,user_website text,user_email text,user_position text,user_device_id text,user_logo text,color_code text,used_layout text,company text,color_code_second text,fax_no text,po_box_no text)");
    }

    /*
    drop table if exists
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists" + table_name);

        onCreate(db);
    }

    /*
    insert data to qr_code_data table
     */

    public void insertData(String user_name1, String user_address1, String user_phone1, String user_website1,String user_email1,String user_position1, String user_device_id1, String logo1,String color_code1,String used_layout1,String company1,String color_code_second1,String fax_no1,String po_box_no1){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(user_name,user_name1);
        values.put(user_address,user_address1);
        values.put(user_phone, user_phone1);
        values.put(user_website,user_website1);
        values.put(user_email,user_email1);
        values.put(user_position,user_position1);
        values.put(user_device_id,user_device_id1);
        values.put(user_logo,logo1);
        values.put(color_code,color_code1);
        values.put(used_layout,used_layout1);
        values.put(company,company1);
        values.put(color_code_second,color_code_second1);
        values.put(fax_no,fax_no1);
        values.put(po_box_no,po_box_no1);

        db.insert(table_name,null,values);

//        int u = db.update("qr_code_data", values, "user_device_id=?", new String[]{user_device_id1});
//        if (u == 0) {
//            db.insertWithOnConflict("qr_code_data", null, values, SQLiteDatabase.CONFLICT_REPLACE);
//        }

    }

    /*
        retrieve data from qr_code_data table
     */

    public Cursor viewData(){
        SQLiteDatabase db=this.getWritableDatabase();

        Cursor cr =  db.rawQuery( "select _id as _id, user_name, color_code,  user_address,  user_phone,  user_website, user_email, user_position,  user_device_id, user_logo, used_layout, company, fax_no,color_code_second,po_box_no from qr_code_data ",null);

        if (cr != null) {
            cr.moveToFirst();
        }
        if (cr != null) {
            cr.getCount();
        }
        return cr;

    }



    /*
    delete from cart table when quantity = 0;
     */
    public void deleteDataSingle(String device_id1){
        try {
            SQLiteDatabase db=this.getWritableDatabase();
            String delete="delete from qr_code_data where user_device_id='" + device_id1 + "'";
            db.execSQL(delete);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RcycloDatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "rcyclo";
    public static final int DB_VERSION = 1;

    public RcycloDatabaseHelper(Context context){ super(context, DB_NAME, null, DB_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ESTABLISHMENT (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + "NAME TEXT, " + "EMAIL TEXT, " + "PASSWORD TEXT, " + "PHONE TEXT, " + "ADDRESS TEXT, " + "WASTE TEXT, " + "ACTIVE TEXT);");
        db.execSQL("CREATE TABLE COMPANY       (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + "NAME TEXT, " + "EMAIL TEXT, " + "PASSWORD TEXT, " + "PHONE TEXT, " + "ADDRESS TEXT, " + "ACTIVE TEXT);");
        db.execSQL("CREATE TABLE CONTAINER     (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + "NAME_CONTAINER TEXT, " + "LATLONG TEXT, " + "ESTABLISHMENT TEXT, " + "COMPANY TEXT, " + "ESTADO TEXT, " + "ACTIVE TEXT, " + "WASTE TEXT, " + "EMAIL_ESTABLISHMENT TEXT, " + "EMAIL_COMPANY TEXT);");

        insertEstablishment(db, "Fundacion San Jose", "sanJose@gmail.com", "admin", "800 212 200", "lat/lng: (-20.245366,-70.129313)", "Papel", "ACTIVO");
        insertEstablishment(db, "Fundacion Maria Ayuda",    "mariaAyuda@gmail.com",     "admin",    "23 28 0100",       "lat/lng: (-33.037147,-71.592293)",         "Plastico", "ACTIVO");
        insertEstablishment(db, "CENFA",                    "cenfa@gmail.com",          "admin",    "22 59 4187",       "lat/lng: (-33.034409,-71.596390)",       "Vidrio", "INACTIVE");
        insertEstablishment(db, "COAR",                     "coar@gmail.com",           "admin",    "27 32 2821",       "lat/lng: (-33.377523,-70.577431)",         "Lata", "ACTIVO");

        insertCompany(db, "Jumbo", "jumbo@gmail.com", "admin", "6032424", "lat/lng: (-33.440070,-70.598046)", "ACTIVO");
        insertCompany(db, "Entel",  "entel@gmail.com", "admin", "6032424",  "lat/lng: (-33.372810,-70.542968)", "ACTIVO");
        insertCompany(db, "Torre",  "torre@gmail.com", "admin", "6032424",  "lat/lng: (-33.373169,-70.502465)", "ACTIVO");

        insertContainer(db, "Basurerito", "lat/lng: (-33.440070,-70.598046)", "Fundacion San Jose", "Jumbo", "Lleno", "ACTIVO", "Papel", "sanJose@gmail.com","jumbo@gmail.com");
        insertContainer(db,"Basurerito2", "lat/lng: (-33.440070,-70.598046)", "CENFA", "Jumbo", "Medio", "ACTIVO", " Vidrio", "cenfa@gmail.com","jumbo@gmail.com");
        insertContainer(db,"Basurerito3", "lat/lng: (-33.440070,-70.598046)", "Fundacion San Jose", "Jumbo", "Vacio", "ACTIVO", "Papel","sanJose@gmail.com","jumbo@gmail.com");
        insertContainer(db,"Basurerito4", "lat/lng: (-33.440070,-70.598046)", "Fundacion San Jose", "Jumbo", "Vacio", "INACTIVE", "Papel","sanJose@gmail.com","jumbo@gmail.com");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}

    public static void  insertEstablishment(SQLiteDatabase db, String name, String email, String password, String phone, String address, String waste, String active) {
        ContentValues establishmentValues = new ContentValues();
        establishmentValues.put("NAME", name);
        establishmentValues.put("EMAIL", email);
        establishmentValues.put("PASSWORD", password);
        establishmentValues.put("PHONE", phone);
        establishmentValues.put("ADDRESS", address);
        establishmentValues.put("WASTE", waste);
        establishmentValues.put("ACTIVE", "ACTIVO");
        db.insert("ESTABLISHMENT", null, establishmentValues);
    }

    public static void  insertCompany(SQLiteDatabase db, String name, String email, String password, String phone, String address, String activo) {
        ContentValues companyValues = new ContentValues();
        companyValues.put("NAME", name);
        companyValues.put("EMAIL", email);
        companyValues.put("PASSWORD", password);
        companyValues.put("PHONE", phone);
        companyValues.put("ADDRESS", address);
        companyValues.put("ACTIVE", "ACTIVO");

        db.insert("COMPANY", null, companyValues);
    }

    public static void  insertContainer(SQLiteDatabase db, String nameContainer, String latlong, String establishmentName, String companyName, String estado, String activo, String waste, String email_establishment, String email_company) {
        ContentValues containerValues = new ContentValues();
        containerValues.put("NAME_CONTAINER", nameContainer);
        containerValues.put("LATLONG", latlong);
        containerValues.put("ESTABLISHMENT", establishmentName);
        containerValues.put("COMPANY", companyName);
        containerValues.put("ESTADO", estado);
        containerValues.put("ACTIVE", activo);
        containerValues.put("WASTE", waste);
        containerValues.put("EMAIL_ESTABLISHMENT", email_establishment);
        containerValues.put("EMAIL_COMPANY", email_company);
        db.insert("CONTAINER", null, containerValues);
    }

}
package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class CompanyRegisterActivity extends AppCompatActivity {

    private final int DURACION = 1000;
    EditText etName, etEmail, etPassword,etPhone, etAddress;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_register);
        etName      = (EditText) findViewById(R.id.Cname);
        etEmail     = (EditText) findViewById(R.id.Cemail);
        etPassword  = (EditText) findViewById(R.id.CPassword);
        etPhone     = (EditText) findViewById(R.id.CPhone);
        etAddress   = (EditText) findViewById(R.id.CAddress);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public void onStop() {
        super.onStop();
        Crouton.cancelAllCroutons();
    }

    public void sendForm(View view){
        String name     = etName.getText().toString();
        String email    = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String phone    = etPhone.getText().toString();
        String address  = etAddress.getText().toString();

        if (name.matches("") || email.matches("") || password.matches("") || phone.matches("") || address.matches("")) {
            Crouton.makeText(this,"Se deben llenar todos los campos!", Style.ALERT).show();
        }

        else {
            if (isEmailValid(email)) {

                if( isEmailUsed(email)){
                    etEmail.setError("Este correo ya existe");
                }
                else {
                    ContentValues companyValues = new ContentValues();
                    companyValues.put("NAME", name);
                    companyValues.put("EMAIL", email);
                    companyValues.put("PASSWORD", password);
                    companyValues.put("PHONE", phone);
                    companyValues.put("ADDRESS", address);
                    companyValues.put("ACTIVO", "ACTIVO");

                    SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(this);
                    SQLiteDatabase db = rcycloDatabaseHelper.getWritableDatabase();

                    db.insert("COMPANY", null, companyValues);
                    db.close();

                    Toast toast= Toast.makeText(getApplicationContext(),"Registro exitoso!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 70);
                    toast.show();

                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            Intent intent = new Intent().setClass(CompanyRegisterActivity.this, SelectEstablishmentActivity.CompanyLoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(task, DURACION);
                }
            } else {
                etEmail.setError("El correo esta mal escrito.");
            }
        }
    }


    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public boolean isEmailUsed(String email) {
        SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(this);
        SQLiteDatabase db = rcycloDatabaseHelper.getWritableDatabase();
        Cursor cursor = db.query("COMPANY", new String[]{"EMAIL"}, "EMAIL = ?", new String[]{email}, null, null, null);
        if (cursor.moveToFirst()){
            return true;
        }
        else {
            return false;
        }
    }



}

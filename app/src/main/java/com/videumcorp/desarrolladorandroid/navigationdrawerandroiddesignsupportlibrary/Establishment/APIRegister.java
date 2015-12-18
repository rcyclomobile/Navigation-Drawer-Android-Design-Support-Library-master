package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Establishment;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.R;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.DataBase.RcycloDatabaseHelper;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


public class APIRegister extends AppCompatActivity {

    private final int DURACION = 1000;
    EditText etName, etEmail, etPassword,etPhone, etAddress;
    RadioGroup rgWaste;
    private RadioButton rbPapel, rbPlastico, rbVidrio, rbLata;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_establishment_register);
        etName      = (EditText) findViewById(R.id.Ename);
        etEmail     = (EditText) findViewById(R.id.Eemail);
        etPassword  = (EditText) findViewById(R.id.EPassword);
        etPhone     = (EditText) findViewById(R.id.EPhone);
        etAddress   = (EditText) findViewById(R.id.EAddress);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    public void sendForm(View view){
        String name     = etName.getText().toString();
        String email    = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String phone    = etPhone.getText().toString();
        String address  = etAddress.getText().toString();

        rbPapel     = (RadioButton) findViewById(R.id.radio_papel);
        rbVidrio    = (RadioButton) findViewById(R.id.radio_vidrio);
        rbPlastico  = (RadioButton) findViewById(R.id.radio_plastico);
        rbLata      = (RadioButton) findViewById(R.id.radio_lata);

        if (name.matches("") || email.matches("") || password.matches("") || phone.matches("") || address.matches("")) {
            Crouton.makeText(this, "Se deben llenar todos los campos!", Style.ALERT).show();
        }

        else {
            if (isEmailValid(email)) {

                if( isEmailUsed(email)){
                    Crouton.makeText(this, "El Email ya existe!!", Style.ALERT).show();
                }

                else {
                    ContentValues establishmentValues = new ContentValues();
                    establishmentValues.put("NAME", name);
                    establishmentValues.put("EMAIL", email);
                    establishmentValues.put("PASSWORD", password);
                    establishmentValues.put("PHONE", phone);
                    establishmentValues.put("ADDRESS", address);
                    establishmentValues.put("ACTIVO", "ACTIVO");

                    SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(this);
                    SQLiteDatabase db = rcycloDatabaseHelper.getWritableDatabase();

                    if (rbPapel.isChecked()) {
                        establishmentValues.put("WASTE", "papel");
                    } else if (rbPlastico.isChecked()) {
                        establishmentValues.put("WASTE", "plastico");
                    } else if (rbVidrio.isChecked()) {
                        establishmentValues.put("WASTE", "vidrio");
                    } else if (rbLata.isChecked()) {
                        establishmentValues.put("WASTE", "lata");
                    }

                    //Implementar API Aqui!!
                    db.insert("ESTABLISHMENT", null, establishmentValues);
                    db.close();

                    Toast toast= Toast.makeText(getApplicationContext(),"Registro exitoso!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 70);
                    toast.show();
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            Intent intent = new Intent().setClass(APIRegister.this, APILogin.class);
                            startActivity(intent);
                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(task, DURACION);
                }
            }

            else {
                Crouton.makeText(this, "El mail debe ser valido!!", Style.ALERT).show();
                etEmail.setText("");
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
        //Implementar API Aqui!!
        Cursor cursor = db.query("ESTABLISHMENT", new String[]{"EMAIL"}, "EMAIL = ?", new String[]{email}, null, null, null);
        if (cursor.moveToFirst()){
            return true;
        }
        else {
            return false;
        }
    }


}

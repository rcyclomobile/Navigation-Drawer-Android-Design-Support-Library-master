package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class CompanyLoginActivity extends Activity {

    private final int DURACION = 1000;
    EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_login);

        etEmail = (EditText) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);
    }

    @Override
    public void onStop() {
        super.onStop();
        Crouton.cancelAllCroutons();
    }

    public boolean validate() {
        boolean valid = true;

        String email    = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email incorrecto");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            etPassword.setError("Contraseña incorrecta");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }

    //holaaa

    public void enter(View view){
        String email    = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String active = "ACTIVO";
        SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(this);
        SQLiteDatabase db = rcycloDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query("COMPANY", new String[]{"NAME", "EMAIL", "PASSWORD", "PHONE", "ADDRESS", "ACTIVO"}, "EMAIL = ? AND PASSWORD = ? AND ACTIVO = ?", new String[]{email, password, active}, null, null, null);

        if (validate()) {
            if (cursor.moveToFirst()) {
                final String empresa = cursor.getString(0);
                cursor.close();
                db.close();
                Crouton.makeText(this, "Bienvenido a Rcyclo " + empresa + "!", Style.CONFIRM).show();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(CompanyLoginActivity.this, CompanyMainActivity.class);
                        intent.putExtra(CompanyMainActivity.EMPRESA, empresa);
                        startActivity(intent);
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, DURACION);
            }
            else{
                Crouton.makeText(this, "Este usuario no existe", Style.ALERT).show();
            }
        }
    }
    public void register(View view){
        Intent intent = new Intent(this, CompanyRegisterActivity.class);
        startActivity(intent);
    }

}

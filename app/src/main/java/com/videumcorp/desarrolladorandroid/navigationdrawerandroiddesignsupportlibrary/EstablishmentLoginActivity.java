package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class EstablishmentLoginActivity extends Activity {

    EditText etEmail, etPassword;
    private final int DURACION = 300;
    TextView register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_establishment_login);

        etEmail = (EditText) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);
        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EstablishmentRegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        Crouton.cancelAllCroutons();
    }

    public void enter(View view){
        String email    = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(this);
        SQLiteDatabase db = rcycloDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query("ESTABLISHMENT", new String[]{"NAME", "EMAIL", "PASSWORD", "PHONE", "ADDRESS"}, "EMAIL = ? AND PASSWORD = ? AND ACTIVO = ?", new String[]{email, password, "ACTIVO"}, null, null, null);

        if (cursor.moveToFirst()) {
            final String name = cursor.getString(0);
            cursor.close();
            db.close();

            Toast toast= Toast.makeText(getApplicationContext(),"Bienvenido a Rcyclo " + name + "!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 70);
            toast.show();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(EstablishmentLoginActivity.this, EstablishmentMainActivity.class);
                    intent.putExtra(EstablishmentMainActivity.NAME, name);
                    startActivity(intent);
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, DURACION);
        }

        else {
            Crouton.makeText(this, "Este usuario no existe", Style.ALERT).show();
        }
    }

    public void register(View view){
        Intent intent = new Intent(this, EstablishmentRegisterActivity.class);
        startActivity(intent);
    }




}
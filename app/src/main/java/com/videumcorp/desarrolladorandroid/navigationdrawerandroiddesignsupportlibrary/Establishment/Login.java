package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Establishment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company.Main;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.DataBase.RcycloDatabaseHelper;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class Login extends Activity {

    EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_establishment_login);

        etEmail = (EditText) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);

    }

    public boolean validate() {
        boolean valid = true;

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email incorrecto");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        if (password.isEmpty()) {
            etPassword.setError("Contraseña incorrecta");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }

    public void enter(View view) {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        if(validate()) {

            SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(this);
            SQLiteDatabase db = rcycloDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query("ESTABLISHMENT", new String[]{"NAME", "EMAIL", "PASSWORD", "PHONE", "ADDRESS"}, "EMAIL = ? AND PASSWORD = ?", new String[]{email, password}, null, null, null);

            if (cursor.moveToFirst()) {
                final String name_to_send = cursor.getString(0);
                final String email_to_send = cursor.getString(1);
                final String address_to_send = cursor.getString(4);
                cursor.close();
                db.close();
                Toast toast = Toast.makeText(getApplicationContext(), "Bienvenido a Rcyclo " + name_to_send + "!", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(Login.this, com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Establishment.Main.class);
                intent.putExtra("name", name_to_send);
                intent.putExtra("email", email_to_send);
                intent.putExtra("address", address_to_send);
                startActivity(intent);
            } else {
                Crouton.makeText(this, "Este usuario no existe", Style.ALERT).show();
            }
        }
    }

    public void register(View view) {
        Intent intent = new Intent(this,APIRegister.class);
        startActivity(intent);
    }
}

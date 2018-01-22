package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
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
        setContentView(R.layout.activity_company_login);

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
        final String email = etEmail.getText().toString();
        final String activo = "ACTIVO";
        String inactivo = "INACTIVE";
        String password = etPassword.getText().toString();
        if(validate()) {

            SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(this);
            SQLiteDatabase db = rcycloDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query("COMPANY", new String[]{"NAME", "EMAIL", "PASSWORD", "ADDRESS", "ACTIVE"}, "EMAIL = ? AND PASSWORD = ? AND ACTIVE = ?", new String[]{email, password, activo}, null, null, null);
            Cursor cursor2 = db.query("COMPANY", new String[]{"NAME", "EMAIL", "PASSWORD", "ADDRESS", "ACTIVE"}, "EMAIL = ? AND PASSWORD = ? AND ACTIVE = ?", new String[]{email, password, inactivo}, null, null, null);

            if (cursor.moveToFirst()) {
                final String name_to_send = cursor.getString(0);
                final String email_to_send = cursor.getString(1);
                final String address_to_send = cursor.getString(3);

                cursor.close();
                db.close();
                Toast toast1 = Toast.makeText(getApplicationContext(), "Bienvenido a Rcyclo " + name_to_send + "!", Toast.LENGTH_SHORT);
                toast1.show();
                Intent intent = new Intent(Login.this, com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company.Main.class);
                intent.putExtra("name", name_to_send);
                intent.putExtra("email", email_to_send);
                intent.putExtra("address", address_to_send);
                startActivity(intent);
            } else {
                if(cursor2.moveToFirst()){
                    final String name_to_send_inactive = cursor2.getString(0);
                    final String email_to_send_inactive = cursor2.getString(1);
                    final String address_to_send_inactive = cursor2.getString(3);

                    cursor.close();
                    db.close();

                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
                    dialogo1.setTitle("Reactivar cuenta");
                    dialogo1.setMessage("¿Deseas reactivar tu cuenta?");
                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {

                            SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(getApplicationContext());
                            SQLiteDatabase db = rcycloDatabaseHelper.getWritableDatabase();

                            ContentValues containerValues = new ContentValues();
                            containerValues.put("ACTIVE", activo);

                            db.update("COMPANY", containerValues, "EMAIL = ?", new String[]{email_to_send_inactive});

                            Toast toast1 = Toast.makeText(getApplicationContext(), "Tu cuenta ha sido reactivada. Bienvenido a Rcyclo " + name_to_send_inactive + "!", Toast.LENGTH_SHORT);
                            toast1.show();

                            Intent intent = new Intent(Login.this, Main.class);
                            intent.putExtra("name", name_to_send_inactive);
                            intent.putExtra("email", email_to_send_inactive);
                            intent.putExtra("address", address_to_send_inactive);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            finish();

                        }
                    });
                    dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                        }
                    });
                    dialogo1.show();
                }
                else{
                    Crouton.makeText(this, "Este usuario no existe", Style.ALERT).show();
                }
            }
        }
    }

    public void register(View view) {
        Intent intent = new Intent(this,Register.class);
        startActivity(intent);
    }
}

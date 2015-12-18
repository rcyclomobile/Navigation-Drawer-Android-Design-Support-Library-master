package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Establishment;

import android.app.Activity;
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

import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.R;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.DataBase.RcycloDatabaseHelper;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class APILogin extends Activity {

    EditText etEmail, etPassword;
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
                Intent intent = new Intent(v.getContext(), APIRegister.class);
                startActivity(intent);
            }
        });

    }

    public void enter(View view){
        String email    = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(this);
        SQLiteDatabase db = rcycloDatabaseHelper.getReadableDatabase();
        //Implementar API Aqui!!
        Cursor cursor = db.query("ESTABLISHMENT", new String[]{"NAME", "EMAIL", "PASSWORD", "PHONE", "ADDRESS"}, "EMAIL = ? AND PASSWORD = ? AND ACTIVO = ?", new String[]{email, password, "ACTIVO"}, null, null, null);

        if (cursor.moveToFirst()) {
            final String name = cursor.getString(0);
            cursor.close();
            db.close();

            Toast toast= Toast.makeText(getApplicationContext(), "Bienvenido a Rcyclo " + name + "!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 70);
            toast.show();
            Intent intent = new Intent(APILogin.this, APIMain.class);
            intent.putExtra(APIMain.NAME, name);
            startActivity(intent);
        }

        else {
            Crouton.makeText(this, "Este usuario no existe", Style.ALERT).show();
        }
    }

}
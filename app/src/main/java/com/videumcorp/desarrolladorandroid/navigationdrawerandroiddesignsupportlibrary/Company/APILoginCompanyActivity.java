package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.DataBase.RcycloDatabaseHelper;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.R;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class APILoginCompanyActivity extends Activity {

    EditText etEmail, etPassword;
    TextView register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_login);

        etEmail = (EditText) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);
        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), APIRegisterCompanyActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
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

        if (password.isEmpty()) {
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
        SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(this);
        final SQLiteDatabase db = rcycloDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query("COMPANY", new String[]{"NAME", "EMAIL", "PASSWORD", "PHONE", "ADDRESS", "ACTIVO"}, "EMAIL = ? AND PASSWORD = ?", new String[]{email, password}, null, null, null);
        if (validate()) {
            if (cursor.moveToFirst()) {
                if(cursor.getString(5).equals("INNACTIVO")){
                    final String empresa = cursor.getString(0);
                    AlertDialog.Builder builder = new AlertDialog.Builder(APILoginCompanyActivity.this);
                    builder.setMessage("Su cuenta actualmente esta inactiva. ¿Desea volver a activarla?");
                    builder.setTitle("Activacion de cuenta");
                    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Su cuenta ha sido activada exitosamente.", Toast.LENGTH_SHORT);
                            toast.show();
                            ContentValues companyValues = new ContentValues();
                            companyValues.put("ACTIVO", "ACTIVO");
                            //Implementar API Aqui!!
                            db.update("COMPANY", companyValues, "NAME = ?", new String[]{empresa});
                            //Implementar API Aqui!!
                            db.delete("CONTAINER","COMPANY = ?",new String[]{empresa} );

                            Intent intent = new Intent(APILoginCompanyActivity.this, APIMainCompanyActivity.class);
                            intent.putExtra(APIMainCompanyActivity.EMPRESA, empresa);
                            startActivity(intent);
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else{
                    final String empresa = cursor.getString(0);
                    cursor.close();
                    db.close();
                    Toast toast = Toast.makeText(getApplicationContext(), "Bienvenido a Rcyclo " + empresa + "!", Toast.LENGTH_SHORT);
                    toast.show();
                    Intent intent = new Intent(APILoginCompanyActivity.this, APIMainCompanyActivity.class);
                    intent.putExtra(APIMainCompanyActivity.EMPRESA, empresa);
                    startActivity(intent);
                    }
                }
            }
            else{
                Crouton.makeText(this, "Este usuario no existe", Style.ALERT).show();
            }
        }
    }


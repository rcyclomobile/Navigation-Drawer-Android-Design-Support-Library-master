package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Establishment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.R;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.DataBase.RcycloDatabaseHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class APISettings extends AppCompatActivity {

    Toolbar toolbar;
    String emailCompany, phoneCompany, addressCompany;
    public static final String NAME = "fundacion";
    TextView nameCo, emailCo, phoneCo, adressCo, nombreHeaderCo, correoHeaderCo, fechaHeaderCo, phoneHeaderCo, passwordCo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_establishment);

        String fundacion = (String) getIntent().getExtras().get(NAME);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        TypedValue typedValueColorPrimaryDark = new TypedValue();
        APISettings.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValueColorPrimaryDark, true);
        final int colorPrimaryDark = typedValueColorPrimaryDark.data;
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(colorPrimaryDark);
        }

        SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(this);
        SQLiteDatabase db = rcycloDatabaseHelper.getWritableDatabase();

        //Implementar API Aqui!!
        Cursor cursor = db.query("ESTABLISHMENT", new String[]{"EMAIL", "PHONE", "ADDRESS"}, "NAME = ? AND ACTIVO = ?", new String[]{fundacion, "ACTIVO"}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                emailCompany = cursor.getString(0);
                phoneCompany = cursor.getString(1);
                addressCompany = cursor.getString(2);
            } while (cursor.moveToNext());
        }

        nameCo = (TextView) findViewById(R.id.nameCo);
        emailCo = (TextView) findViewById(R.id.emailCo);
        phoneCo = (TextView) findViewById(R.id.phoneCo);
        adressCo = (TextView) findViewById(R.id.adressCo);
        nombreHeaderCo = (TextView) findViewById(R.id.nombreHeaderCo);
        correoHeaderCo = (TextView) findViewById(R.id.correoHeaderCo);
        fechaHeaderCo = (TextView) findViewById(R.id.fechaHeaderCo);
        phoneHeaderCo = (TextView) findViewById(R.id.phoneHeaderCo);
        passwordCo = (TextView) findViewById(R.id.passwordCo);

        emailCo.setText(emailCompany);
        phoneCo.setText(phoneCompany);
        adressCo.setText(addressCompany);
        nombreHeaderCo.setText(fundacion);
        correoHeaderCo.setText(emailCompany);
        phoneHeaderCo.setText(phoneCompany);

        emailCo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                editar_perfil(v);
            }
        });

        passwordCo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            editar_contraseña(v);
            }
        });

        phoneCo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar_telefono(v);
            }
        });

    }


    public void editar_perfil(View view){
        String empresa = (String)getIntent().getExtras().get(NAME);
        Intent intent = new Intent(this, APIEditProfile.class);
        intent.putExtra(APIEditProfile.NAME, empresa);

        startActivity(intent);
        finish();
    }

    public void editar_contraseña(View view){
        String empresa1 = (String)getIntent().getExtras().get(NAME);
        Intent intent = new Intent(this, APIChangePassword.class);
        intent.putExtra(APIChangePassword.NAME, empresa1);
        startActivity(intent);
        finish();
    }

    public void editar_telefono(View view){
        String empresa1 = (String)getIntent().getExtras().get(NAME);
        //cambiar
        Intent intent = new Intent(this, APIEditPhone.class);
        intent.putExtra(APIEditPhone.NAME, empresa1);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inbox, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        Cursor cursor = db.query("ESTABLISHMENT", new String[]{"EMAIL"}, "EMAIL = ?", new String[]{email}, null, null, null);
        if (cursor.moveToFirst()){
            return true;
        }
        else {
            return false;
        }
    }


}

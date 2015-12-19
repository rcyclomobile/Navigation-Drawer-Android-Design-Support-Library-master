package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Establishment.*;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Establishment.APIChangePass;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Establishment.APIEditPhone;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.R;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.DataBase.RcycloDatabaseHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class APISettings extends AppCompatActivity {

    Toolbar toolbar;
    String emailCompany,phoneCompany,addressCompany;
    public static final String EMPRESA= "empresa";
    TextView nameCo, emailCo, phoneCo, adressCo, nombreHeaderCo, correoHeaderCo, fechaHeaderCo, phoneHeaderCo, passwordCo;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        String empresa = (String)getIntent().getExtras().get(EMPRESA);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TypedValue typedValueColorPrimaryDark = new TypedValue();
        APISettings.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValueColorPrimaryDark, true);
        final int colorPrimaryDark = typedValueColorPrimaryDark.data;
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(colorPrimaryDark);
        }

        SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(this);
        SQLiteDatabase db = rcycloDatabaseHelper.getWritableDatabase();

        //Implementar API Aqui!!
        Cursor cursor = db.query("COMPANY", new String[]{"EMAIL", "PHONE", "ADDRESS" }, "NAME = ? AND ACTIVO = ?", new String[]{empresa, "ACTIVO"}, null, null, null);

        if(cursor.moveToFirst()){
            do {
                emailCompany = cursor.getString(0);
                phoneCompany = cursor.getString(1);
                addressCompany = cursor.getString(2);
            }while (cursor.moveToNext()) ;
        }

        emailCo = (TextView) findViewById(R.id.emailCo);
        phoneCo = (TextView) findViewById(R.id.phoneCo);
        adressCo = (TextView) findViewById(R.id.adressCo);
        nombreHeaderCo = (TextView) findViewById(R.id.nombreHeaderCo);
        correoHeaderCo = (TextView) findViewById(R.id.correoHeaderCo);
        phoneHeaderCo = (TextView) findViewById(R.id.phoneHeaderCo);
        passwordCo = (TextView) findViewById(R.id.passwordCo);

     /*   emailCo.setText(emailCompany);
        phoneCo.setText(phoneCompany);*/
        adressCo.setText(addressCompany);
        nombreHeaderCo.setText(empresa);
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
        String empresa = (String)getIntent().getExtras().get(EMPRESA);
        Intent intent = new Intent(this, com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Establishment.APIEditEmail.class);
        intent.putExtra(com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Establishment.APIEditEmail.NAME, empresa);

        startActivity(intent);
        finish();
    }

    public void editar_contraseña(View view){
        String empresa1 = (String)getIntent().getExtras().get(EMPRESA);
        Intent intent = new Intent(this, com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Establishment.APIChangePass.class);
        intent.putExtra(APIChangePass.NAME, empresa1);
        startActivity(intent);
        finish();
    }

    public void editar_telefono(View view){
        String empresa1 = (String)getIntent().getExtras().get(EMPRESA);
        //cambiar
        Intent intent = new Intent(this, com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Establishment.APIEditPhone.class);
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
}

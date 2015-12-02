package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SettingsEstablishmentActivity extends AppCompatActivity {

    Toolbar toolbar;
    String emailCompany, phoneCompany, addressCompany;
    public static final String NAME = "fundacion";
    TextView nameCo, emailCo, phoneCo, adressCo, nombreHeaderCo, correoHeaderCo, fechaHeaderCo;

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
        SettingsEstablishmentActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValueColorPrimaryDark, true);
        final int colorPrimaryDark = typedValueColorPrimaryDark.data;
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(colorPrimaryDark);
        }

        SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(this);
        SQLiteDatabase db = rcycloDatabaseHelper.getWritableDatabase();

        Cursor cursor = db.query("ESTABLISHMENT", new String[]{"EMAIL", "PHONE", "ADDRESS" }, "NAME = ? AND ACTIVO = ?", new String[]{fundacion, "ACTIVO"}, null, null, null);

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

        nameCo.setText(fundacion);
        emailCo.setText(emailCompany);
        phoneCo.setText(phoneCompany);
        adressCo.setText(addressCompany);
        nombreHeaderCo.setText(fundacion);
        correoHeaderCo.setText(emailCompany);
        fechaHeaderCo.setText(dateFormat.format(date));

    }
    public void editar_perfil(View view){
        String empresa = (String)getIntent().getExtras().get(NAME);
        Intent intent = new Intent(this, EditProfileEstablishmentActivity.class);
        intent.putExtra(EditProfileEstablishmentActivity.NAME, empresa);

        startActivity(intent);
        finish();
    }

    public void editar_contraseña(View view){
        String empresa1 = (String)getIntent().getExtras().get(NAME);
        Intent intent = new Intent(this, ChangePasswordEstablishmentActivity.class);
        intent.putExtra(EditProfileEstablishmentActivity.NAME, empresa1);
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

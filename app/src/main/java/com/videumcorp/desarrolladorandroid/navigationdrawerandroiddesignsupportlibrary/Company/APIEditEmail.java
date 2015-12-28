package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company;

import android.content.ContentValues;
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
import android.widget.EditText;
import android.widget.TextView;

import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Establishment.*;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.R;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.DataBase.RcycloDatabaseHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
public class APIEditEmail extends AppCompatActivity {

        Toolbar toolbar;
        String emailCompany,phoneCompany,addressCompany;
        public static final String COMPANY= "empresa";
        TextView nombreHeaderCo, correoHeaderCo, fechaHeaderCo;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_edit_profile);

            String empresa = (String)getIntent().getExtras().get(COMPANY);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            TypedValue typedValueColorPrimaryDark = new TypedValue();
            APIEditEmail.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValueColorPrimaryDark, true);
            final int colorPrimaryDark = typedValueColorPrimaryDark.data;
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setStatusBarColor(colorPrimaryDark);
            }

            SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(this);
            SQLiteDatabase db = rcycloDatabaseHelper.getWritableDatabase();

            //Implementar API Aqui!!
            Cursor cursor = db.query("ESTABLISHMENT", new String[]{"EMAIL", "PHONE", "ADDRESS" }, "NAME = ? AND ACTIVO = ?", new String[]{empresa, "ACTIVO"}, null, null, null);

            if(cursor.moveToFirst()){
                do {
                    emailCompany = cursor.getString(0);
                    phoneCompany = cursor.getString(1);
                    addressCompany = cursor.getString(2);
                }while (cursor.moveToNext()) ;
            }
            nombreHeaderCo = (TextView) findViewById(R.id.nombreHeaderCo);
            nombreHeaderCo.setText(empresa);
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

    public void aceptar(View view){
        EditText etEmail = (EditText) findViewById(R.id.emailCo);
        String email =  etEmail.getText().toString();

 /*     if (email.matches("") || phone.matches("")) {
            Crouton.makeText(this, "Se deben llenar todos los campos!", Style.ALERT).show();        }
*/
   /*     else {*/
        if(!email.matches("")) {
            if (isEmailValid(email)) {
                if (isEmailUsed(email) && (!email.equals(emailCompany))) {
                    Crouton.makeText(this, "El Email ya existe!!", Style.ALERT).show();
                } else {
                    String empresa = (String)getIntent().getExtras().get(COMPANY);
                    SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(this);
                    SQLiteDatabase db = rcycloDatabaseHelper.getWritableDatabase();
                    ContentValues companyValues = new ContentValues();
                    companyValues.put("EMAIL", email);
                    //Implementar API Aqui!!
                    db.update("ESTABLISHMENT", companyValues, "NAME = ? ", new String[]{empresa});
                    Intent intent = new Intent(this, com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Establishment.APISettings.class);
                    startActivity(intent);
                    finish();

                }
            } else {

                etEmail.setError("El mail debe ser valido!");
            }
        }
        else{
            etEmail.setError("No se puede dejar el mail en blanco.");
        }
      /*  }*/

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
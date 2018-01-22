package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Establishment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company.*;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.R;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.DataBase.RcycloDatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


public class APIRegister extends AppCompatActivity {

    EditText etName, etEmail, etPassword,etPhone, etAddress;
    private RadioButton rbPapel, rbPlastico, rbVidrio, rbLata;
    Toolbar toolbar;
    String name, email, password, phone, address, desecho;

    private String Name;
    private String Email;
    private String Password;
    private String PasswordRepeat;
    private String Address = "nada";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_establishment_register);
        etName      = (EditText) findViewById(R.id.Ename);
        etEmail     = (EditText) findViewById(R.id.Eemail);
        etPassword  = (EditText) findViewById(R.id.EPassword);
        etPhone     = (EditText) findViewById(R.id.EPhone);
        etAddress   = (EditText) findViewById(R.id.EAddress);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        Name = intent.getStringExtra("name");
        Email = intent.getStringExtra("email");
        Password = intent.getStringExtra("password");
        PasswordRepeat = intent.getStringExtra("passwordRepeat");
        Address = intent.getStringExtra("address");

        etName.setText(Name);
        etEmail.setText(Email);
        etPassword.setText(Password);
        etPhone.setText(PasswordRepeat);

        if(Name != null){
            etName.setText(Name);
        }

        if(Email != null){
            etEmail.setText(Email);
        }
        if(Password != null){
            etPassword.setText(Password);
        }
        if(PasswordRepeat != null){
            etPhone.setText(PasswordRepeat);
        }

        disableEditText(etAddress, Address);
    }

    @Override
    public void onStart() {
        super.onStart();

        Intent intent = getIntent();
        Name = intent.getStringExtra("name");
        Email = intent.getStringExtra("email");
        Password = intent.getStringExtra("password");
        PasswordRepeat = intent.getStringExtra("passwordRepeat");
        Address = intent.getStringExtra("address");
        if (Name != null) {
            etName.setText(Name);
        }

        if (Email != null) {
            etEmail.setText(Email);
        }
        if (Password != null) {
            etPassword.setText(Password);
        }
        if (PasswordRepeat != null) {
            etPhone.setText(PasswordRepeat);
        }

        disableEditText(etAddress, Address);
    }

    @Override
    public void onResume(){
        super.onResume();
        Intent intent = getIntent();
        Name = intent.getStringExtra("name");
        Email = intent.getStringExtra("email");
        Password = intent.getStringExtra("password");
        PasswordRepeat = intent.getStringExtra("passwordRepeat");
        Address = intent.getStringExtra("address");
        if (Name != null) {
            etName.setText(Name);
        }

        if (Email != null) {
            etEmail.setText(Email);
        }
        if (Password != null) {
            etPassword.setText(Password);
        }
        if (PasswordRepeat != null) {
            etPhone.setText(PasswordRepeat);
        }

        address = disableEditText(etAddress, Address);
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<android.location.Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

    private String disableEditText(EditText editText, String Address) {
        //editText.setFocusable(false);
        //editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setHint("Escoja la dirección en el mapa");
        editText.setBackgroundColor(Color.TRANSPARENT);
        if(Address == null){
            editText.setHint("Escoja la dirección en el mapa");
            return "nada";
        }
        else{
            String[] direccion = Address.split(":");
            String[] direccion1 = direccion[1].split("\\(");
            String[] direccion2 = direccion1[1].split("\\)");
            String[] direccion3 = direccion2[0].split(",");
            Double latAddress = Double.parseDouble(direccion3[0]);
            Double lngAddress = Double.parseDouble(direccion3[1]);

            String addressShow = getCompleteAddressString(latAddress,lngAddress);
            editText.setHint(addressShow);
            return addressShow;
        }
    }

    public void sendToMap(View view){
        name = etName.getText().toString();
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        phone = etPhone.getText().toString();

        Intent intent = new Intent(APIRegister.this, com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Establishment.EditAddressRegister.class);
        intent.putExtra("name", name);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        intent.putExtra("passwordRepeat", phone);
        startActivity(intent);
        finish();
    }


    public void sendForm(View view){
        name     = etName.getText().toString();
        email    = etEmail.getText().toString();
        password = etPassword.getText().toString();
        phone    = etPhone.getText().toString();

        rbPapel     = (RadioButton) findViewById(R.id.radio_papel);
        rbVidrio    = (RadioButton) findViewById(R.id.radio_vidrio);
        rbPlastico  = (RadioButton) findViewById(R.id.radio_plastico);
        rbLata      = (RadioButton) findViewById(R.id.radio_lata);

        if (rbPapel.isChecked()) {
            desecho = "Papel";
        } else if (rbPlastico.isChecked()) {
            desecho = "Plastico";
        } else if (rbVidrio.isChecked()) {
            desecho = "Vidrio";
        } else if (rbLata.isChecked()) {
            desecho = "Lata";
        }
        if (name.equals("")) {
            etName.setError("Debe llenar este campo!");
        }
        else if(email.equals("")){
            etEmail.setError("Debe llenar este campo!");
        }
        else if(password.equals("")){
            etPassword.setError("Debe llenar este campo!");
        }
        else if(phone.equals("")){
            etPhone.setError("Debe llenar este campo!");
        }
        else if(address.equals("nada")){
            etAddress.setError("Debe llenar este campo mediante el mapa!");
        }
        else {
            if (isEmailValid(email)) {
                if (change_password(view)) {

                    if( isEmailUsed(email)){
                        Crouton.makeText(this, "El Email ya existe!!", Style.ALERT).show();
                    }

                    else {
                        ContentValues establishmentValues = new ContentValues();
                        establishmentValues.put("NAME", name);
                        establishmentValues.put("EMAIL", email);
                        establishmentValues.put("PASSWORD", password);
                        establishmentValues.put("PHONE", phone);
                        establishmentValues.put("ADDRESS", Address);
                        establishmentValues.put("ACTIVE", "ACTIVO");

                        SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(this);
                        SQLiteDatabase db = rcycloDatabaseHelper.getWritableDatabase();

                        if (rbPapel.isChecked() == true) {
                            establishmentValues.put("WASTE", "Papel");
                        } else if (rbPlastico.isChecked() == true) {
                            establishmentValues.put("WASTE", "Plastico");
                        } else if (rbVidrio.isChecked() == true) {
                            establishmentValues.put("WASTE", "Vidrio");
                        } else if (rbLata.isChecked() == true) {
                            establishmentValues.put("WASTE", "Lata");
                        }

                        db.insert("ESTABLISHMENT", null, establishmentValues);
                        db.close();

                        Toast toast1 = Toast.makeText(getApplicationContext(), Address, Toast.LENGTH_LONG);
                        toast1.show();

                        Toast toast = Toast.makeText(getApplicationContext(), "Registro exitoso!", Toast.LENGTH_LONG);
                        toast.show();
                        Intent intent = new Intent(APIRegister.this, Login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    }
                }
            }
            else {
                Crouton.makeText(this, "El mail debe ser valido!!", Style.ALERT).show();
            }
        }
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

    public boolean change_password(View view) {
        if(!password.equals(phone)){
            etPassword.setError("Las contraseñas no coinciden.");
            return false;
        }
        else{
            return true;
        }

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

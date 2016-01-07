package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Establishment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


public class APIRegister extends AppCompatActivity {

    private final int DURACION = 1000;
    EditText etName, etEmail, etPassword,etPhone, etAddress;
    RadioGroup rgWaste;
    private RadioButton rbPapel, rbPlastico, rbVidrio, rbLata;
    Toolbar toolbar;
    String name, email, password, phone, address, desecho;



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
    }


    public void sendForm(View view){
        name     = etName.getText().toString();
        email    = etEmail.getText().toString();
        password = etPassword.getText().toString();
        phone    = etPhone.getText().toString();
        address  = etAddress.getText().toString();

        rbPapel     = (RadioButton) findViewById(R.id.radio_papel);
        rbVidrio    = (RadioButton) findViewById(R.id.radio_vidrio);
        rbPlastico  = (RadioButton) findViewById(R.id.radio_plastico);
        rbLata      = (RadioButton) findViewById(R.id.radio_lata);

        if (rbPapel.isChecked()) {
            desecho = "papel";
        } else if (rbPlastico.isChecked()) {
            desecho = "´plastico";
        } else if (rbVidrio.isChecked()) {
            desecho = "vidrio";
        } else if (rbLata.isChecked()) {
            desecho = "lata";
        }
        if (name.equals("")) {
            etName.setError("Debe llenar este campo!");
        }
        else if(email.equals("")){
            etEmail.setError("Debe llenar este campo!");
        }
        else if(address.equals("")){
            etAddress.setError("Debe llenar este campo!");
        }
        else if(password.equals("")){
            etPassword.setError("Debe llenar este campo!");
        }
        else if(phone.equals("")){
            etPhone.setError("Debe llenar este campo!");
        }
        else {
            if (isEmailValid(email)) {
                if (change_password(view)) {
                    GetContainers g = new GetContainers();
                    g.execute();
                }
            }

            else {
                etEmail.setError("¡El mail debe ser valido!");
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

    public class GetContainers extends AsyncTask<URL, String, String> {

        @Override
        protected String doInBackground(URL... params) {

            try {
                URL url = new URL("https://api-rcyclo.herokuapp.com/establishments/new");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");

                JSONObject jsonParam = new JSONObject();

                jsonParam.put("name", name);
                jsonParam.put("address", address);
                jsonParam.put("email", email);
                jsonParam.put("password", password);
                jsonParam.put("password_confirmation", phone);


                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                out.write(jsonParam.toString());
                out.close();


                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();

                    String line;

                    while ((line = in.readLine()) != null) {
                        sb.append(line + "\n");
                    }

                    in.close();

                    return "success";

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "failed";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result.equals("success")) {
                Toast toast = Toast.makeText(getApplicationContext(), "Registro exitoso!", Toast.LENGTH_LONG);
                toast.show();
                Intent intent = new Intent(APIRegister.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
            else{
                Toast toast1 =
                        Toast.makeText(getApplicationContext(),
                                "Lo sentimos, algo ha ido mal.", Toast.LENGTH_SHORT);

                toast1.show();
            }

        }
    }

}

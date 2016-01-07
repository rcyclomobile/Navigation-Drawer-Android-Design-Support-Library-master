package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    EditText etName, etEmail, etPassword,etPhone, etAddress;
    Toolbar toolbar;
    String name, email, password, phone, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_register);
        etName      = (EditText) findViewById(R.id.Cname);
        etEmail     = (EditText) findViewById(R.id.Cemail);
        etPassword  = (EditText) findViewById(R.id.CPassword);
        etPhone     = (EditText) findViewById(R.id.CPhone);
        etAddress   = (EditText) findViewById(R.id.CAddress);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    public void sendForm(View view){
        name     = etName.getText().toString();
        email    = etEmail.getText().toString();
        password = etPassword.getText().toString();
        phone    = etPhone.getText().toString();
        address  = etAddress.getText().toString();

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
                URL url = new URL("https://api-rcyclo.herokuapp.com/companies/new");
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
                Intent intent = new Intent(Register.this, com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company.Login.class);
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

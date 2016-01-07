package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Establishment.*;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class APIChangePass extends AppCompatActivity {

    Toolbar toolbar;
    public static final String NAME= "empresa";
    TextView nombreHeaderCo;

    private String access_token;
    private String client;
    private String uid;
    private String Company;

    String passwordCo1;
    String passwordCo2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Intent intent = getIntent();

        access_token = intent.getStringExtra("access-token");
        client = intent.getStringExtra("client");
        uid = intent.getStringExtra("uid");
        Company = intent.getStringExtra("name");

        String empresa = (String)getIntent().getExtras().get(NAME);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        TypedValue typedValueColorPrimaryDark = new TypedValue();
        APIChangePass.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValueColorPrimaryDark, true);
        final int colorPrimaryDark = typedValueColorPrimaryDark.data;
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(colorPrimaryDark);
        }

        nombreHeaderCo = (TextView) findViewById(R.id.nombreHeaderCo);


        nombreHeaderCo.setText(empresa);


    }

    public void change_password(View view) {
        EditText etPasswordCo1 = (EditText) findViewById(R.id.nuevoCorreo1);
        EditText etPasswordCo2 = (EditText) findViewById(R.id.nuevoCorreo2);
        passwordCo1 = etPasswordCo1.getText().toString();
        passwordCo2 = etPasswordCo2.getText().toString();
        if(passwordCo1.matches("")){
            etPasswordCo1.setError("Debe llenar este campo!");
        }

        else if(passwordCo2.matches("")){
            etPasswordCo2.setError("Debe llenar este campo!");
        }

        else if (!passwordCo1.equals(passwordCo2)) {
            etPasswordCo2.setError("las contraseñas son diferentes");
        }

        else {
            GetContainers g = new GetContainers();
            g.execute();

        }

    }

    public class GetContainers extends AsyncTask<URL, String, String> {

        public String name;

        @Override
        protected String doInBackground(URL... params) {

            try {
                URL url = new URL("https://api-rcyclo.herokuapp.com/company_auth/");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestProperty("access-token", access_token);
                conn.setRequestProperty("client", client);
                conn.setRequestProperty("uid", uid);

                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("PUT");

                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");

                JSONObject jsonParam = new JSONObject();

                jsonParam.put("password", passwordCo1);
                jsonParam.put("password_confirmation", passwordCo2);

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
                Toast toast1 =
                        Toast.makeText(getApplicationContext(),
                                "La contraseña ha sido cambiada.", Toast.LENGTH_SHORT);
                toast1.show();

                Intent intent = new Intent(APIChangePass.this, com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company.Settings.class);
                intent.putExtra("access-token", access_token);
                intent.putExtra("client", client);
                intent.putExtra("uid", uid);
                intent.putExtra("name", Company);
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
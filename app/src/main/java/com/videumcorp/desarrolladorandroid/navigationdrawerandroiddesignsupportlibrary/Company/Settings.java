package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Settings extends AppCompatActivity {

    private String access_token;
    private String client;
    private String uid;
    private String Company;

    Toolbar toolbar;
    String emailCompany,phoneCompany;
    TextView emailCo, phoneCo, adressCo, nombreHeaderCo, correoHeaderCo, phoneHeaderCo, passwordCo;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = getIntent();

        access_token = intent.getStringExtra("access-token");
        client = intent.getStringExtra("client");
        uid = intent.getStringExtra("uid");
        Company = intent.getStringExtra("name");

        GetContainers g = new GetContainers();
        g.execute();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        TypedValue typedValueColorPrimaryDark = new TypedValue();
        Settings.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValueColorPrimaryDark, true);
        final int colorPrimaryDark = typedValueColorPrimaryDark.data;
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(colorPrimaryDark);
        }

        emailCo = (TextView) findViewById(R.id.emailCo);
        phoneCo = (TextView) findViewById(R.id.phoneCo);
        adressCo = (TextView) findViewById(R.id.adressCo);
        nombreHeaderCo = (TextView) findViewById(R.id.nombreHeaderCo);
        correoHeaderCo = (TextView) findViewById(R.id.correoHeaderCo);
        phoneHeaderCo = (TextView) findViewById(R.id.phoneHeaderCo);
        passwordCo = (TextView) findViewById(R.id.passwordCo);

        nombreHeaderCo.setText(Company);
        phoneHeaderCo.setText(dateFormat.format(date));
        adressCo.setText("");

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
                editar_direccion(v);
            }
        });

    }

    public void editar_perfil(View view){
        Intent intent = new Intent(this, com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company.APIEditEmail.class);
        intent.putExtra(APIEditEmail.COMPANY, Company);
        startActivity(intent);
        finish();
    }

    public void editar_contraseña(View view){
        Intent intent = new Intent(this, com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company.APIChangePass.class);
        intent.putExtra(com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company.APIChangePass.COMPANY, Company);
        startActivity(intent);
        finish();
    }

    public void editar_direccion(View view){
        Intent intent = new Intent(this, APIEditAddress.class);
        intent.putExtra(APIEditAddress.COMPANY, Company);
        startActivity(intent);
        finish();
    }

    public class GetContainers extends AsyncTask<URL, String, String> {


        public String name;
        @Override
        protected String doInBackground(URL... params) {

            try {
                URL url = new URL("https://api-rcyclo.herokuapp.com/companies/index");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestProperty("access-token", access_token);
                conn.setRequestProperty("client", client);
                conn.setRequestProperty("uid", uid);

                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();

                    String line;

                    while ((line = in.readLine()) != null) {
                        sb.append(line + "\n");
                    }

                    in.close();

                    JSONObject mJsonObject = new JSONObject(sb.toString());

                    String email = mJsonObject.getString("email");

                    return email;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            correoHeaderCo.setText(result);
        }
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

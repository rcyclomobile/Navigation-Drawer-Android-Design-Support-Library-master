package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Set;

public class Settings extends AppCompatActivity {

    private String access_token;
    private String client;
    private String uid;
    private String Company;

    TextView salirse;

    Toolbar toolbar;

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

        TypedValue typedValueColorPrimaryDark = new TypedValue();
        Settings.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValueColorPrimaryDark, true);
        final int colorPrimaryDark = typedValueColorPrimaryDark.data;
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(colorPrimaryDark);
        }

        salirse = (TextView) findViewById(R.id.salirse);

        salirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                builder.setMessage("¿Quieres eliminar la empresa de la aplicación?");
                builder.setTitle("Eliminar empresa");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteAccount d = new DeleteAccount();
                        d.execute();
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
        });
    }

    public void editar_mail(View view, String nameCompany, String addressCompany, String emailCompany){
        Intent intent = new Intent(view.getContext(), EditEmail.class);
        intent.putExtra(EditEmail.COMPANY, Company);
        intent.putExtra(EditEmail.NAME, nameCompany);
        intent.putExtra(EditEmail.ADDRESS, addressCompany);
        intent.putExtra(EditEmail.EMAIL, emailCompany);
        intent.putExtra("access-token", access_token);
        intent.putExtra("client", client);
        intent.putExtra("uid", uid);
        intent.putExtra("name", Company);

        startActivity(intent);
    }

    public void editar_contraseña(View view, String nameCompany ,String addressCompany ,String emailCompany){
        Intent intent = new Intent(view.getContext(), com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company.APIChangePass.class);
        intent.putExtra(com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company.APIChangePass.COMPANY, Company);
        startActivity(intent);
    }

    public void editar_direccion(View view, String nameCompany ,String addressCompany ,String emailCompany){
        Intent intent = new Intent(view.getContext(), EditAddress.class);
        intent.putExtra(EditEmail.COMPANY, Company);
        intent.putExtra(EditEmail.NAME, nameCompany);
        intent.putExtra(EditEmail.ADDRESS, addressCompany);
        intent.putExtra(EditEmail.EMAIL, emailCompany);
        intent.putExtra("access-token", access_token);
        intent.putExtra("client", client);
        intent.putExtra("uid", uid);
        intent.putExtra("name", Company);
        startActivity(intent);
    }

    public class GetContainers extends AsyncTask<URL, String, String> {

        public String name;

        public String nameCompany;
        public String emailCompany;
        public String addressCompany;

        TextView emailCo, phoneCo, adressCo, nombreHeaderCo, correoHeaderCo, phoneHeaderCo, passwordCo;

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

                    nameCompany = mJsonObject.getString("name");
                    emailCompany = mJsonObject.getString("email");
                    addressCompany = mJsonObject.getString("address");

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

            adressCo = (TextView) findViewById(R.id.adressCo);
            nombreHeaderCo = (TextView) findViewById(R.id.nombreHeaderCo);
            correoHeaderCo = (TextView) findViewById(R.id.correoHeaderCo);
            phoneHeaderCo = (TextView) findViewById(R.id.phoneHeaderCo);

            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            nombreHeaderCo.setText(Company);
            phoneHeaderCo.setText(dateFormat.format(date));
            correoHeaderCo.setText(result);
            adressCo.setText(addressCompany);

            emailCo = (TextView) findViewById(R.id.emailCo);
            phoneCo = (TextView) findViewById(R.id.phoneCo);
            passwordCo = (TextView) findViewById(R.id.passwordCo);

            emailCo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    editar_mail(v, nameCompany, addressCompany, emailCompany);
                }
            });

            passwordCo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editar_contraseña(v,nameCompany,addressCompany,emailCompany);
                }
            });

            phoneCo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editar_direccion(v,nameCompany,addressCompany,emailCompany);
                }
            });
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

    public class DeleteAccount extends AsyncTask<URL, String, String> {


        public String name;
        @Override
        protected String doInBackground(URL... params) {

            try {
                URL url = new URL("https://api-rcyclo.herokuapp.com/companies/drop_out");
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

                    return "success";

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
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
                                "Te has desvinculado de R-cyclo con exito.", Toast.LENGTH_SHORT);

                toast1.show();
                Intent intent = new Intent(Settings.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
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

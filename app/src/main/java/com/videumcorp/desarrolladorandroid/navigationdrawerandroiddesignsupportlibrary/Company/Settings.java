package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.DataBase.RcycloDatabaseHelper;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.MyAdapter.Container;
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
import java.util.List;
import java.util.Locale;

public class Settings extends AppCompatActivity {

    private String Company;
    private String Email;

    public String name;

    public String nameCompany;
    public String emailCompany;
    public String addressCompany;

    public String nombreEmpresa;
    public String nombreDireccion;
    public String nombreCorreo;

    TextView emailCo, phoneCo, adressCo, nombreHeaderCo, correoHeaderCo, phoneHeaderCo, passwordCo, nameCo;

    TextView salirse;

    private SQLiteDatabase db;
    private Cursor cursor;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = getIntent();

        Company = intent.getStringExtra("name");
        Email = intent.getStringExtra("email");


        SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(this);
        db = rcycloDatabaseHelper.getReadableDatabase();

        cursor = db.query("COMPANY",
                new String[]{"_id", "NAME", "EMAIL", "PASSWORD", "ADDRESS", "ACTIVE"},
                "EMAIL = ? AND ACTIVE = ?",
                new String[]{Email, "ACTIVO"},
                null, null, null);

        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                nombreEmpresa = cursor.getString(1);
                nombreCorreo = cursor.getString(2);
                nombreDireccion = cursor.getString(4);
            }
        }

        String[] direccion = nombreDireccion.split(":");
        String[] direccion1 = direccion[1].split("\\(");
        String[] direccion2 = direccion1[1].split("\\)");
        String[] direccion3 = direccion2[0].split(",");
        Double latAddress = Double.parseDouble(direccion3[0]);
        Double lngAddress = Double.parseDouble(direccion3[1]);

        String addressShow = getCompleteAddressString(latAddress,lngAddress);

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
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                builder.setMessage("¿Quieres eliminar la empresa de la aplicación?");
                builder.setTitle("Eliminar empresa");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(v.getContext());
                        SQLiteDatabase db = rcycloDatabaseHelper.getWritableDatabase();

                        ContentValues containerValues = new ContentValues();
                        containerValues.put("ACTIVE", "INACTIVE");

                        db.update("COMPANY", containerValues, "EMAIL = ?", new String[]{Email});

                        Toast toast1 =
                                Toast.makeText(getApplicationContext(),
                                        "Te has desvinculado de R-cyclo con exito.", Toast.LENGTH_SHORT);

                        toast1.show();
                        Intent intent = new Intent(Settings.this, Login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
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

        adressCo = (TextView) findViewById(R.id.adressCo);
        nombreHeaderCo = (TextView) findViewById(R.id.nombreHeaderCo);
        correoHeaderCo = (TextView) findViewById(R.id.correoHeaderCo);
        phoneHeaderCo = (TextView) findViewById(R.id.phoneHeaderCo);

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        nombreHeaderCo.setText(nombreEmpresa);
        phoneHeaderCo.setText(dateFormat.format(date));
        correoHeaderCo.setText(nombreCorreo);
        adressCo.setText(addressShow);

        emailCo = (TextView) findViewById(R.id.emailCo);
        phoneCo = (TextView) findViewById(R.id.phoneCo);
        passwordCo = (TextView) findViewById(R.id.passwordCo);
        nameCo = (TextView) findViewById(R.id.nameCo);


        nameCo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                editar_nombre(v, nameCompany, addressCompany, emailCompany);
            }
        });

        emailCo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                editar_mail(v, nameCompany, addressCompany, emailCompany);
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
                editar_direccion(v,nameCompany,addressCompany,emailCompany);
            }
        });
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
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

    public void editar_mail(View view, String nameCompany, String addressCompany, String emailCompany){
        Intent intent = new Intent(view.getContext(), EditEmail.class);
        intent.putExtra(EditEmail.COMPANY, Company);
        intent.putExtra(EditEmail.NAME, nameCompany);
        intent.putExtra(EditEmail.ADDRESS, addressCompany);
        intent.putExtra(EditEmail.EMAIL, emailCompany);
        intent.putExtra("name", Company);
        intent.putExtra("email", Email);

        startActivity(intent);
    }

    public void editar_contraseña(View view){
        Intent intent = new Intent(view.getContext(), ChangePass.class);
        intent.putExtra("name", Company);
        intent.putExtra("email", Email);
        startActivity(intent);
    }

    public void editar_direccion(View view, String nameCompany ,String addressCompany ,String emailCompany){
        Intent intent = new Intent(view.getContext(), EditAddress.class);
        intent.putExtra(EditEmail.COMPANY, Company);
        intent.putExtra(EditEmail.NAME, nameCompany);
        intent.putExtra(EditEmail.ADDRESS, addressCompany);
        intent.putExtra(EditEmail.EMAIL, emailCompany);
        intent.putExtra("name", Company);
        intent.putExtra("email", Email);
        startActivity(intent);
    }

    public void editar_nombre(View view, String nameCompany ,String addressCompany ,String emailCompany){
        Intent intent = new Intent(view.getContext(), EditName.class);
        intent.putExtra(EditEmail.COMPANY, Company);
        intent.putExtra(EditEmail.NAME, nameCompany);
        intent.putExtra(EditEmail.ADDRESS, addressCompany);
        intent.putExtra(EditEmail.EMAIL, emailCompany);
        intent.putExtra("name", Company);
        intent.putExtra("email", Email);
        startActivity(intent);
    }

    public class GetContainers extends AsyncTask<URL, String, String> {

        @Override
        protected String doInBackground(URL... params) {

            try {
                URL url = new URL("https://api-rcyclo.herokuapp.com/companies/index");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

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
            nameCo = (TextView) findViewById(R.id.nameCo);


            nameCo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    editar_nombre(v, nameCompany, addressCompany, emailCompany);
                }
            });

            emailCo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    editar_mail(v, nameCompany, addressCompany, emailCompany);
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
                    editar_direccion(v,nameCompany,addressCompany,emailCompany);
                }
            });
        }
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
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

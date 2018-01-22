package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.DataBase.RcycloDatabaseHelper;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.MyAdapter.AdapterSelEst;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.MyAdapter.Container;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.MyAdapter.Establishment;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SelectEst extends AppCompatActivity {

    public static final String WASTE= "waste";
    ListView listEstablishmentCompany;
    ArrayList<Establishment> arrayList = new ArrayList<>();
    Establishment establishment;
    Toolbar toolbar;
    String waste;
    String estado;
    private SQLiteDatabase db;
    private Cursor cursor;

    private SwipeRefreshLayout swipeContainer;

    private String Company;
    private String Email;
    private String Address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apiselect_esta);

        Intent intent = getIntent();

        waste = (String)getIntent().getExtras().get(WASTE);
        Company = intent.getStringExtra("name");
        Email = intent.getStringExtra("email");
        Address = intent.getStringExtra("address");

        switch (waste) {
            case "Papel":
                estado = "Papel";
                break;

            case "Plastico":
                estado = "Plastico";
                break;

            case "Vidrio":
                estado = "Vidrio";
                break;

            case "Lata":
                estado = "Lata";
                break;
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listEstablishmentCompany = (ListView) findViewById(R.id.list_view_establishment);

        SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(this);
        db = rcycloDatabaseHelper.getReadableDatabase();

        cursor = db.query("ESTABLISHMENT",
                new String[]{"_id", "NAME", "EMAIL", "ADDRESS", "WASTE", "ACTIVE"},
                "WASTE = ? AND ACTIVE = ?",
                new String[]{waste, "ACTIVO"},
                null, null, null);

        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                String[] direccion = cursor.getString(3).split(":");
                String[] direccion1 = direccion[1].split("\\(");
                String[] direccion2 = direccion1[1].split("\\)");
                String[] direccion3 = direccion2[0].split(",");
                Double latAddress = Double.parseDouble(direccion3[0]);
                Double lngAddress = Double.parseDouble(direccion3[1]);
                String addressShow = getCompleteAddressString(latAddress, lngAddress);
                establishment = new Establishment(cursor.getString(1),
                        cursor.getString(2),
                        addressShow,
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(0));
                arrayList.add(establishment);
            }
        }

        if (arrayList.isEmpty()) {
            setContentView(R.layout.activity_main_empty);
            Context context = getApplicationContext();
            CharSequence text = "¿Deseas agregar un contenedor? Puedes hacerlo desde aqui!";
            int duration = Toast.LENGTH_SHORT;

            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast_layout,
                    (ViewGroup) findViewById(R.id.toast_layout_root));

            TextView textToast = (TextView) layout.findViewById(R.id.text_toast);
            textToast.setText(text);

            Toast toast = new Toast(context);
            toast.setDuration(duration);
            toast.setView(layout);
            toast.setGravity(Gravity.TOP | Gravity.LEFT, 150, 0);
            toast.show();
        } else {
            AdapterSelEst adapter = new AdapterSelEst(SelectEst.this, arrayList,Company,Email,Address);
            adapter.notifyDataSetChanged();
            listEstablishmentCompany.setAdapter(adapter);
            swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
            // Setup refresh listener which triggers new data loading
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    arrayList.clear();
                    Intent refresh = new Intent(SelectEst.this, SelectEst.class);
                    refresh.putExtra("name", Company);
                    refresh.putExtra("email", Email);
                    refresh.putExtra("address", Address);
                    refresh.putExtra(SelectEst.WASTE, waste);

                    startActivity(refresh);//Start the same Activity
                    finish(); //finish Activity.
                }
            });
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }

    public class GetContainers extends AsyncTask<URL, String, String> {


        public String name;
        @Override
        protected String doInBackground(URL... params) {

            try {
                URL url = new URL("https://api-rcyclo.herokuapp.com/companies/establishments_by_waste_type?waste_type_id="+waste);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");

                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();

                    String line;

                    while ((line = in.readLine()) != null) {
                        sb.append(line + "\n");
                    }

                    in.close();

                    int largo = sb.toString().length();
                    String sb1 = sb.toString().substring(19, largo - 64);

                    JSONObject mJsonObject = new JSONObject(sb.toString());

                    //String aid = mJsonObject2.getString("id");
                    //String name = mJsonObject.getString("name");
                    //String email = mJsonObject.getString("email");
                    //String aaddress = mJsonObject.getString("address");

                    JSONArray mJsonArrayProperty = mJsonObject.getJSONArray("establishments");
                    for (int i = 0; i < mJsonArrayProperty.length(); i++) {
                        JSONObject mJsonObjectProperty = mJsonArrayProperty.getJSONObject(i);

                        establishment = new Establishment(mJsonObjectProperty.getString("name"),
                                mJsonObjectProperty.getString("uid"),
                                mJsonObjectProperty.getString("address"),
                                estado,
                                mJsonObjectProperty.getString("active"),
                                mJsonObjectProperty.getString("id"));

                        if(mJsonObjectProperty.getString("active").equals("true")){arrayList.add(establishment);}
                    }

                    return "success";

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
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
                if (arrayList.isEmpty()) {
                    setContentView(R.layout.activity_main_empty);
                    Context context = getApplicationContext();
                    CharSequence text = "¿Deseas agregar un contenedor? Puedes hacerlo desde aqui!";
                    int duration = Toast.LENGTH_SHORT;

                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast_layout,
                            (ViewGroup) findViewById(R.id.toast_layout_root));

                    TextView textToast = (TextView) layout.findViewById(R.id.text_toast);
                    textToast.setText(text);

                    Toast toast = new Toast(context);
                    toast.setDuration(duration);
                    toast.setView(layout);
                    toast.setGravity(Gravity.TOP | Gravity.LEFT, 150, 0);
                    toast.show();
                } else {
                    AdapterSelEst adapter = new AdapterSelEst(SelectEst.this, arrayList,Company, Email, Address);
                    adapter.notifyDataSetChanged();
                    listEstablishmentCompany.setAdapter(adapter);
                }
            }
            else{
                Toast toast1 =
                        Toast.makeText(getApplicationContext(),
                                "No hay conexion a internet.", Toast.LENGTH_SHORT);

                toast1.show();
            }

        }
    }


}

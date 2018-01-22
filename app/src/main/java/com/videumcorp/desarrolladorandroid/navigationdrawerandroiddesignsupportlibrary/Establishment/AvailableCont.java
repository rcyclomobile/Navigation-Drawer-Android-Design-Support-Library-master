package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Establishment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.DataBase.RcycloDatabaseHelper;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.MyAdapter.AdapterEst;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.MyAdapter.Container;
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
import java.util.concurrent.ConcurrentMap;

public class AvailableCont extends AppCompatActivity {

    public static final String NAME= "name";
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBar actionBar;
    ListView listContainerCompany;
    ArrayList<Container> arrayList = new ArrayList<>();
    Container container;
    Button btCambiar;

    private SwipeRefreshLayout swipeContainer;

    private String Company;
    private String Email;
    private String Address;

    private SQLiteDatabase db;
    private Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_container);

        listContainerCompany = (ListView) findViewById(R.id.list_view_solicitudes);

        Intent intent = getIntent();

        Company = intent.getStringExtra("name");
        Email = intent.getStringExtra("email");
        Address = intent.getStringExtra("address");

        SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(this);
        db = rcycloDatabaseHelper.getReadableDatabase();

        cursor = db.query("CONTAINER",
                new String[]{"_id", "NAME_CONTAINER", "LATLONG", "ESTABLISHMENT", "COMPANY", "ESTADO", "WASTE", "ACTIVE", "EMAIL_COMPANY"},
                "EMAIL_ESTABLISHMENT = ? AND ACTIVE = ?",
                new String[]{Email, "INACTIVE"},
                null, null, null);

        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                String[] direccion = cursor.getString(2).split(":");
                String[] direccion1 = direccion[1].split("\\(");
                String[] direccion2 = direccion1[1].split("\\)");
                String[] direccion3 = direccion2[0].split(",");
                Double latAddress = Double.parseDouble(direccion3[0]);
                Double lngAddress = Double.parseDouble(direccion3[1]);

                String addressShow = getCompleteAddressString(latAddress,lngAddress);
                container = new Container(cursor.getString(0),
                        cursor.getString(1),
                        addressShow,
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7));
                arrayList.add(container);
            }
        }

        if(!arrayList.isEmpty()) {
            AdapterEst adapter = new AdapterEst(AvailableCont.this, arrayList, Company, Email, Address);
            adapter.notifyDataSetChanged();
            listContainerCompany.setAdapter(adapter);
            swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
            // Setup refresh listener which triggers new data loading
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    arrayList.clear();
                    Intent refresh = new Intent(AvailableCont.this, AvailableCont.class);
                    refresh.putExtra("name", Company);
                    refresh.putExtra("email", Email);
                    refresh.putExtra("address", Address);
                    startActivity(refresh);//Start the same Activity
                    finish(); //finish Activity.
                }
            });
        }
        else {
            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            "No hay solicitudes disponibles", Toast.LENGTH_SHORT);

            toast1.show();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

    public class GetContainers extends AsyncTask<URL, String, String> {


        public String name;
        @Override
        protected String doInBackground(URL... params) {

            listContainerCompany = (ListView) findViewById(R.id.list_view_solicitudes);

            try {
                URL url = new URL("https://api-rcyclo.herokuapp.com/establishments/containers");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

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

                    JSONArray mJsonArrayProperty = mJsonObject.getJSONArray("containers_request");
                    for (int i = 0; i < mJsonArrayProperty.length(); i++) {
                        JSONObject mJsonObjectProperty = mJsonArrayProperty.getJSONObject(i);

                        String[] parts = mJsonObjectProperty.getString("title").split("-");
                        String establishment = parts[0];
                        String waste = parts[1];
                        String[] parts2 = waste.split("\\|");
                        String empresa = parts2[0];
                        String desecho = parts2[1];

                        container = new Container(mJsonObjectProperty.getString("id"),
                                mJsonObjectProperty.getString("title"),
                                mJsonObjectProperty.getString("address"),
                                establishment,
                                empresa ,
                                mJsonObjectProperty.getString("status_id"),
                                desecho,
                                mJsonObjectProperty.getString("active"));

                        if(mJsonObjectProperty.getString("erased").equals("false")&&mJsonObjectProperty.getString("active").equals("false")){arrayList.add(container);}
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
                    if(!arrayList.isEmpty()) {
                        AdapterEst adapter = new AdapterEst(AvailableCont.this, arrayList, Company, Email, Address);
                        adapter.notifyDataSetChanged();
                        listContainerCompany.setAdapter(adapter);
                    }
                else {
                        Toast toast1 =
                                Toast.makeText(getApplicationContext(),
                                        "No hay solicitudes disponibles", Toast.LENGTH_SHORT);

                        toast1.show();
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

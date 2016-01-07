package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Establishment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company.Login;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.MyAdapter.AdapterContEst;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.MyAdapter.Container;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.FullscreenActivity;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.R;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.DataBase.RcycloDatabaseHelper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBar actionBar;
    TextView nameCompany;
    ListView listContainerCompany;
    ArrayList<Container> arrayList = new ArrayList<>();
    Container container;

    private SwipeRefreshLayout swipeContainer;

    private String access_token;
    private String client;
    private String uid;
    private String Company;

    private GoogleMap mMap;

    public String largo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_establisment);

        toolbar = (Toolbar) findViewById(R.id.toolbar_es);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        access_token = intent.getStringExtra("access-token");
        client = intent.getStringExtra("client");
        uid = intent.getStringExtra("uid");
        Company = intent.getStringExtra("name");

        GetContainers g = new GetContainers();
        g.execute();

        GetDirection g2 = new GetDirection();
        g2.execute();

        nameCompany = (TextView) findViewById(R.id.nameCompany);
        nameCompany.setText(Company);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arrayList.clear();
                Intent refresh = new Intent(Main.this, Main.class);
                refresh.putExtra("access-token", access_token);
                refresh.putExtra("client", client);
                refresh.putExtra("uid", uid);
                refresh.putExtra("name",Company);
                startActivity(refresh);//Start the same Activity
                finish(); //finish Activity.
            }
        });


        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);


        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout_es);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view_es);
        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }

        setupNavigationDrawerContent(navigationView);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class GetContainers extends AsyncTask<URL, String, String> {


        public String name;
        @Override
        protected String doInBackground(URL... params) {

            listContainerCompany = (ListView) findViewById(R.id.list_view_solicitudes);

            try {
                URL url = new URL("https://api-rcyclo.herokuapp.com/establishments/containers");
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

                    //String aid = mJsonObject2.getString("id");
                    //String name = mJsonObject.getString("name");
                    //String email = mJsonObject.getString("email");
                    //String aaddress = mJsonObject.getString("address");

                    JSONArray mJsonArrayProperty = mJsonObject.getJSONArray("containers");
                    largo = Integer.toString(mJsonArrayProperty.length());
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

                        if(mJsonObjectProperty.getString("erased").equals("false")&&mJsonObjectProperty.getString("active").equals("true")){arrayList.add(container);}
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
                    Toast toast1 =
                            Toast.makeText(getApplicationContext(),
                                    "No existen contenedores a mostrar.", Toast.LENGTH_SHORT);

                    toast1.show();

                    toolbar = (Toolbar) findViewById(R.id.toolbar_es);
                    setSupportActionBar(toolbar);

                    nameCompany = (TextView) findViewById(R.id.nameCompany);
                    nameCompany.setText(Company);

                    actionBar = getSupportActionBar();
                    actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
                    actionBar.setDisplayHomeAsUpEnabled(true);

                    drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout_es);

                    NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view_es);
                    if (navigationView != null) {
                        setupNavigationDrawerContent(navigationView);
                    }

                    setupNavigationDrawerContent(navigationView);

                } else {
                    AdapterContEst adapter = new AdapterContEst(Main.this, arrayList,access_token,client,uid);
                    adapter.notifyDataSetChanged();
                    listContainerCompany.setAdapter(adapter);
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

    public class GetDirection extends AsyncTask<URL, String, String> {

        public String Lat;
        public String Lng;

        @Override
        protected String doInBackground(URL... params) {

            try {
                URL url = new URL("https://api-rcyclo.herokuapp.com/establishments/index");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestProperty("access-token", access_token);
                conn.setRequestProperty("client", client);
                conn.setRequestProperty("uid", uid);

                /*conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");

                JSONObject jsonParam = new JSONObject();

                jsonParam.put("name", "KITEKNOLOGY");
                jsonParam.put("address", "UTFSM, Valparaiso, Chile");
                jsonParam.put("email", "aleccapetillo1@gmail.com");

                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                out.write(jsonParam.toString());
                out.close();

*/
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();

                    String line;

                    while ((line = in.readLine()) != null) {
                        sb.append(line + "\n");
                    }

                    JSONObject mJsonObject = new JSONObject(sb.toString());

                    //String aid = mJsonObject2.getString("id");
                    Lat = mJsonObject.getString("latitude");
                    Lng = mJsonObject.getString("longitude");

                            in.close();

                    return "success";

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "cagaste";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("success")) {
                setUpMapIfNeeded();
                mMap.setMyLocationEnabled(true);

            }

            else{
                Toast toast1 =
                        Toast.makeText(getApplicationContext(),
                                "Lo sentimos, algo ha ido mal.", Toast.LENGTH_SHORT);

                toast1.show();
            }
        }

        private void setUpMapIfNeeded() {
            // Do a null check to confirm that we have not already instantiated the map.
            if (mMap == null) {
                // Try to obtain the map from the SupportMapFragment.
                mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                        .getMap();
                // Check if we were successful in obtaining the map.
                if (mMap != null) {
                    setUpMap();
                }
            }
        }

        private void setUpMap() {
            GetDirection g = new GetDirection();
            g.execute();
            Double Latitude = Double.parseDouble(Lat);
            Double Longitude = Double.parseDouble(Lng);
            LatLng sanjose = new LatLng(Latitude, Longitude);
            CameraPosition camPos = new CameraPosition.Builder()
                    .target(sanjose)   //Centramos el mapa en sanJose
                    .zoom(16)         //Establecemos el zoom en 16
                    .build();

            CameraUpdate camUpd3 =
                    CameraUpdateFactory.newCameraPosition(camPos);

            final LatLng latLng = new LatLng(Latitude, Longitude);
            mMap.animateCamera(camUpd3);
            mMap.addMarker(new MarkerOptions().position(latLng).title("Actualmente usted tiene "+largo+" contenedores"));
        }
    }

    private void setupNavigationDrawerContent(NavigationView navigationView) {
        final String empresa = (String)getIntent().getExtras().get(Company);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.item_navigation_drawer_inbox_es:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                Intent intent = new Intent(Main.this, AvailableCont.class);
                                intent.putExtra("access-token", access_token);
                                intent.putExtra("client", client);
                                intent.putExtra("uid", uid);
                                intent.putExtra("name", Company);
                                startActivity(intent);
                                return true;
                            case R.id.item_navigation_drawer_starred_es:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                intent = new Intent(Main.this, DeleteCont.class);
                                intent.putExtra("access-token", access_token);
                                intent.putExtra("client", client);
                                intent.putExtra("uid", uid);
                                intent.putExtra("name",Company);
                                startActivity(intent);
                                return true;
                            case R.id.item_navigation_drawer_sent_mail_es:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;

                            case R.id.item_navigation_drawer_settings_es:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                intent = new Intent(Main.this, Settings.class);
                                intent.putExtra("access-token", access_token);
                                intent.putExtra("client", client);
                                intent.putExtra("uid", uid);
                                intent.putExtra("name",Company);
                                startActivity(intent);
                                return true;
                            case R.id.item_navigation_drawer_help_and_feedback_es:
                                menuItem.setChecked(true);
                                intent = new Intent(Main.this,FullscreenActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                Toast.makeText(Main.this, menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                        }
                        return true;
                    }
                });

    }
}


package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.MyAdapter.Container;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.FullscreenActivity;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.MyAdapter.APIMyAdapter;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.R;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.DataBase.RcycloDatabaseHelper;

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

public class APIMain extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBar actionBar;
    TextView nameCompany;
    ListView listContainerCompany;
    ArrayList<Container> arrayList = new ArrayList<>();
    Container container;
    Button btCambiar;
    private SwipeRefreshLayout swipeContainer;



    private String access_token;
    private String client;
    private String uid;
    private String Company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btCambiar = (Button) findViewById(R.id.btCambiar);

        Intent intent = getIntent();

        access_token = intent.getStringExtra("access-token");
        client = intent.getStringExtra("client");
        uid = intent.getStringExtra("uid");
        Company = intent.getStringExtra("name");

        GetContainers g = new GetContainers();
        g.execute();

        nameCompany = (TextView) findViewById(R.id.nameCompany);
        nameCompany.setText(Company);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arrayList.clear();
                Intent refresh = new Intent(APIMain.this, APIMain.class);
                refresh.putExtra("access-token", access_token);
                refresh.putExtra("client", client);
                refresh.putExtra("uid", uid);
                refresh.putExtra("name",Company);
                startActivity(refresh);//Start the same Activity
                finish(); //finish Activity.
            }
        });


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }

        setupNavigationDrawerContent(navigationView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
    //API
    public class GetContainers extends AsyncTask<URL, String, String> {


        public String name;
        @Override
        protected String doInBackground(URL... params) {

            listContainerCompany = (ListView) findViewById(R.id.list_view_containers);

            try {
                URL url = new URL("https://api-rcyclo.herokuapp.com/companies/containers");
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

                    int largo = sb.toString().length();
                    String sb1 = sb.toString().substring(19, largo - 64);

                    JSONObject mJsonObject = new JSONObject(sb.toString());

                    //String aid = mJsonObject2.getString("id");
                    //String name = mJsonObject.getString("name");
                    //String email = mJsonObject.getString("email");
                    //String aaddress = mJsonObject.getString("address");

                    JSONArray mJsonArrayProperty = mJsonObject.getJSONArray("containers");
                    for (int i = 0; i < mJsonArrayProperty.length(); i++) {
                        JSONObject mJsonObjectProperty = mJsonArrayProperty.getJSONObject(i);

                        String[] parts = mJsonObjectProperty.getString("title").split("-");
                        String establishment = parts[0];
                        String waste = parts[1];

                        container = new Container(mJsonObjectProperty.getString("id"),
                                                  mJsonObjectProperty.getString("title"),
                                                  mJsonObjectProperty.getString("address"),
                                                  establishment,
                                                  name ,
                                                  mJsonObjectProperty.getString("status_id"),
                                                  waste,
                                                  mJsonObjectProperty.getString("active"),
                                                  mJsonObjectProperty.getString("description"));

                        arrayList.add(container);
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
                    APIMyAdapter adapter = new APIMyAdapter(APIMain.this, arrayList);
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



    public void borrar(){
        //FALTA API AQUI
        SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(this);
        SQLiteDatabase db = rcycloDatabaseHelper.getWritableDatabase();

        ContentValues companyValues = new ContentValues();

        companyValues.put("ACTIVO", "INNACTIVO");

        db.update("COMPANY", companyValues, "NAME = ?", new String[]{Company});

        ContentValues containerValues = new ContentValues();

        containerValues.put("ESTADO", "Congelado");

        db.update("CONTAINER", containerValues, "COMPANY = ?", new String[]{Company});

        Intent intent = new Intent(APIMain.this, APILogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }


    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.item_navigation_drawer_inbox:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                Intent intent = new Intent(APIMain.this, SelectWaste.class);
                                intent.putExtra("access-token", access_token);
                                intent.putExtra("client", client);
                                intent.putExtra("uid", uid);
                                intent.putExtra("name", Company);
                                startActivity(intent);
                                return true;
                            case R.id.item_navigation_drawer_starred:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                intent = new Intent(APIMain.this, WaitCont.class);
                                intent.putExtra("access-token", access_token);
                                intent.putExtra("client", client);
                                intent.putExtra("uid", uid);
                                intent.putExtra("name", Company);

                                startActivity(intent);
                                return true;
                            case R.id.item_navigation_drawer_sent_mail:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_drafts:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                AlertDialog.Builder builder = new AlertDialog.Builder(APIMain.this);
                                builder.setMessage("¿Quieres eliminar la empresa de la aplicación?");
                                builder.setTitle("Eliminar empresa");
                                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        borrar();
                                        Toast toast = Toast.makeText(getApplicationContext(), "La empresa ha sido eliminada exitosamente.", Toast.LENGTH_SHORT);
                                        toast.show();
                                        finish();
                                    }
                                });

                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                                return true;
                            case R.id.item_navigation_drawer_settings:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                intent = new Intent(APIMain.this, Settings.class);
                                intent.putExtra("access-token", access_token);
                                intent.putExtra("client", client);
                                intent.putExtra("uid", uid);
                                intent.putExtra("name",Company);
                                startActivity(intent);
                                return true;
                            case R.id.item_navigation_drawer_help_and_feedback:
                                menuItem.setChecked(true);
                                intent = new Intent(APIMain.this, FullscreenActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                Toast.makeText(APIMain.this, menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                        }
                        return true;
                    }
                });

    }

}


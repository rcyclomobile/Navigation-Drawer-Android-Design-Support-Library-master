package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Establishment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company.APILogin;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.MyAdapter.APIAdapterContEst;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.MyAdapter.Container;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.FullscreenActivity;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.MyAdapter.APIAdapterEst;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.R;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.DataBase.RcycloDatabaseHelper;

import java.util.ArrayList;

public class APIMain extends AppCompatActivity {

    public static String NAME= "name";

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBar actionBar;
    TextView nameCompany;
    ListView listContainerCompany;
    ArrayList<Container> arrayList = new ArrayList<>();
    Container container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_establisment);

        String fundacion = (String)getIntent().getExtras().get(NAME);
        toolbar = (Toolbar) findViewById(R.id.toolbar_es);
        setSupportActionBar(toolbar);

        listContainerCompany = (ListView) findViewById(R.id.list_view_solicitudes);

        SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(this);
        SQLiteDatabase db = rcycloDatabaseHelper.getWritableDatabase();

        //Implementar API Aqui!!
        Cursor cursor = db.query("CONTAINER", new String[]{"NAME_CONTAINER", "LATLONG", "COMPANY", "ESTADO", "WASTE", "ACTIVO"}, "ESTABLISHMENT = ? AND ACTIVO = ?", new String[]{fundacion,"ACTIVO" }, null, null, null);

        if(cursor.moveToFirst()){
            do {
                container = new Container(cursor.getString(0), cursor.getString(1),fundacion,cursor.getString(2) , cursor.getString(3), cursor.getString(4),cursor.getString(5));
                arrayList.add(container);
            }while (cursor.moveToNext()) ;
        }
        APIAdapterContEst adapter = new APIAdapterContEst(this, arrayList);
        adapter.notifyDataSetChanged();
        listContainerCompany.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);


        nameCompany = (TextView) findViewById(R.id.nameCompany);
        nameCompany.setText(fundacion);

        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout_es);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view_es);
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
    public void borrar(){
        SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(this);
        SQLiteDatabase db = rcycloDatabaseHelper.getWritableDatabase();
        String nameCompany      = (String) getIntent().getExtras().get(NAME);

        ContentValues companyValues = new ContentValues();

        companyValues.put("ACTIVO", "INNACTIVO");

        //Implementar API Aqui!!
        db.update("COMPANY", companyValues, "NAME = ?", new String[]{nameCompany});

        Intent intent = new Intent(APIMain.this, APILogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }


    private void setupNavigationDrawerContent(NavigationView navigationView) {
        final String empresa = (String)getIntent().getExtras().get(NAME);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.item_navigation_drawer_inbox_es:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                Intent intent = new Intent(APIMain.this, APIAvailableCont.class);
                                intent.putExtra(APIMain.NAME, empresa);
                                startActivity(intent);
                                return true;
                            case R.id.item_navigation_drawer_starred_es:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                intent = new Intent(APIMain.this, APIDeleteCont.class);
                                intent.putExtra(APIDeleteCont.NAME, empresa);
                                startActivity(intent);
                                return true;
                            case R.id.item_navigation_drawer_sent_mail_es:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_drafts_es:
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
                            case R.id.item_navigation_drawer_settings_es:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                intent = new Intent(APIMain.this, APISettings.class);
                                intent.putExtra(APISettings.NAME, empresa);
                                startActivity(intent);
                                return true;
                            case R.id.item_navigation_drawer_help_and_feedback_es:
                                menuItem.setChecked(true);
                                intent = new Intent(APIMain.this,FullscreenActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
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


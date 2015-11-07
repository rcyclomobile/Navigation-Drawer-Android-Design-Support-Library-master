package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EstablishmentMainActivity extends AppCompatActivity {

    public static final String NAME= "name";

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBar actionBar;
    TextView nameCompany;
    ListView listContainerCompany;
    ArrayList<Container> arrayList = new ArrayList<>();
    Container container;
    Button btCambiar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_establisment);

        String empresa = (String)getIntent().getExtras().get(NAME);

        btCambiar = (Button) findViewById(R.id.btCambiar);

        listContainerCompany = (ListView) findViewById(R.id.list_view_solicitudes);

        SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(this);
        SQLiteDatabase db = rcycloDatabaseHelper.getWritableDatabase();

        Cursor cursor = db.query("CONTAINER", new String[]{"NAME_CONTAINER", "LATLONG", "COMPANY", "ESTADO", "WASTE", "ACTIVO" }, "ESTABLISHMENT = ? AND ACTIVO = ? AND ESTADO != ?" , new String[]{empresa,"INACTIVO","Congelado"}, null, null, null);

        if(cursor.moveToFirst()){
            do {
                container = new Container(cursor.getString(0), cursor.getString(1),empresa,cursor.getString(2) , cursor.getString(3), cursor.getString(4),cursor.getString(5));
                arrayList.add(container);
            }while (cursor.moveToNext()) ;
        }
        MyAdapterEstablishment adapter = new MyAdapterEstablishment(this, arrayList);
        adapter.notifyDataSetChanged();
        listContainerCompany.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        nameCompany = (TextView) findViewById(R.id.nameCompany);
        nameCompany.setText(empresa);

        toolbar = (Toolbar) findViewById(R.id.toolbar_es);
        setSupportActionBar(toolbar);

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

        db.update("COMPANY", companyValues, "NAME = ?", new String[]{nameCompany});

        Intent intent = new Intent(EstablishmentMainActivity.this, SelectEstablishmentActivity.CompanyLoginActivity.class);
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
                                Intent intent = new Intent(EstablishmentMainActivity.this, AvailableContainerActivity.class);
                                intent.putExtra(EstablishmentMainActivity.NAME, empresa);
                                startActivity(intent);
                                return true;
                            case R.id.item_navigation_drawer_starred_es:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                intent = new Intent(EstablishmentMainActivity.this, DeleteContainerEstablishmentActivity.class);
                                intent.putExtra(DeleteContainerEstablishmentActivity.NAME, empresa);
                                startActivity(intent);
                                return true;
                            case R.id.item_navigation_drawer_sent_mail_es:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_drafts_es:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                AlertDialog.Builder builder = new AlertDialog.Builder(EstablishmentMainActivity.this);
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
                                intent = new Intent(EstablishmentMainActivity.this, SettingsEstablishmentActivity.class);
                                intent.putExtra(SettingsEstablishmentActivity.NAME, empresa);
                                startActivity(intent);
                                return true;
                            case R.id.item_navigation_drawer_help_and_feedback_es:
                                menuItem.setChecked(true);
                                intent = new Intent(EstablishmentMainActivity.this,FullscreenActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                Toast.makeText(EstablishmentMainActivity.this, menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                        }
                        return true;
                    }
                });

    }
}


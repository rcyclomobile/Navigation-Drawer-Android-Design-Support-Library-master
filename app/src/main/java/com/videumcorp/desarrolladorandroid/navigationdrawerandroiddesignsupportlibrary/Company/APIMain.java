package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String empresa = "jumbo";

        btCambiar = (Button) findViewById(R.id.btCambiar);

        listContainerCompany = (ListView) findViewById(R.id.list_view_containers);


        SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(this);
        SQLiteDatabase db = rcycloDatabaseHelper.getWritableDatabase();

        //Implementar API Aqui!!
        Cursor cursor = db.query("CONTAINER", new String[]{"NAME_CONTAINER", "LATLONG", "ESTABLISHMENT", "ESTADO", "WASTE" }, "COMPANY = ? AND ACTIVO = ?", new String[]{empresa, "ACTIVO"}, null, null, null);

        if(cursor.moveToFirst()){
            do {
                container = new Container(cursor.getString(0), cursor.getString(1), cursor.getString(2),empresa , cursor.getString(3), cursor.getString(4),"ACTIVO");
                arrayList.add(container);
                }while (cursor.moveToNext()) ;
        }

        if(arrayList.isEmpty()){
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
            toast.setGravity(Gravity.TOP|Gravity.LEFT, 150, 0);
            toast.show();
        }
        else{
            APIMyAdapter adapter = new APIMyAdapter(this, arrayList);
            adapter.notifyDataSetChanged();
            listContainerCompany.setAdapter(adapter);

        }

        nameCompany = (TextView) findViewById(R.id.nameCompany);
        nameCompany.setText(empresa);

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
    public void borrar(){
        SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(this);
        SQLiteDatabase db = rcycloDatabaseHelper.getWritableDatabase();
        String nameCompany      = "jumbo";

        ContentValues companyValues = new ContentValues();

        companyValues.put("ACTIVO", "INNACTIVO");

        db.update("COMPANY", companyValues, "NAME = ?", new String[]{nameCompany});

        ContentValues containerValues = new ContentValues();

        containerValues.put("ESTADO", "Congelado");

        db.update("CONTAINER", containerValues, "COMPANY = ?", new String[]{nameCompany});

        Intent intent = new Intent(APIMain.this, APILogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }


    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        String empresa = "jumbo";
                        switch (menuItem.getItemId()) {
                            case R.id.item_navigation_drawer_inbox:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                Intent intent = new Intent(APIMain.this, SelectWaste.class);
                                intent.putExtra(SelectWaste.EMPRESA, empresa);
                                startActivity(intent);
                                return true;
                            case R.id.item_navigation_drawer_starred:
                                menuItem.setChecked(true);
                                drawerLayout.closeDrawer(GravityCompat.START);
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
                                intent = new Intent(APIMain.this, APISettings.class);
                                intent.putExtra(APISettings.EMPRESA, empresa);
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


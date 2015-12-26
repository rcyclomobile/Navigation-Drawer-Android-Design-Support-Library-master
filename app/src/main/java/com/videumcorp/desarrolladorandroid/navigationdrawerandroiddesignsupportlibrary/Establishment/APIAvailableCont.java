package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Establishment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.DataBase.RcycloDatabaseHelper;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.MyAdapter.APIAdapterEst;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.MyAdapter.Container;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.MyAdapter.APIAdapterContEst;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.R;

import java.util.ArrayList;

public class APIAvailableCont extends AppCompatActivity {

    public static final String NAME= "name";
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBar actionBar;
    ListView listContainerCompany;
    ArrayList<Container> arrayList = new ArrayList<>();
    Container container;
    Button btCambiar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_container);

        String empresa = (String)getIntent().getExtras().get(NAME);

        btCambiar = (Button) findViewById(R.id.btCambiar);

        listContainerCompany = (ListView) findViewById(R.id.list_view_solicitudes);

        SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(this);
        SQLiteDatabase db = rcycloDatabaseHelper.getWritableDatabase();

        //Implementar API Aqui!!
        Cursor cursor = db.query("CONTAINER", new String[]{"NAME_CONTAINER", "LATLONG", "COMPANY", "ESTADO", "WASTE", "ACTIVO" }, "ESTABLISHMENT = ? AND ACTIVO = ? AND ESTADO != ?" , new String[]{empresa,"INACTIVO","Congelado"}, null, null, null);

        if(cursor.moveToFirst()){
            do {
                container = new Container(cursor.getString(0), cursor.getString(1),empresa,cursor.getString(2) , cursor.getString(3), cursor.getString(4),cursor.getString(5),"asdf","asdf");
                arrayList.add(container);
            }while (cursor.moveToNext()) ;
        }
        APIAdapterEst adapter = new APIAdapterEst(this, arrayList);
        adapter.notifyDataSetChanged();
        listContainerCompany.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

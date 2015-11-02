package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class SolicitudPendiente extends AppCompatActivity {

    /*private SQLiteDatabase db;
    public static final String CONTENEDOR= "contenedor";
    public static final String EMPRESA= "empresa";
    public static final String NAME = "empresa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_pendiente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void confirmar(View view){
        String nameContainer        = (String) getIntent().getExtras().get(CONTENEDOR);
        String nameCompany          = (String) getIntent().getExtras().get(EMPRESA);

        SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(this);
        db = rcycloDatabaseHelper.getWritableDatabase();

        activateContainer(db, nameContainer, nameCompany);

        db.close();

        Intent intent = new Intent(this, EstablishmentMainActivity.class);
        startActivity(intent);

    }

    public static void  activateContainer(SQLiteDatabase db, String nameContainer, String nameCompany) {
        ContentValues containerValues = new ContentValues();
        containerValues.put("ACTIVO", "ACTIVO");
        db.update("CONTAINER", containerValues, "NAME_CONTAINER = ? AND COMPANY = ?", new String[]{nameContainer, nameCompany});
    }
    */

}

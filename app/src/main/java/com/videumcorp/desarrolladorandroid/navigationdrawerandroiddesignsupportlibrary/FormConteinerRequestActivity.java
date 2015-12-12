package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class FormConteinerRequestActivity extends AppCompatActivity {

    private final int DURACION = 300;

    public static final String WASTE = "waste";
    public static final String FUNDACION = "fundacion";
    public static final String COORDENADAS = "coordenadas";
    public static final String EMPRESA= "empresa";

    TextView tvWaste, tvEmpresa, tvFundacion, tvCoordenadas, tvEstado;
    EditText etNombreContenedor;
    private SQLiteDatabase db;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_conteiner_request);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String waste       = (String) getIntent().getExtras().get(WASTE);
        String fundacion   = (String) getIntent().getExtras().get(FUNDACION);
        String coordenadas = (String) getIntent().getExtras().get(COORDENADAS);
        String empresa     = (String) getIntent().getExtras().get(EMPRESA);

        String replacelatlong1 = coordenadas.replace("lat/lng: (", "");
        String replacelatlong2 = replacelatlong1.replace(")", "");
        String[] latlong =  replacelatlong2.split(",");
        double latitude = Double.parseDouble(latlong[0]);
        double longitude = Double.parseDouble(latlong[1]);

        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            final String adddress = addresses.get(0).getAddressLine(0);
            final String city = addresses.get(0).getLocality();
            tvWaste            = (TextView) findViewById(R.id.waste);
            tvEmpresa          = (TextView) findViewById(R.id.empresa);
            tvFundacion        = (TextView) findViewById(R.id.fundacion);
            tvCoordenadas      = (TextView) findViewById(R.id.coordenadas);
            tvEstado           = (TextView) findViewById(R.id.estado);
            etNombreContenedor = (EditText) findViewById(R.id.nombre_contenedor);

            tvWaste.setText(waste);
            tvEmpresa.setText(empresa);
            tvFundacion.setText(fundacion);
            tvCoordenadas.setText(adddress + " ," + city);
            tvEstado.setText("Vacío");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Crouton.cancelAllCroutons();
    }

    public void confirmar(View view){
        final String empresa      = (String) getIntent().getExtras().get(EMPRESA);
        String nombre_contenedor = etNombreContenedor.getText().toString();

        if(nombre_contenedor.matches("")){
            Crouton.makeText(this, "El Contenedor debe tener un nombre", Style.ALERT).show();
        }
        else {
            SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(this);
            db = rcycloDatabaseHelper.getWritableDatabase();
            insertContainer(db, nombre_contenedor, tvCoordenadas.getText().toString(), tvFundacion.getText().toString(), tvEmpresa.getText().toString(), "VACIO", "INACTIVO",tvWaste.getText().toString());
            db.close();
            Crouton.makeText(this, "¡Su Solicitud ha sido enviada!", Style.CONFIRM).show();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent().setClass(FormConteinerRequestActivity.this, CompanyMainActivity.class);
                    intent.putExtra(CompanyMainActivity.EMPRESA, empresa);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, DURACION);
        }
    }

    public static void  insertContainer(SQLiteDatabase db, String nameContainer, String latlong, String establishmentName, String companyName, String estado, String activo, String waste) {
        ContentValues containerValues = new ContentValues();
        containerValues.put("NAME_CONTAINER", nameContainer);
        containerValues.put("LATLONG", latlong);
        containerValues.put("ESTABLISHMENT", establishmentName);
        containerValues.put("COMPANY", companyName);
        containerValues.put("ESTADO", estado);
        containerValues.put("ACTIVO", activo);
        containerValues.put("WASTE", waste);
        db.insert("CONTAINER", null, containerValues);
    }
}
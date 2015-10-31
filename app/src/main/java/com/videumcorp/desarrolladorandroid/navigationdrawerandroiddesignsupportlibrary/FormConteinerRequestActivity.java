package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class FormConteinerRequestActivity extends Activity {

    private final int DURACION = 1000;

    public static final String WASTE = "waste";
    public static final String FUNDACION = "fundacion";
    public static final String COORDENADAS = "coordenadas";
    public static final String EMPRESA= "empresa";

    TextView tvWaste, tvEmpresa, tvFundacion, tvCoordenadas, tvEstado;
    EditText etNombreContenedor;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_conteiner_request);

        String waste       = (String) getIntent().getExtras().get(WASTE);
        String fundacion   = (String) getIntent().getExtras().get(FUNDACION);
        String coordenadas = (String) getIntent().getExtras().get(COORDENADAS);
        String empresa     = (String) getIntent().getExtras().get(EMPRESA);

        tvWaste            = (TextView) findViewById(R.id.waste);
        tvEmpresa          = (TextView) findViewById(R.id.empresa);
        tvFundacion        = (TextView) findViewById(R.id.fundacion);
        tvCoordenadas      = (TextView) findViewById(R.id.coordenadas);
        tvEstado           = (TextView) findViewById(R.id.estado);
        etNombreContenedor = (EditText) findViewById(R.id.nombre_contenedor);

        tvWaste.setText(waste);
        tvEmpresa.setText(empresa);
        tvFundacion.setText(fundacion);
        tvCoordenadas.setText(coordenadas);
        tvEstado.setText("Vacío");
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
            insertContainer(db, nombre_contenedor, tvCoordenadas.getText().toString(), tvFundacion.getText().toString(), tvEmpresa.getText().toString(), "VACIO", "INACTIVO");
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

    public static void  insertContainer(SQLiteDatabase db, String nameContainer, String latlong, String establishmentName, String companyName, String estado, String activo) {
        ContentValues containerValues = new ContentValues();
        containerValues.put("NAME_CONTAINER", nameContainer);
        containerValues.put("LATLONG", latlong);
        containerValues.put("ESTABLISHMENT", establishmentName);
        containerValues.put("COMPANY", companyName);
        containerValues.put("ESTADO", estado);
        containerValues.put("ACTIVO", activo);
        db.insert("CONTAINER", null, containerValues);
    }
}
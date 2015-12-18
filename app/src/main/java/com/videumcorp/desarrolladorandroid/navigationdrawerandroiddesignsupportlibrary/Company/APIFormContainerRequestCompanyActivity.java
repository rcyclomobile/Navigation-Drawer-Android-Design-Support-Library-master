package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.R;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.DataBase.RcycloDatabaseHelper;

public class APIFormContainerRequestCompanyActivity extends AppCompatActivity {

    private final int DURACION = 300;

    public static final String WASTE = "waste";
    public static final String FUNDACION = "fundacion";
    public static final String COORDENADAS = "coordenadas";
    public static final String EMPRESA= "empresa";
    Toolbar toolbar;

    TextView tvWaste, tvEmpresa, tvFundacion, tvCoordenadas, tvEstado;
    EditText etNombreContenedor;
    Button btConfirmar;
    private SQLiteDatabase db;

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

            tvWaste            = (TextView) findViewById(R.id.waste);
            tvEmpresa          = (TextView) findViewById(R.id.empresa);
            tvFundacion        = (TextView) findViewById(R.id.fundacion);
            tvCoordenadas      = (TextView) findViewById(R.id.coordenadas);
            tvEstado           = (TextView) findViewById(R.id.estado);
            etNombreContenedor = (EditText) findViewById(R.id.nombre_contenedor);

            btConfirmar = (Button) findViewById(R.id.confirmar);

            tvWaste.setText(waste);
            tvEmpresa.setText(empresa);
            tvFundacion.setText(fundacion);
            tvCoordenadas.setText(coordenadas);
            tvEstado.setText("Vacío");

        btConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String empresa      = (String) getIntent().getExtras().get(EMPRESA);
                String nombre_contenedor = etNombreContenedor.getText().toString();

                if(nombre_contenedor.matches("")){
                    etNombreContenedor.setError("El campo no puede quedar vacio.");
                }
                else {
                    SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(APIFormContainerRequestCompanyActivity.this);
                    db = rcycloDatabaseHelper.getWritableDatabase();
                    insertContainer(db, nombre_contenedor, tvCoordenadas.getText().toString(), tvFundacion.getText().toString(), tvEmpresa.getText().toString(), "Vacio", "INACTIVO",tvWaste.getText().toString());
                    db.close();
                    Toast toast = Toast.makeText(getApplicationContext(), "Su solicitud ha sido enviada.", Toast.LENGTH_SHORT);
                    toast.show();
                    Intent intent = new Intent().setClass(APIFormContainerRequestCompanyActivity.this, APIMainCompanyActivity.class);
                    intent.putExtra(APIMainCompanyActivity.EMPRESA, empresa);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);

                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
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
        //Implementar API Aqui!!
        db.insert("CONTAINER", null, containerValues);
    }
}
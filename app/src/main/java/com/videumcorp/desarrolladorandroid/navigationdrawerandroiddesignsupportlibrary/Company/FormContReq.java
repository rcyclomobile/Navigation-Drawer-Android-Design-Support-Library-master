package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.DataBase.RcycloDatabaseHelper;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class FormContReq extends AppCompatActivity {

    public static final String WASTE = "waste";
    public static final String FUNDACION = "fundacion";
    public static final String ESTABLISHMENT_ID = "id";
    public static final String ADDRESS = "coordenadas";
    public static final String EMPRESA= "empresa";
    public static final String EMAIL_COMPANY= "email";
    public static final String EMAIL_ESTABLISHMENT= "correo_est";
    Toolbar toolbar;

    TextView tvWaste, tvEmpresa, tvFundacion, tvCoordenadas, tvEstado;
    EditText etNombreContenedor;
    Button btConfirmar;
    private SQLiteDatabase db;

    private String Company;
    public String waste;
    public String establishment_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_conteiner_request);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        waste = (String)getIntent().getExtras().get(WASTE);
        establishment_id = (String)getIntent().getExtras().get(ESTABLISHMENT_ID);
        Company = intent.getStringExtra("name");

        String waste       = (String) getIntent().getExtras().get(WASTE);
        final String fundacion   = (String) getIntent().getExtras().get(FUNDACION);
        final String coordenadas = (String) getIntent().getExtras().get(ADDRESS);
        final String empresa     = (String) getIntent().getExtras().get(EMPRESA);
        final String correo_empresa     = (String) getIntent().getExtras().get(EMAIL_COMPANY);
        final String correo_establishment     = (String) getIntent().getExtras().get(EMAIL_ESTABLISHMENT);

            tvWaste            = (TextView) findViewById(R.id.waste);
            tvEmpresa          = (TextView) findViewById(R.id.empresa);
            tvFundacion        = (TextView) findViewById(R.id.fundacion);
            tvCoordenadas      = (TextView) findViewById(R.id.coordenadas);
            tvEstado           = (TextView) findViewById(R.id.estado);
            etNombreContenedor = (EditText) findViewById(R.id.nombre_contenedor);

            btConfirmar = (Button) findViewById(R.id.confirmar);

        final String desecho;

        switch (waste) {
            case "Papel":
                desecho = "Papel";
                break;

            case "Plastico":
                desecho = "Plastico";
                break;

            case "Vidrio":
                desecho = "Vidrio";
                break;

            case "Lata":
                desecho = "Lata";
                break;

            default:
                desecho = "Otro desecho";
                break;
        }

        String[] direccion = coordenadas.split(":");
        String[] direccion1 = direccion[1].split("\\(");
        String[] direccion2 = direccion1[1].split("\\)");
        String[] direccion3 = direccion2[0].split(",");
        Double latAddress = Double.parseDouble(direccion3[0]);
        Double lngAddress = Double.parseDouble(direccion3[1]);

        String addressShow = getCompleteAddressString(latAddress,lngAddress);
            tvWaste.setText(desecho);
            tvEmpresa.setText(empresa);
            tvFundacion.setText(fundacion);
            tvCoordenadas.setText(addressShow);
            tvEstado.setText("Inactivo");
        Toast toast = Toast.makeText(getApplicationContext(), coordenadas, Toast.LENGTH_SHORT);
        toast.show();


        btConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(v.getContext());
                db = rcycloDatabaseHelper.getWritableDatabase();
                insertContainer(db, "Inactivo", coordenadas, fundacion, empresa, "Vacio", "INACTIVE", desecho, correo_establishment, correo_empresa);
                db.close();
                Toast toast = Toast.makeText(getApplicationContext(), "Su solicitud ha sido enviada.", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent().setClass(FormContReq.this, Main.class);
                intent.putExtra("name", Company);
                intent.putExtra("name", EMAIL_COMPANY);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

    public String getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(this);
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress,1);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();
            return "lat/lng: (" + location.getLatitude() + "," + location.getLongitude() + ")";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "holi";
    }

    public static void  insertContainer(SQLiteDatabase db, String nameContainer, String latlong, String establishmentName, String companyName, String estado, String activo, String waste, String email_establishment, String email_company) {
        ContentValues containerValues = new ContentValues();
        containerValues.put("NAME_CONTAINER", nameContainer);
        containerValues.put("LATLONG", latlong);
        containerValues.put("ESTABLISHMENT", establishmentName);
        containerValues.put("COMPANY", companyName);
        containerValues.put("ESTADO", estado);
        containerValues.put("ACTIVE", activo);
        containerValues.put("WASTE", waste);
        containerValues.put("EMAIL_ESTABLISHMENT", email_establishment);
        containerValues.put("EMAIL_COMPANY", email_company);
        db.insert("CONTAINER", null, containerValues);
    }

}

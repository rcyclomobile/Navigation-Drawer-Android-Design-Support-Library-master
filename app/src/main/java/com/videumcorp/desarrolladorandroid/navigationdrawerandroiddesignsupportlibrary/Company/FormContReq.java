package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FormContReq extends AppCompatActivity {

    private final int DURACION = 300;

    public static final String WASTE = "waste";
    public static final String FUNDACION = "fundacion";
    public static final String ESTABLISHMENT_ID = "id";
    public static final String ADDRESS = "coordenadas";
    public static final String EMPRESA= "empresa";
    Toolbar toolbar;

    TextView tvWaste, tvEmpresa, tvFundacion, tvCoordenadas, tvEstado;
    EditText etNombreContenedor;
    Button btConfirmar;
    private SQLiteDatabase db;

    private String access_token;
    private String client;
    private String uid;
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
        access_token = intent.getStringExtra("access-token");
        client = intent.getStringExtra("client");
        uid = intent.getStringExtra("uid");
        Company = intent.getStringExtra("name");

        String waste       = (String) getIntent().getExtras().get(WASTE);
        String fundacion   = (String) getIntent().getExtras().get(FUNDACION);
        String coordenadas = (String) getIntent().getExtras().get(ADDRESS);
        String empresa     = (String) getIntent().getExtras().get(EMPRESA);

            tvWaste            = (TextView) findViewById(R.id.waste);
            tvEmpresa          = (TextView) findViewById(R.id.empresa);
            tvFundacion        = (TextView) findViewById(R.id.fundacion);
            tvCoordenadas      = (TextView) findViewById(R.id.coordenadas);
            tvEstado           = (TextView) findViewById(R.id.estado);
            etNombreContenedor = (EditText) findViewById(R.id.nombre_contenedor);

            btConfirmar = (Button) findViewById(R.id.confirmar);

        String desecho;

        switch (waste) {
            case "1":
                desecho = "Papel";
                break;

            case "2":
                desecho = "Plastico";
                break;

            case "3":
                desecho = "Vidrio";
                break;

            case "4":
                desecho = "Lata";
                break;

            default:
                desecho = "Otro desecho";
                break;
        }

            tvWaste.setText(desecho);
            tvEmpresa.setText(empresa);
            tvFundacion.setText(fundacion);
            tvCoordenadas.setText(coordenadas);
            tvEstado.setText("Sin estado");

        btConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre_contenedor = etNombreContenedor.getText().toString();

                if(nombre_contenedor.matches("")){
                    etNombreContenedor.setError("El campo no puede quedar vacio.");
                }
                else {
                    GetContainers g = new GetContainers();
                    g.execute();
                }
            }
        });
    }

    public class GetContainers extends AsyncTask<URL, String, String> {


        public String name;
        @Override
        protected String doInBackground(URL... params) {

            try {
                URL url = new URL("https://api-rcyclo.herokuapp.com/companies/create_container_by_company_request?waste_type_id="+waste+"&establishment_id="+establishment_id);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestProperty("access-token", access_token);
                conn.setRequestProperty("client", client);
                conn.setRequestProperty("uid", uid);
                conn.setRequestMethod("GET");

                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();

                    String line;

                    while ((line = in.readLine()) != null) {
                        sb.append(line + "\n");
                    }

                    in.close();

                    return "success";

                } catch (MalformedURLException e) {
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
                Toast toast = Toast.makeText(getApplicationContext(), "Su solicitud ha sido enviada.", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent().setClass(FormContReq.this, APIMain.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                } else {
                Toast toast1 =
                        Toast.makeText(getApplicationContext(),
                                "Lo sentimos, algo ha ido mal.", Toast.LENGTH_SHORT);

                toast1.show();
                }
            }

    }
}

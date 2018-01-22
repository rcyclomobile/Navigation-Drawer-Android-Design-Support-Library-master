package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.DataBase.RcycloDatabaseHelper;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditName extends AppCompatActivity {

    Toolbar toolbar;

    public static final String COMPANY = "empresa";
    public static final String NAME = "empresa";
    public static final String ADDRESS = "direccion";
    public static final String EMAIL = "email";

    TextView nombreHeaderCo;

    private String Company;
    private String Email;

    public String nameCompany;
    public String addressCompany;
    public String emailCompany;
    public String newEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);
        String empresa = (String) getIntent().getExtras().get(COMPANY);

        nameCompany = (String) getIntent().getExtras().get(NAME);
        addressCompany = (String) getIntent().getExtras().get(ADDRESS);
        emailCompany = (String) getIntent().getExtras().get(EMAIL);

        Intent intent = getIntent();

        Company = intent.getStringExtra("name");
        Email = intent.getStringExtra("email");

        EditText etEmail = (EditText) findViewById(R.id.emailCo);
        etEmail.setText(Company);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TypedValue typedValueColorPrimaryDark = new TypedValue();
        EditName.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValueColorPrimaryDark, true);
        final int colorPrimaryDark = typedValueColorPrimaryDark.data;
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(colorPrimaryDark);
        }

        nombreHeaderCo = (TextView) findViewById(R.id.nombreHeaderCo);
        nombreHeaderCo.setText(empresa);
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

    public void aceptar(View view) {
        EditText etEmail = (EditText) findViewById(R.id.emailCo);
        String email = etEmail.getText().toString();
        newEmail = etEmail.getText().toString();

        if (!email.matches("")) {

            SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(this);
            SQLiteDatabase db = rcycloDatabaseHelper.getWritableDatabase();

            ContentValues containerValues = new ContentValues();
            containerValues.put("NAME", newEmail);

            db.update("COMPANY", containerValues, "EMAIL = ?", new String[]{Email});

            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            "La solicitud de cambio de nombre ha sido enviada.", Toast.LENGTH_SHORT);
            toast1.show();

            Intent intent = new Intent(EditName.this, Login.class);
            intent.putExtra("name", newEmail);
            intent.putExtra("email", Email);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            }
        else {
            etEmail.setError("No se puede dejar el nombre en blanco.");
        }

    }

    public class GetContainers extends AsyncTask<URL, String, String> {

        public String name;

        @Override
        protected String doInBackground(URL... params) {

            try {
                URL url = new URL("https://api-rcyclo.herokuapp.com/companies/modify_data");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");

                JSONObject jsonParam = new JSONObject();

                jsonParam.put("name", newEmail);
                jsonParam.put("address", addressCompany);
                jsonParam.put("email", emailCompany);

                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                out.write(jsonParam.toString());
                out.close();


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
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "failed";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result.equals("success")) {
                Toast toast1 =
                        Toast.makeText(getApplicationContext(),
                                "La solicitud de cambio de email ha sido enviada.", Toast.LENGTH_SHORT);
                toast1.show();

                Intent intent = new Intent(EditName.this, Login.class);
                intent.putExtra("name", Company);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
            else{
                Toast toast1 =
                        Toast.makeText(getApplicationContext(),
                                "Lo sentimos, algo ha ido mal.", Toast.LENGTH_SHORT);

                toast1.show();
            }

        }
    }
}
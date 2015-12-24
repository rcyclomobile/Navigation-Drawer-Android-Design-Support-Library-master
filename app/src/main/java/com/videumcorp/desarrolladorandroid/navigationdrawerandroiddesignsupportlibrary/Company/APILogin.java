package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.DataBase.RcycloDatabaseHelper;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class APILogin extends Activity {

    EditText etEmail, etPassword;
    TextView register;
    Button sign_in_button;

    private String access_token;
    private String client;
    private String uid;

    public String active;
    public String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_login);

        etEmail = (EditText) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);

        register = (TextView) findViewById(R.id.register);
        sign_in_button = (Button) findViewById(R.id.sign_in_button);

        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetWasteTypes g = new GetWasteTypes();
                g.execute();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), APIRegister.class);
                startActivity(intent);
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String email    = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email incorrecto");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        if (password.isEmpty()) {
            etPassword.setError("Contraseña incorrecta");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }

    public class GetWasteTypes extends AsyncTask<URL, String, String> {

        @Override
        protected String doInBackground(URL... params) {

            try {
                URL url = new URL("https://api-rcyclo.herokuapp.com/company_auth/sign_in");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");

                JSONObject jsonParam = new JSONObject();

                String email    = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                jsonParam.put("email", email);
                jsonParam.put("password", password);

                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                out.write(jsonParam.toString());
                out.close();

                try {
                    if(conn.getResponseCode() == 200) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder sb = new StringBuilder();

                        String line;

                        while ((line = in.readLine()) != null) {
                            sb.append(line + "\n");
                        }

                        in.close();

                        access_token = conn.getHeaderField("access-token");
                        client = conn.getHeaderField("client");
                        uid = conn.getHeaderField("uid");

                        int largo = sb.toString().length();
                        String sb1 = sb.toString().substring(8, largo - 2);

                        JSONArray mJsonArray = new JSONArray("[" + sb1 + "]");
                        JSONObject mJsonObject = mJsonArray.getJSONObject(0);


                        active = mJsonObject.getString("active");
                        name = mJsonObject.getString("name");


                        return "success";
                    }

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

            String email    = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            //email y pass son validos?
            if (validate()) {
                //La cuenta se encontro en la API
                if(!result.equals("failed")){
                    //La cuenta esta innactiva? preguntar si desea activarla
                    if(active.equals("innactivo")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(APILogin.this);
                        builder.setMessage("Su cuenta actualmente esta inactiva. ¿Desea volver a activarla?");
                        builder.setTitle("Activacion de cuenta");
                        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast toast = Toast.makeText(getApplicationContext(), "Su cuenta ha sido activada exitosamente.", Toast.LENGTH_SHORT);
                                toast.show();
                                //activar cuenta

                                Intent intent = new Intent(APILogin.this, APIMain.class);
                                startActivity(intent);
                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    else{

                        Toast toast = Toast.makeText(getApplicationContext(), "Bienvenido a Rcyclo " + name + "!", Toast.LENGTH_SHORT);
                        toast.show();
                        Intent intent = new Intent(APILogin.this, APIMain.class);
                        intent.putExtra("access-token", access_token);
                        intent.putExtra("client", client);
                        intent.putExtra("uid", uid);

                        startActivity(intent);
                    }

                }
                else{
                    Toast toast1 =
                            Toast.makeText(getApplicationContext(),
                                    "El usuario ingresado no existe", Toast.LENGTH_SHORT);

                    toast1.show();
                }
            }
        }

    }
}
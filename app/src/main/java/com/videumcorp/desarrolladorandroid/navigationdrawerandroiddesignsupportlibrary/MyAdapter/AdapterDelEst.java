package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.MyAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company.Main;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Establishment.DeleteCont;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.R;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.DataBase.RcycloDatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AdapterDelEst extends ArrayAdapter<Container> {

    private final Context context;
    private final ArrayList<Container> itemsArrayList;
    private SQLiteDatabase db;
    ProgressBar progressBar;

    private String access_token;
    private String client;
    private String uid;

    public String container_id;

    public AdapterDelEst(Context context, ArrayList<Container> itemsArrayList, String access_token, String client, String uid) {

        super(context, R.layout.item_list_delete_establishment, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
        this.access_token = access_token;
        this.client = client;
        this.uid = uid;
    }

    public void notifyDataSetChanged(){

    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        // 1. Create inflater
        final LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.item_list_delete_establishment, parent, false);

        // 3. Get the two text view from the rowView
        TextView ContainerName = (TextView) rowView.findViewById(R.id.ContainerName);
        TextView ContainerStatus = (TextView) rowView.findViewById(R.id.ContainerStatus);
        final Button btCambiar = (Button) rowView.findViewById(R.id.btCambiar);
        Button mostrar = (Button) rowView.findViewById(R.id.ContainerStatus);
        ImageView imContenedor = (ImageView) rowView.findViewById(R.id.move_poster);

        //progressBar = (ProgressBar) rowView.findViewById(R.id.progressBar);
        //progressBar.setVisibility(View.VISIBLE);
        //progressBar.setProgressDrawable(rowView.getResources().getDrawable(android.R.drawable.progress_horizontal));

        // 4. Set the text for textView
        ContainerName.setText(itemsArrayList.get(position).getNameContainer());
        if(itemsArrayList.get(position).getStatus().equals("1")) {
            imContenedor.setImageResource(R.drawable.icon_container_vacio);
//            progressBar.setProgress(2);
        }
        else if(itemsArrayList.get(position).getStatus().equals("3")){
            imContenedor.setImageResource(R.drawable.icon_container_lleno);
//            progressBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
//            progressBar.setProgress(100);
        }
        else if(itemsArrayList.get(position).getStatus().equals("2")){
            imContenedor.setImageResource(R.drawable.icon_container_mitad);
//            progressBar.setProgress(50);
        }
        else if(itemsArrayList.get(position).getStatus().equals("4")){
            imContenedor.setImageResource(R.drawable.congelado);
        }

        mostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String estado;
                if(itemsArrayList.get(position).getStatus().equals("1")){
                    estado = "Vacio";
                }
                else if(itemsArrayList.get(position).getStatus().equals("2")){
                    estado = "Medio";
                }
                else if(itemsArrayList.get(position).getStatus().equals("1") ){
                    estado = "Lleno";
                }
                else{
                    estado = "Sin estado";
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage(
                        "Nombre del contenedor: " + "\n" + itemsArrayList.get(position).getNameContainer() + "\n" +
                                "\n" + "Ubicacion: " + "\n" + itemsArrayList.get(position).getLatlong() + "\n" +
                                "\n" + "Fundacion Asociada: " + "\n" + itemsArrayList.get(position).getEstablishment() + "\n" +
                                "\n" + "Estado del contenedor: " + "\n" + estado + "\n" +
                                "\n" + "Tipo de desecho: " + "\n" + itemsArrayList.get(position).getDesecho() + "\n");
                builder.setTitle("Datos del contenedor");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        btCambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage(
                        "¿Esta seguro de eliminar este contenedor?");
                builder.setTitle("Eliminar Contenedor");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        container_id = itemsArrayList.get(position).getId();
                        GetContainers g = new GetContainers();
                        g.execute();

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
        });
        // 5. retrn rowView
        return rowView;
    }

    public class GetContainers extends AsyncTask<URL, String, String> {

        public String name;

        @Override
        protected String doInBackground(URL... params) {

            try {
                URL url = new URL("https://api-rcyclo.herokuapp.com/establishments/delete_container");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestProperty("access-token", access_token);
                conn.setRequestProperty("client", client);
                conn.setRequestProperty("uid", uid);

                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");

                JSONObject jsonParam = new JSONObject();

                jsonParam.put("id_container", container_id);

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
                        Toast.makeText(context,
                                "El contenedor ha sido eliminado con exito.", Toast.LENGTH_SHORT);
                toast1.show();

                DeleteCont activity = (DeleteCont) context;
                activity.finish();
                activity.startActivity(activity.getIntent());
            }
            else{
                Toast toast1 =
                        Toast.makeText(context,
                                "Lo sentimos, algo ha ido mal.", Toast.LENGTH_SHORT);

                toast1.show();
            }

        }
    }

}


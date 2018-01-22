package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.MyAdapter;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.DataBase.RcycloDatabaseHelper;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Establishment.Main;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AdapterContEst extends ArrayAdapter<Container> {

    private final Context context;
    private final ArrayList<Container> itemsArrayList;
    ProgressBar progressBar;

    private RadioButton rbVacio, rbMedio, rbLleno;

    public String id;
    public String new_status;
    private GoogleMap mMap;

    public AdapterContEst(Context context, ArrayList<Container> itemsArrayList, GoogleMap mMap) {
        super(context, R.layout.item_list_container_establishment, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
        this.mMap = mMap;
    }


    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        // 1. Create inflater
        final LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.item_list_container_establishment, parent, false);

        // 3. Get the two text view from the rowView
        TextView ContainerName = (TextView) rowView.findViewById(R.id.ContainerName);
        TextView ContainerStatus = (TextView) rowView.findViewById(R.id.ContainerStatus);
        Button mostrar = (Button) rowView.findViewById(R.id.ContainerStatus);
        final Button btCambiar = (Button) rowView.findViewById(R.id.btCambiar);
        ImageView imContenedor = (ImageView) rowView.findViewById(R.id.move_poster);

        progressBar = (ProgressBar) rowView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgressDrawable(rowView.getResources().getDrawable(android.R.drawable.progress_horizontal));

        // 4. Set the text for textView
        ContainerName.setText(itemsArrayList.get(position).getCompany() + " - " + itemsArrayList.get(position).getDesecho() + " - " + itemsArrayList.get(position).getId());
        if(itemsArrayList.get(position).getStatus().equals("Vacio")) {
            imContenedor.setImageResource(R.drawable.icon_container_vacio);
            progressBar.setProgress(2);
        }
        else if(itemsArrayList.get(position).getStatus().equals("Lleno")){
            imContenedor.setImageResource(R.drawable.icon_container_lleno);
            progressBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            progressBar.setProgress(100);
        }
        else if(itemsArrayList.get(position).getStatus().equals("Medio")){
            imContenedor.setImageResource(R.drawable.icon_container_mitad);
            progressBar.setProgress(50);
        }
        else if(itemsArrayList.get(position).getStatus().equals("4")){
            imContenedor.setImageResource(R.drawable.congelado);
        }

            mostrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    String estado;
                    if(itemsArrayList.get(position).getStatus().equals("Vacio")){
                        estado = "Vacio";
                    }
                    else if(itemsArrayList.get(position).getStatus().equals("Medio")){
                        estado = "Medio";
                    }
                    else if(itemsArrayList.get(position).getStatus().equals("Lleno") ){
                        estado = "Lleno";
                    }
                    else{
                        estado = "Sin estado";
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage(
                            "Nombre del contenedor: " + "\n" + itemsArrayList.get(position).getCompany() + " - " + itemsArrayList.get(position).getDesecho() + " - " + itemsArrayList.get(position).getId() + "\n" +
                                    "\n" + "Ubicacion: " + "\n" + itemsArrayList.get(position).getLatlong() + "\n" +
                                    "\n" + "Empresa Asociada: " + "\n" + itemsArrayList.get(position).getCompany() + "\n" +
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

                final LayoutInflater inflater = (LayoutInflater) context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View dialoglayout = inflater.inflate(R.layout.modify_status_container, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                TextView tvEstado            = (TextView) dialoglayout.findViewById(R.id.estado);

                rbVacio     = (RadioButton) dialoglayout.findViewById(R.id.rbVacio);
                rbMedio    = (RadioButton) dialoglayout.findViewById(R.id.rbMedio);
                rbLleno  = (RadioButton) dialoglayout.findViewById(R.id.rbLleno);

                builder.setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            id = itemsArrayList.get(position).getId();

                            String nameContainer_to_Change = itemsArrayList.get(position).getNameContainer();
                            String nameCompany_to_Change = itemsArrayList.get(position).getCompany();

                            SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(context);
                            SQLiteDatabase db = rcycloDatabaseHelper.getWritableDatabase();

                            ContentValues containerValues = new ContentValues();

                            if(rbVacio.isChecked()){
                                containerValues.put("ESTADO", "Vacio");
                            }

                            else if(rbMedio.isChecked()){
                                containerValues.put("ESTADO", "Medio");
                            }

                            else if(rbLleno.isChecked()){
                                containerValues.put("ESTADO", "Lleno");
                            }
                            db.update("CONTAINER", containerValues, "_id = ? AND NAME_CONTAINER = ? AND COMPANY = ?", new String[]{id,nameContainer_to_Change, nameCompany_to_Change});
                            Toast.makeText(context, "El estado del contenedor ha sido cambiado.",
                                    Toast.LENGTH_SHORT).show();
                            com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Establishment.Main activity = (com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Establishment.Main) context;
                            activity.finish();
                            activity.startActivity(activity.getIntent());
                        }
                    });

                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    builder.setView(dialoglayout);
                    builder.show();
                }
        });
        // 5. retrn rowView
        return rowView;
    }

    public class GetStatus extends AsyncTask<URL, String, String> {

        @Override
        protected String doInBackground(URL... params) {

            try {
                URL url = new URL("https://api-rcyclo.herokuapp.com/establishments/update_state_container?container_id="+id+"&status_id="+new_status);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

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
            if (result.equals("success")) {
                Toast toast1 =
                        Toast.makeText(context,
                                "El estado del contenedor ha sido cambiado.", Toast.LENGTH_SHORT);

                toast1.show();
                Main activity = (Main) context;
                activity.finish();
                activity.startActivity(activity.getIntent());
            } else {
                Toast toast1 =
                        Toast.makeText(context,
                                "Lo sentimos, algo ha ido mal.", Toast.LENGTH_SHORT);

                toast1.show();
            }

        }
    }


}

package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.MyAdapter;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
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

import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company.Main;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.DataBase.RcycloDatabaseHelper;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<Container> {

    private final Context context;
    private final ArrayList<Container> itemsArrayList;
    ProgressBar progressBar;

    public String container_id;
    public String nameContainer_to_Change;
    public String nameCompany_to_Change;

    public MyAdapter(Context context, ArrayList<Container> itemsArrayList) {

        super(context, R.layout.item_list, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        // 1. Create inflater
        final LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.item_list, parent, false);

        // 3. Get the two text view from the rowView
        TextView ContainerName = (TextView) rowView.findViewById(R.id.ContainerName);
        Button mostrar = (Button) rowView.findViewById(R.id.ContainerStatus);
        final Button btCambiar = (Button) rowView.findViewById(R.id.btCambiar);
        ImageView imContenedor = (ImageView) rowView.findViewById(R.id.move_poster);

        progressBar = (ProgressBar) rowView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgressDrawable(rowView.getResources().getDrawable(android.R.drawable.progress_horizontal));

        // 4. Set the text for textView
        final String nameContainter = itemsArrayList.get(position).getCompany()+" - " + itemsArrayList.get(position).getDesecho() + " - " + itemsArrayList.get(position).getId();
        ContainerName.setText(nameContainter);
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
        else if(itemsArrayList.get(position).getStatus().equals("Congelado")){
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
                            "Nombre del contenedor: " + "\n" + nameContainter + "\n" +
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
                final LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View dialoglayout = inflater.inflate(R.layout.activity_company_modify_container, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());


                final RadioButton rbVacio         = (RadioButton) dialoglayout.findViewById(R.id.vacio);
                final RadioButton rbMitad         = (RadioButton) dialoglayout.findViewById(R.id.mitad);
                final RadioButton rbLLeno         = (RadioButton) dialoglayout.findViewById(R.id.lleno);

                String estado                     = itemsArrayList.get(position).getStatus();

                if("Vacio".equals(estado) ){
                    rbVacio.setChecked(true);
                }

                else if("Medio".equals(estado)){
                    rbMitad.setChecked(true);
                }

                else if("Lleno".equals(estado)){
                    rbLLeno.setChecked(true);
                }

                builder.setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String idToChange = itemsArrayList.get(position).getId();
                            nameContainer_to_Change = itemsArrayList.get(position).getNameContainer();
                            nameCompany_to_Change = itemsArrayList.get(position).getCompany();

                            SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(context);
                            SQLiteDatabase db = rcycloDatabaseHelper.getWritableDatabase();

                            ContentValues containerValues = new ContentValues();

                            if(rbVacio.isChecked() == true){
                                containerValues.put("ESTADO", "Vacio");
                            }

                            else if(rbMitad.isChecked() == true){
                                containerValues.put("ESTADO", "Medio");
                            }

                            else if(rbLLeno.isChecked() == true){
                                containerValues.put("ESTADO", "Lleno");
                            }
                            db.update("CONTAINER", containerValues, "_id = ? AND NAME_CONTAINER = ? AND COMPANY = ?", new String[]{idToChange,nameContainer_to_Change, nameCompany_to_Change});
                            Toast.makeText(context, "El estado del contenedor ha sido cambiado.",
                                    Toast.LENGTH_SHORT).show();
                            Main activity = (Main) context;
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
}


package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.MyAdapter;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.PorterDuff;
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

import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company.APIMain;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.R;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.DataBase.RcycloDatabaseHelper;

import java.util.ArrayList;

public class APIMyAdapter extends ArrayAdapter<Container> {

    private final Context context;
    private final ArrayList<Container> itemsArrayList;
    ProgressBar progressBar;

    public APIMyAdapter(Context context, ArrayList<Container> itemsArrayList) {

        super(context, R.layout.item_list, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    public void notifyDataSetChanged(){

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
        TextView ContainerStatus = (TextView) rowView.findViewById(R.id.ContainerStatus);
        final Button btCambiar = (Button) rowView.findViewById(R.id.btCambiar);
        ImageView imContenedor = (ImageView) rowView.findViewById(R.id.move_poster);

        progressBar = (ProgressBar) rowView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgressDrawable(rowView.getResources().getDrawable(android.R.drawable.progress_horizontal));

        // 4. Set the text for textView
        ContainerName.setText(itemsArrayList.get(position).getNameContainer());
        if(itemsArrayList.get(position).getStatus().equals("Vacio")) {
            imContenedor.setImageResource(R.drawable.vacio);
            progressBar.setProgress(2);
        }
        else if(itemsArrayList.get(position).getStatus().equals("Lleno")){
            imContenedor.setImageResource(R.drawable.lleno);
            progressBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            progressBar.setProgress(100);
        }
        else if(itemsArrayList.get(position).getStatus().equals("Medio")){
            imContenedor.setImageResource(R.drawable.medio);
            progressBar.setProgress(50);
        }
        else if(itemsArrayList.get(position).getStatus().equals("Congelado")){
            imContenedor.setImageResource(R.drawable.congelado);
        }

            imContenedor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage(
                            "Nombre del contenedor: " + "\n" + itemsArrayList.get(position).getNameContainer() + "\n" +
                                    "\n" + "Ubicacion: " + "\n" + itemsArrayList.get(position).getLatlong() + "\n" +
                                    "\n" + "Fundacion Asociada: " + "\n" + itemsArrayList.get(position).getEstablishment() + "\n" +
                                    "\n" + "Estado del contenedor: " + "\n" + itemsArrayList.get(position).getStatus() + "\n" +
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

                final String nameContainer        = itemsArrayList.get(position).getNameContainer();
                final String nameCompany          = itemsArrayList.get(position).getCompany();
                String estado               = itemsArrayList.get(position).getStatus();

                if("Vacio".equals(estado) ){
                    rbVacio.setChecked(true);        }

                else if("Medio".equals(estado)){
                    rbVacio.setEnabled(false);
                    rbMitad.setChecked(true);        }

                else if("Lleno".equals(estado)){
                    rbVacio.setEnabled(false);
                    rbMitad.setEnabled(false);
                    rbLLeno.setChecked(true);        }

                if(!itemsArrayList.get(position).getStatus().equals("Lleno")) {
                    builder.setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(v.getContext());
                            SQLiteDatabase db = rcycloDatabaseHelper.getWritableDatabase();

                            ContentValues containerValues = new ContentValues();

                            if (rbMitad.isChecked()) {
                                containerValues.put("ESTADO", "Medio");
                            } else if (rbLLeno.isChecked()) {
                                containerValues.put("ESTADO", "Lleno");
                            }

                            //Implementar API Aqui!!
                            db.update("CONTAINER", containerValues, "NAME_CONTAINER = ? AND COMPANY = ?", new String[]{nameContainer, nameCompany});
                            Toast.makeText(v.getContext(), "El estado del contenedor ha sido cambiado.",
                                    Toast.LENGTH_SHORT).show();
                            APIMain activity = (APIMain) context;
                            activity.finish();
                            activity.startActivity(activity.getIntent());
                        }
                    });

                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                }
                else{
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                }
                builder.setView(dialoglayout);
                builder.show();
            }
        });


        // 5. retrn rowView
        return rowView;
    }

}


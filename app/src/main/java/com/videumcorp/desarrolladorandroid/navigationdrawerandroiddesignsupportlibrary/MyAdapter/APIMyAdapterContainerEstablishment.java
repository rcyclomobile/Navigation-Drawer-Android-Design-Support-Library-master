package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.MyAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Establishment.APIAvailableContainerEstablishmentActivity;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Establishment.NewDirectionEstablishmentActivity;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.R;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.DataBase.RcycloDatabaseHelper;

import java.util.ArrayList;

public class APIMyAdapterContainerEstablishment extends ArrayAdapter<Container> {

    private final Context context;
    private final ArrayList<Container> itemsArrayList;
    private SQLiteDatabase db;
    ProgressBar progressBar;

    public APIMyAdapterContainerEstablishment(Context context, ArrayList<Container> itemsArrayList) {
        super(context, R.layout.item_list_container_establishment, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
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
        final Button btCambiar = (Button) rowView.findViewById(R.id.btCambiar);
        final Button btCambiarNombre = (Button) rowView.findViewById(R.id.btCambiarNombre);
        final Button btCambiarDireccion = (Button) rowView.findViewById(R.id.btCambiarDireccion);
        ImageView imContenedor = (ImageView) rowView.findViewById(R.id.move_poster);

        progressBar = (ProgressBar) rowView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgressDrawable(rowView.getResources().getDrawable(android.R.drawable.progress_horizontal));

        // 4. Set the text for textView
        ContainerName.setText(itemsArrayList.get(position).getNameContainer());
        ContainerStatus.setText("Ver contenedor");
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
                                    "\n" + "Empresa Asociada: " + "\n" + itemsArrayList.get(position).getCompany() + "\n" +
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


        btCambiarNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View dialoglayout = inflater.inflate(R.layout.modify_container_name, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                final EditText etActual = (EditText) dialoglayout.findViewById(R.id.et_NombreActual);
                final EditText etNuevo = (EditText) dialoglayout.findViewById(R.id.et_NuevoNombre);

                etActual.setText(itemsArrayList.get(position).getNameContainer());


                builder.setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(v.getContext());
                        db = rcycloDatabaseHelper.getWritableDatabase();
                        ContentValues containerValues = new ContentValues();
                        containerValues.put("NAME_CONTAINER", etNuevo.getText().toString());
                        //Implementar API Aqui!!
                        db.update("CONTAINER", containerValues, "NAME_CONTAINER = ? AND COMPANY = ?", new String[]{itemsArrayList.get(position).getNameContainer(), itemsArrayList.get(position).getCompany()});
                        db.close();
                        Toast.makeText(v.getContext(), "El nombre del contenedor ha sido cambiado.",
                                Toast.LENGTH_SHORT).show();
                        APIAvailableContainerEstablishmentActivity activity = (APIAvailableContainerEstablishmentActivity) context;
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

        btCambiarDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(context, NewDirectionEstablishmentActivity.class);

                String empresa = itemsArrayList.get(position).getCompany();
                String coordenadas = itemsArrayList.get(position).getLatlong();
                String nombre = itemsArrayList.get(position).getNameContainer();
                String fundacion = itemsArrayList.get(position).getEstablishment();

                intent.putExtra(NewDirectionEstablishmentActivity.NAME,fundacion);
                intent.putExtra(NewDirectionEstablishmentActivity.CONTAINER,nombre);
                intent.putExtra(NewDirectionEstablishmentActivity.LATLONG, coordenadas);
                intent.putExtra(NewDirectionEstablishmentActivity.COMPANY, empresa);

                v.getContext().startActivity(intent);
                ((Activity)context).finish();

            }
        });

        btCambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final String nameContainer        = itemsArrayList.get(position).getNameContainer();
                final String nameCompany          = itemsArrayList.get(position).getCompany();
                String estado               = itemsArrayList.get(position).getStatus();

                if(!estado.equals("Vacio")) {


                    final LayoutInflater inflater = (LayoutInflater) context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    View dialoglayout = inflater.inflate(R.layout.modify_status_container, null);

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                    TextView tvEstado            = (TextView) dialoglayout.findViewById(R.id.estado);

                    tvEstado.setText(estado);

                    builder.setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(v.getContext());
                            SQLiteDatabase db = rcycloDatabaseHelper.getWritableDatabase();

                            ContentValues containerValues = new ContentValues();

                            containerValues.put("ESTADO", "Vacio");

                            //Implementar API Aqui!!
                            db.update("CONTAINER", containerValues, "NAME_CONTAINER = ? AND COMPANY = ?", new String[]{nameContainer, nameCompany});
                            Toast.makeText(v.getContext(), "El estado del contenedor ha sido cambiado.",
                                    Toast.LENGTH_SHORT).show();
                            APIAvailableContainerEstablishmentActivity activity = (APIAvailableContainerEstablishmentActivity) context;
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
                else{
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(v.getContext());
                    dialogo1.setTitle("Cambiar estado");
                    dialogo1.setMessage("El contenedor esta vacio");
                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {

                        }
                    });
                    dialogo1.show();
                }
            }
        });
        // 5. retrn rowView
        return rowView;
    }


}

package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyAdapterContainerEstablishment extends ArrayAdapter<Container> {

    private final Context context;
    private final ArrayList<Container> itemsArrayList;
    private SQLiteDatabase db;

    public MyAdapterContainerEstablishment(Context context, ArrayList<Container> itemsArrayList) {
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


        // 4. Set the text for textView
        ContainerName.setText(itemsArrayList.get(position).getNameContainer());
        ContainerStatus.setText("Ver contenedor");
        if(itemsArrayList.get(position).getStatus().equals("Vacio")) {
            imContenedor.setImageResource(R.drawable.vacio);
        }
        else if(itemsArrayList.get(position).getStatus().equals("Lleno")){
            imContenedor.setImageResource(R.drawable.lleno);
        }
        else if(itemsArrayList.get(position).getStatus().equals("Medio")){
            imContenedor.setImageResource(R.drawable.medio);
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
                        db.update("CONTAINER", containerValues, "NAME_CONTAINER = ? AND COMPANY = ?", new String[]{itemsArrayList.get(position).getNameContainer(), itemsArrayList.get(position).getCompany()});
                        db.close();
                        Toast.makeText(v.getContext(), "El nombre del contenedor ha sido cambiado.",
                                Toast.LENGTH_SHORT).show();
                        AvailableContainerActivity activity = (AvailableContainerActivity) context;
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
                final LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View dialoglayout = inflater.inflate(R.layout.modify_adress_container, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                final EditText etActual = (EditText) dialoglayout.findViewById(R.id.et_DireccionActual);
                final EditText etNuevo = (EditText) dialoglayout.findViewById(R.id.et_NuevaDireccion);

                etActual.setText(itemsArrayList.get(position).getLatlong());


                builder.setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(v.getContext());
                        db = rcycloDatabaseHelper.getWritableDatabase();
                        ContentValues containerValues = new ContentValues();
                        containerValues.put("LATLONG", etNuevo.getText().toString());
                        db.update("CONTAINER", containerValues, "NAME_CONTAINER = ? AND COMPANY = ?", new String[]{itemsArrayList.get(position).getNameContainer(), itemsArrayList.get(position).getCompany()});
                        db.close();
                        Toast.makeText(v.getContext(), "La dirección del contenedor ha sido cambiado.",
                                Toast.LENGTH_SHORT).show();
                        AvailableContainerActivity activity = (AvailableContainerActivity) context;
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

        btCambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View dialoglayout = inflater.inflate(R.layout.modify_status_container, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());


                final RadioButton rbVacio         = (RadioButton) dialoglayout.findViewById(R.id.vacio);
                final RadioButton rbMitad         = (RadioButton) dialoglayout.findViewById(R.id.mitad);
                final RadioButton rbLLeno         = (RadioButton) dialoglayout.findViewById(R.id.lleno);

                final String nameContainer        = itemsArrayList.get(position).getNameContainer();
                final String nameCompany          = itemsArrayList.get(position).getCompany();
                String estado               = itemsArrayList.get(position).getStatus();
                String waste                = itemsArrayList.get(position).getDesecho();

                TextView tvNameContainer     = (TextView) dialoglayout.findViewById(R.id.nombre_contenedor);
                TextView tvNameEstablishment = (TextView) dialoglayout.findViewById(R.id.fundacion);
                TextView tvEstado            = (TextView) dialoglayout.findViewById(R.id.estado);
                TextView tvWaste             = (TextView) dialoglayout.findViewById(R.id.tipo_desecho);

                tvNameContainer.setText(nameContainer);
                tvNameEstablishment.setText(nameCompany);
                tvEstado.setText(estado);
                tvWaste.setText(waste);

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

                            db.update("CONTAINER", containerValues, "NAME_CONTAINER = ? AND COMPANY = ?", new String[]{nameContainer, nameCompany});
                            Toast.makeText(v.getContext(), "El estado del contenedor ha sido cambiado.",
                                    Toast.LENGTH_SHORT).show();
                            AvailableContainerActivity activity = (AvailableContainerActivity) context;
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

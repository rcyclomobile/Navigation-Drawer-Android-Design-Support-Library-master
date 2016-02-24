package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.MyAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.R;

import java.util.ArrayList;

public class WaitAdapter extends ArrayAdapter<Container> {

    private final Context context;
    private final ArrayList<Container> itemsArrayList;

    public WaitAdapter(Context context, ArrayList<Container> itemsArrayList) {

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
        View rowView = inflater.inflate(R.layout.item_list_wait, parent, false);

        // 3. Get the two text view from the rowView
        TextView ContainerName = (TextView) rowView.findViewById(R.id.ContainerName);
        TextView ContainerStatus = (TextView) rowView.findViewById(R.id.ContainerStatus);
        Button mostrar = (Button) rowView.findViewById(R.id.ContainerStatus);
        ImageView imContenedor = (ImageView) rowView.findViewById(R.id.move_poster);

        // 4. Set the text for textView
        ContainerName.setText(itemsArrayList.get(position).getNameContainer());
        if(itemsArrayList.get(position).getStatus().equals("1")) {
            imContenedor.setImageResource(R.drawable.icon_container_vacio);
        }
        else if(itemsArrayList.get(position).getStatus().equals("3")){
            imContenedor.setImageResource(R.drawable.icon_container_lleno);

        }
        else if(itemsArrayList.get(position).getStatus().equals("2")){
            imContenedor.setImageResource(R.drawable.icon_container_mitad);
        }
        else if(itemsArrayList.get(position).getStatus().equals("Congelado")){
            imContenedor.setImageResource(R.drawable.congelado);
        }
        else if(itemsArrayList.get(position).getStatus().equals("null")){
            imContenedor.setImageResource(R.drawable.vacio);
        }

        final String[] desecho = itemsArrayList.get(position).getDesecho().split("\\|");

            mostrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    String estado = "Sin estado";

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage(
                            "Nombre del contenedor: " + "\n" + itemsArrayList.get(position).getNameContainer() + "\n" +
                                    "\n" + "Ubicacion: " + "\n" + itemsArrayList.get(position).getLatlong() + "\n" +
                                    "\n" + "Fundacion Asociada: " + "\n" + itemsArrayList.get(position).getEstablishment() + "\n" +
                                    "\n" + "Estado del contenedor: " + "\n" + estado + "\n" +
                                    "\n" + "Tipo de desecho: " + "\n" + desecho[1] + "\n");
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


        // 5. retrn rowView
        return rowView;
    }

}
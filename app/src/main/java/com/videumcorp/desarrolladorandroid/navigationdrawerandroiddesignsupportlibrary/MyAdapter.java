package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<Container> {

    private final Context context;
    private final ArrayList<Container> itemsArrayList;

    public MyAdapter(Context context, ArrayList<Container> itemsArrayList) {

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


        // 4. Set the text for textView
        ContainerName.setText(itemsArrayList.get(position).getNameContainer());
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
                String nameContainer        = itemsArrayList.get(position).getNameContainer();
                String latLong              = itemsArrayList.get(position).getLatlong();
                String nameEstablishment    = itemsArrayList.get(position).getEstablishment();
                String estateContainer      = itemsArrayList.get(position).getStatus();
                String waste                = itemsArrayList.get(position).getDesecho();
                String nameCompany          = itemsArrayList.get(position).getCompany();


                Intent intent = new Intent(v.getContext(), CompanyModifyContainerActivity.class);
                intent.putExtra(CompanyModifyContainerActivity.NAME_CONTAINER, nameContainer);
                intent.putExtra(CompanyModifyContainerActivity.LATLONG, latLong);
                intent.putExtra(CompanyModifyContainerActivity.ESTABLISHMENT, nameEstablishment);
                intent.putExtra(CompanyModifyContainerActivity.COMPANY, nameCompany);
                intent.putExtra(CompanyModifyContainerActivity.ESTADO, estateContainer);
                intent.putExtra(CompanyModifyContainerActivity.WASTE, waste);

                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });
        // 5. retrn rowView
        return rowView;
    }

}


package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.DataBase.RcycloDatabaseHelper;

public class APISelectEstablishment extends ListActivity {
    private SQLiteDatabase db;
    private Cursor cursor;
    public static final String WASTE= "waste";
    public static final String EMPRESA= "empresa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listEstablishment = getListView();

        String waste = (String)getIntent().getExtras().get(WASTE);

        SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(this);
        db = rcycloDatabaseHelper.getReadableDatabase();

        //Implementar API Aqui!!
        cursor = db.query("ESTABLISHMENT",
                new String[]{"_id", "NAME", "WASTE"},
                "WASTE = ?",
                new String[] {waste},
                null, null, null);


        CursorAdapter listAdapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_2,
                cursor,
                new String[]{"NAME","WASTE"},
                new int[]{android.R.id.text1,android.R.id.text2},
                0);
        listEstablishment.setAdapter(listAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }

    @Override
    public void onListItemClick(ListView listView, View itemView, int position, long id) {
        cursor.moveToFirst();
        cursor.move(position);

        String fundacion = cursor.getString(1);
        String waste = (String)getIntent().getExtras().get(WASTE);
        String empresa = (String)getIntent().getExtras().get(EMPRESA);
        //    String fundacion = listView.getItemAtPosition(position).toString();
        //   String fundacion = Integer.toString(position);

        Intent intent = new Intent(APISelectEstablishment.this, SelectLatlong.class);
        intent.putExtra(SelectLatlong.WASTE, waste);
        intent.putExtra(SelectLatlong.FUNDACION, fundacion);
        intent.putExtra(SelectLatlong.EMPRESA, empresa);

        startActivity(intent);
    }
}
package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Toast;

import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.DataBase.RcycloDatabaseHelper;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.MyAdapter.APIAdapterSelEst;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.MyAdapter.Establishment;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.R;

import java.util.ArrayList;

public class APISelectEst extends AppCompatActivity {

    private SQLiteDatabase db;
    public static final String WASTE= "waste";
    public static final String EMPRESA= "empresa";
    ListView listEstablishmentCompany;
    ArrayList<Establishment> arrayList = new ArrayList<>();
    Establishment establishment;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apiselect_esta);
        String waste = (String)getIntent().getExtras().get(WASTE);
        String empresa = (String)getIntent().getExtras().get(EMPRESA);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listEstablishmentCompany = (ListView) findViewById(R.id.list_view_establishment);

        SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(this);
        db = rcycloDatabaseHelper.getReadableDatabase();

        Cursor cursor = db.query("ESTABLISHMENT", new String[]{"NAME", "EMAIL", "PASSWORD", "PHONE", "ADDRESS", "WASTE", "ACTIVO" }, "ACTIVO = ? AND WASTE = ?", new String[]{"ACTIVO",waste}, null, null, null);

        if(cursor.moveToFirst()){
            do {
                establishment = new Establishment(cursor.getString(0), cursor.getString(1), cursor.getString(2),cursor.getString(3) , cursor.getString(4), cursor.getString(5),cursor.getString(6));
                arrayList.add(establishment);
            }while (cursor.moveToNext()) ;
        }

        if(arrayList.isEmpty()){
            setContentView(R.layout.activity_select_establishment_empty);
        }
        else{
            APIAdapterSelEst adapter = new APIAdapterSelEst(APISelectEst.this, arrayList,empresa);
            adapter.notifyDataSetChanged();
            listEstablishmentCompany.setAdapter(adapter);

        }
    }

}

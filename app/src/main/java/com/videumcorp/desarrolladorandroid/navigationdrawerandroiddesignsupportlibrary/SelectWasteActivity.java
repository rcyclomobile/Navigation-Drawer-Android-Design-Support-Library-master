package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class SelectWasteActivity extends Activity implements View.OnClickListener{

    public String waste;

    Button plastic, paper, tin, glass;
    public static final String EMPRESA= "empresa";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_waste);
<<<<<<< Updated upstream:app/src/main/java/com/videumcorp/desarrolladorandroid/navigationdrawerandroiddesignsupportlibrary/SelectWasteActivity.java
        paper   = (ImageButton) findViewById(R.id.paper);
        plastic = (ImageButton) findViewById(R.id.plastic);
        glass   = (ImageButton) findViewById(R.id.glass);
        tin     = (ImageButton) findViewById(R.id.tin);
=======

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        paper   = (Button) findViewById(R.id.paper);
        plastic = (Button) findViewById(R.id.plastic);
        glass   = (Button) findViewById(R.id.glass);
        tin     = (Button) findViewById(R.id.tin);
>>>>>>> Stashed changes:app/src/main/java/com/videumcorp/desarrolladorandroid/navigationdrawerandroiddesignsupportlibrary/Company/SelectWaste.java

        paper.setOnClickListener(this);
        plastic.setOnClickListener(this);
        glass.setOnClickListener(this);
        tin.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.paper:
                waste = "papel";
                break;

            case R.id.plastic:
                waste = "plastico";
                break;

            case R.id.glass:
                waste = "vidrio";
                break;

            case R.id.tin:
                waste = "lata";
                break;
        }
        String empresa = (String)getIntent().getExtras().get(EMPRESA);
        Intent intent = new Intent(this, SelectEstablishmentActivity.class);
        intent.putExtra(SelectEstablishmentActivity.WASTE, waste);
        intent.putExtra(SelectEstablishmentActivity.EMPRESA, empresa);
        startActivity(intent);
    }
}
/*public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bRegisterEmpresa:
                startActivity(new Intent(this, FormularioEmpresa.class));
                break;

            case R.id.bLoginEmpresa:
                startActivity(new Intent(this, MenuEmpresa.class));
                break;
        }
    }*/
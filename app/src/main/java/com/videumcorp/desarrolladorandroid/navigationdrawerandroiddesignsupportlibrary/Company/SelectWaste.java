package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.R;

public class SelectWaste extends AppCompatActivity implements View.OnClickListener{

    public String waste;
    Button paper, plastic, glass, tin;
    private String Company;
    private String Email;
    private String Address;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_waste);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        Company = intent.getStringExtra("name");
        Email = intent.getStringExtra("email");
        Address = intent.getStringExtra("address");

        paper   = (Button) findViewById(R.id.paper);
        plastic = (Button) findViewById(R.id.plastic);
        glass   = (Button) findViewById(R.id.glass);
        tin     = (Button) findViewById(R.id.tin);

        paper.setOnClickListener(this);
        plastic.setOnClickListener(this);
        glass.setOnClickListener(this);
        tin.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.paper:
                waste = "Papel";
                break;

            case R.id.plastic:
                waste = "Plastico";
                break;

            case R.id.glass:
                waste = "Vidrio";
                break;

            case R.id.tin:
                waste = "Lata";
                break;
        }
        Intent intent = new Intent(this, SelectEst.class);
        intent.putExtra(SelectEst.WASTE, waste);
        intent.putExtra("name", Company);
        intent.putExtra("email", Email);
        intent.putExtra("address", Address);
        startActivity(intent);
    }
}


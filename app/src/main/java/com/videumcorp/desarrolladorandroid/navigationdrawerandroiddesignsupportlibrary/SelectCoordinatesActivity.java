package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SelectCoordinatesActivity extends FragmentActivity implements GoogleMap.OnMapClickListener{

    public static final String WASTE= "waste";
    public static final String FUNDACION= "fundacion";
    public static final String EMPRESA= "empresa";
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_coordinates);
        setUpMapIfNeeded();
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);
        //   mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(FSJ, 10));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(-33.440030, -70.597875)).title("Fundacion San Jose"));
        //mMap.addMarker(new MarkerOptions().position(new LatLng( mMap.getMyLocation().getLatitude(),mMap.getMyLocation().getLongitude())).title("Mi posicion"));
    }

    public void onMapClick(LatLng puntoPulsado) {
        mMap.addMarker(new MarkerOptions()
                .position(puntoPulsado)
                .draggable(true)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));



        String waste = (String)getIntent().getExtras().get(WASTE);
        String fundacion = (String)getIntent().getExtras().get(FUNDACION);
        String empresa = (String)getIntent().getExtras().get(EMPRESA);
        String coordenadas = puntoPulsado.toString();
        Intent intent = new Intent(this, FormConteinerRequestActivity.class);
        intent.putExtra(FormConteinerRequestActivity.WASTE,waste);
        intent.putExtra(FormConteinerRequestActivity.FUNDACION,fundacion);
        intent.putExtra(FormConteinerRequestActivity.EMPRESA, empresa);
        intent.putExtra(FormConteinerRequestActivity.COORDENADAS, coordenadas);
        startActivity(intent);
    }


}

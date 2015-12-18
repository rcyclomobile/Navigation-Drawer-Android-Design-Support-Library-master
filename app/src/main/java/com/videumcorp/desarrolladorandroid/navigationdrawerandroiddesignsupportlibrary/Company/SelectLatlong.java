package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.Company;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary.R;

import java.io.IOException;
import java.util.List;

public class SelectLatlong extends AppCompatActivity {

    public static final String WASTE= "waste";
    public static final String FUNDACION= "fundacion";
    public static final String EMPRESA= "empresa";
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_coordinates);
        setUpMapIfNeeded();
        mMap.setMyLocationEnabled(true);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

    public void onSearch(View view) throws IOException {
        EditText location_tf = (EditText) findViewById(R.id.TFaddress);
        String location = location_tf.getText().toString();
        List<Address> addressList = null;
        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
                Log.w("My Current loction", "Canont get Address!");


            } catch (IOException e) {
                e.printStackTrace();
                Log.w("My Current loction", "Canont get Address!");
            }

            if (addressList.isEmpty()) {
                Toast.makeText(getApplicationContext(), "No se ha encontrado la direccion deseada.",
                        Toast.LENGTH_SHORT).show();
            } else {
                Address address = addressList.get(0);
                final LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                String a = String.valueOf(address.getLatitude());
                String b = String.valueOf(address.getLongitude());

                List<Address> addresses;

                addresses = geocoder.getFromLocation(address.getLatitude(), address.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                final String adddress = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                final String city = addresses.get(0).getLocality();

                mMap.addMarker(new MarkerOptions().position(latLng).title(adddress + " ," + city));
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {

                        marker.showInfoWindow();
                        String replacelatlong1 = latLng.toString().replace("lat/lng: (", "");
                        String replacelatlong2 = replacelatlong1.replace(")", "");
                        String[] latlong = replacelatlong2.split(",");
                        double latitudeNew = Double.parseDouble(latlong[0]);
                        double longitudeNew = Double.parseDouble(latlong[1]);

                        Geocoder geocoder = new Geocoder(SelectLatlong.this);
                        List<Address> addresses;

                        try {
                            addresses = geocoder.getFromLocation(latitudeNew, longitudeNew, 1);
                            final String adddressNew = addresses.get(0).getAddressLine(0);
                            final String cityNew = addresses.get(0).getLocality();
                            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(SelectLatlong.this);
                            dialogo1.setTitle("Direccion Contenedor");
                            dialogo1.setMessage("¿Es correcta esta direccion?" + "\n" + adddressNew + " ," + cityNew);
                            dialogo1.setCancelable(false);
                            dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                    String waste = (String)getIntent().getExtras().get(WASTE);
                                    String fundacion = (String)getIntent().getExtras().get(FUNDACION);
                                    String empresa = (String)getIntent().getExtras().get(EMPRESA);
                                    String coordenadas = adddress + " ," + city;
                                    Intent intent = new Intent(SelectLatlong.this, APIFormContReq.class);
                                    intent.putExtra(APIFormContReq.WASTE,waste);
                                    intent.putExtra(APIFormContReq.FUNDACION,fundacion);
                                    intent.putExtra(APIFormContReq.EMPRESA, empresa);
                                    intent.putExtra(APIFormContReq.COORDENADAS,coordenadas);
                                    startActivity(intent);

                                }
                            });
                            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                }
                            });
                            dialogo1.show();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        return true;
                    }
                });
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    }


    private void setUpMap() {
        LatLng sanjose = new LatLng(-33.440030, -70.597875);
        CameraPosition camPos = new CameraPosition.Builder()
                .target(sanjose)   //Centramos el mapa en sanJose
                .zoom(16)         //Establecemos el zoom en 16
                .build();

        CameraUpdate camUpd3 =
                CameraUpdateFactory.newCameraPosition(camPos);

        mMap.animateCamera(camUpd3);
    }
}
package com.videumcorp.desarrolladorandroid.navigationdrawerandroiddesignsupportlibrary;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class NewDirectionActivity extends AppCompatActivity {

    public static final String NAME = "fundacion";
    public static final String CONTAINER = "nombre";
    public static final String LATLONG = "coordenadas";
    public static final String COMPANY = "empresa";
    private SQLiteDatabase db;
    private GoogleMap mMap;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_direction);
        setUpMapIfNeeded();
        mMap.setMyLocationEnabled(true);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
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
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();

                final LatLng newLatLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title(adddress + " ," + city));
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
                        marker.showInfoWindow();
                        final LayoutInflater inflater = (LayoutInflater) NewDirectionActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        final View dialoglayout = inflater.inflate(R.layout.modify_adress_container, null);

                        AlertDialog.Builder builder = new AlertDialog.Builder(NewDirectionActivity.this);

                        final EditText etActual = (EditText) dialoglayout.findViewById(R.id.et_DireccionActual);
                        final EditText etNuevo = (EditText) dialoglayout.findViewById(R.id.et_NuevaDireccion);

                        final String fundacion = (String) getIntent().getExtras().get(NAME);
                        final String nombre = (String) getIntent().getExtras().get(CONTAINER);
                        final String empresa = (String) getIntent().getExtras().get(COMPANY);
                        final String coordenadas = (String) getIntent().getExtras().get(LATLONG);

                        String replacelatlong1 = coordenadas.replace("lat/lng: (", "");
                        String replacelatlong2 = replacelatlong1.replace(")", "");
                        String[] latlong = replacelatlong2.split(",");
                        double latitudeOld = Double.parseDouble(latlong[0]);
                        double longitudeOld = Double.parseDouble(latlong[1]);

                        Geocoder geocoder = new Geocoder(NewDirectionActivity.this);
                        List<Address> addresses;

                        try {
                            addresses = geocoder.getFromLocation(latitudeOld, longitudeOld, 1);
                            final String adddressOld = addresses.get(0).getAddressLine(0);
                            final String cityOld = addresses.get(0).getLocality();
                            etActual.setText(adddressOld + " ," + cityOld);
                            etNuevo.setText(adddress + " ," + city);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        builder.setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SQLiteOpenHelper rcycloDatabaseHelper = new RcycloDatabaseHelper(NewDirectionActivity.this);
                                db = rcycloDatabaseHelper.getWritableDatabase();
                                ContentValues containerValues = new ContentValues();
                                containerValues.put("LATLONG", newLatLng.toString());
                                db.update("CONTAINER", containerValues, "NAME_CONTAINER = ? AND COMPANY = ?", new String[]{nombre, empresa});
                                db.close();
                                Toast.makeText(getApplicationContext(), "La direccion del contenedor ha sido cambiada.",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(NewDirectionActivity.this, AvailableContainerActivity.class);
                                intent.putExtra(AvailableContainerActivity.NAME, fundacion);
                                startActivity(intent);
                                finish();
                            }
                        });

                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        builder.setView(dialoglayout);
                        builder.show();

                        return true;
                    }
                });
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
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

        LatLng sanjose = new LatLng(-33.440030, -70.597875);
        CameraPosition camPos = new CameraPosition.Builder()
                .target(sanjose)   //Centramos el mapa en Madrid
                .zoom(16)         //Establecemos el zoom en 19
                .build();

        CameraUpdate camUpd3 =
                CameraUpdateFactory.newCameraPosition(camPos);

        mMap.animateCamera(camUpd3);

    }

}
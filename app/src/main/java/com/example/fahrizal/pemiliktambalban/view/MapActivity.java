package com.example.fahrizal.pemiliktambalban.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.fahrizal.pemiliktambalban.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    // A default location (Mesjid Baiturrahman, Banda Aceh)
    private final LatLng mDefaultLocation = new LatLng(5.553991, 95.317409);
    private static final int DEFAULT_ZOOM = 15;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private Double lat, lng;
    private String namaTambal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent i = this.getIntent();
        lat = i.getExtras().getDouble("lat");
        lng = i.getExtras().getDouble("lng");
        namaTambal = i.getExtras().getString("namaTambal");
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        googleMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
        checkLocationPermission();

        if (lat != 0.0 && lng != 0.0){
            MarkerOptions markerOptions = new MarkerOptions();

            LatLng koord = new LatLng(lat,lng);

            // Setting the position for the marker
            markerOptions.position(koord);

            // Setting the title for the marker.
            // This will be displayed on taping the marker
            markerOptions.title(namaTambal);

            // Clears the previously touched position
            googleMap.clear();

            // Animating to the touched position
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(koord));

            // Placing a marker on the touched position
            googleMap.addMarker(markerOptions);

        }else {
            Toast.makeText(this, "Lokasi Belum Ditentukan!!!", Toast.LENGTH_SHORT).show();
        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkLocationPermission();
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Atur Akses Lokasi")
                        .setMessage("OK untuk atur lokasi")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
        }
    }
}


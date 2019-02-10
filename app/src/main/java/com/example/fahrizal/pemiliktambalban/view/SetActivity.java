package com.example.fahrizal.pemiliktambalban.view;

import android.Manifest;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.fahrizal.pemiliktambalban.R;
import com.example.fahrizal.pemiliktambalban.model.Profil;
import com.example.fahrizal.pemiliktambalban.model.SetMap;
import com.example.fahrizal.pemiliktambalban.model.SharedPref;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class SetActivity  extends AppCompatActivity {
    private ImageView img_lihat_profil;
    private Button btn_set_waktu, btn_set_buka, btn_set_lokasi, btn_set_tutup, btn_simpan_pengaturan;
    private String nama,namaTambal,nomorHp,email,password,ulangPassword, urlfotoprofil, urlfototambal;
    private TextView txLat, txLang, txtbuka, txttutup;
    private DatabaseReference ref, rootRef;
    private ArrayList<String> list;
    private StorageReference mStorageRef;
    private final int PICK_IMAGE_REQUEST = 71;
    private static int IMG_RESULT = 1;
    private Uri filePath;
    private int mHour,mMinute;
    private String buka = "";
    private String tutup = "";
    private Switch swtAktif;

    // A default location (Mesjid Baiturrahman, Banda Aceh)
    private final LatLng mDefaultLocation = new LatLng(5.553991, 95.317409);
    private static final int DEFAULT_ZOOM = 15;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    //Variabel menampung latitude dan longitude
    //Simpan lokasi lat sama lng ke variabel ini
    //Jangan lupa yg di AdminEditData.java juga
    //Jangan lupa abis ambel lokasi set text untuk yg lat sama longitudenya
    private Double lat = 0.0;
    private Double lng = 0.0;
    private String key;
    private String status;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        rootRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        Intent intent = this.getIntent();
        key = intent.getExtras().getString("KEY");
        nama = intent.getExtras().getString("nama");
        namaTambal = intent.getExtras().getString("namaTambal");
        nomorHp = intent.getExtras().getString("nomorHp");
        email = intent.getExtras().getString("email");
        password= intent.getExtras().getString("password");
        ulangPassword = intent.getExtras().getString("ulangPassword");
        urlfotoprofil = intent.getExtras().getString("fotoProfil");
        urlfototambal = intent.getExtras().getString("fotoTambal");
        buka= intent.getExtras().getString("buka");
        tutup= intent.getExtras().getString("tutup");
        lat = intent.getExtras().getDouble("lat");
        lng = intent.getExtras().getDouble("lang");
        status = intent.getExtras().getString("status");

        img_lihat_profil = (ImageView) findViewById(R.id.img_lihat_profil);
        txLat = (TextView) findViewById(R.id.txLat);
        txLang = (TextView) findViewById(R.id.txLang);
        btn_set_lokasi = (Button) findViewById(R.id.btn_set_lokasi);
        btn_set_waktu = (Button) findViewById(R.id.btn_set_waktu);
        btn_set_buka = (Button) findViewById(R.id.btn_set_buka);
        btn_set_tutup = (Button) findViewById(R.id.btn_set_tutup);
        btn_simpan_pengaturan = (Button) findViewById(R.id.btn_simpan_pengaturan);
        txtbuka = (TextView) findViewById(R.id.txtbuka);
        txttutup = (TextView) findViewById(R.id.txttutup);
        swtAktif = (Switch) findViewById(R.id.swt_aktif);

        if (status != null){
            if (status.equals("0")){
                swtAktif.setText("Mati");
                swtAktif.setChecked(false);
            }
            else {
                swtAktif.setText("Hidup");
                swtAktif.setChecked(true);
            }
        }else {
            swtAktif.setChecked(true);
            status = "1";
        }

        swtAktif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    swtAktif.setText("Hidup");
                    status="1";
                }
                else {
                    swtAktif.setText("Mati");
                    status="0";
                }
            }
        });

        btn_set_lokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMap();
            }
        });
        //Untuk tampilin map view, variabel lokasinya ada di variabel lat, lng.

        btn_simpan_pengaturan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Membuat object setMap dengan value lat dan lang
                Profil setMap = new Profil(nama, namaTambal, nomorHp, email, password,status, ulangPassword, urlfotoprofil, urlfototambal, lat, lng,buka, tutup);

                //Membuat referensi database ke Lokasi -> key
                ref = rootRef.child("Profil").child(key);
                //Menginput objek setMap kedalam referensi database
                ref.setValue(setMap);
                Toast.makeText(SetActivity.this, "Pengaturan Waktu Telah Disimpan", Toast.LENGTH_SHORT).show();

                //menutup dialog peta
                //Toast.makeText(Admin_TambahDataSekolah.this, "FP null", Toast.LENGTH_SHORT).show();

                    }



        });

        btn_set_buka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(SetActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txtbuka.setText(hourOfDay + ":" + minute);
                                buka = txtbuka.getText().toString();
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        btn_set_tutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(SetActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txttutup.setText(hourOfDay + ":" + minute);
                                tutup = txttutup.getText().toString();
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        img_lihat_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SetActivity.this, ProfilActivity.class);
                i.putExtra("KEY", key);
                i.putExtra("nama", nama);
                i.putExtra("namaTambal", namaTambal);
                i.putExtra("nomorHp", nomorHp);
                i.putExtra("email", email);
                i.putExtra("password", password);
                i.putExtra("ulangPassword", ulangPassword);
                i.putExtra("lat", lat);
                i.putExtra("lang", lng);
                i.putExtra("buka", buka);
                i.putExtra("tutup", tutup);
                i.putExtra("fotoProfil", urlfotoprofil);
                i.putExtra("fotoTambal", urlfototambal);
                i.putExtra("status", status);

                startActivity(i);

                //finish();
            }
        });

    }

    public void ubahStatus(){

    }

    private void getMap () {

        final Dialog dialog = new Dialog(SetActivity.this, android.R.style.Theme_Holo_DialogWhenLarge);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        /////make map clear
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.setContentView(R.layout.dialog_map);////your custom content
        Button btnClose = dialog.findViewById(R.id.btn_close);
        Button btnSubmit = dialog.findViewById(R.id.btn_submit);
        MapView mapView = dialog.findViewById(R.id.mapView);

        MapsInitializer.initialize(SetActivity.this);

        mapView.onCreate(dialog.onSaveInstanceState());
        mapView.onResume();

        //Method untuk menampilkan peta
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                googleMap.moveCamera(CameraUpdateFactory
                        .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                checkLocationPermission();

                //listener untuk membuat marker pada lokasi yang diklik
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        // Creating a marker
                        MarkerOptions markerOptions = new MarkerOptions();

                        // Setting the position for the marker
                        markerOptions.position(latLng);

                        // Setting the title for the marker.
                        // This will be displayed on taping the marker
                        markerOptions.title(latLng.latitude + "\n" + latLng.longitude);
                        lat = latLng.latitude;
                        lng = latLng.longitude;

                        // Clears the previously touched position
                        googleMap.clear();

                        // Animating to the touched position
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                        // Placing a marker on the touched position
                        googleMap.addMarker(markerOptions);
                        Toast.makeText(SetActivity.this, "lat :"+lat+"\n lng:"+lng, Toast.LENGTH_SHORT).show();

                        //Toast.makeText(MapCobaActivity.this, "Latitude : "+latLng.latitude+"\nLongitude: "+latLng.longitude, Toast.LENGTH_SHORT).show();
                    }
                });

                if (ActivityCompat.checkSelfPermission(SetActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SetActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    checkLocationPermission();
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Membuat object setMap dengan value lat dan lang
                Profil setMap = new Profil(nama, namaTambal, nomorHp, email, password, status, ulangPassword, urlfotoprofil, urlfototambal, lat, lng,buka, tutup);

                //Membuat referensi database ke Lokasi -> key
                ref = rootRef.child("Profil").child(key);
                //Menginput objek setMap kedalam referensi database


                //Mengeset textview lat dan lang dengan nilai lat dan lang yg dipilih
                txLang.setText(String.valueOf(lng));
                txLat.setText(String.valueOf(lat));

                //menutup dialog peta
                dialog.dismiss();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //Method untuk mengecek izin lokasi pengguna
    private void checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new android.app.AlertDialog.Builder(this)
                        .setTitle("Atur Akses Lokasi")
                        .setMessage("OK untuk atur lokasi")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(SetActivity.this,
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

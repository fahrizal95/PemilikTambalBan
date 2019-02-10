package com.example.fahrizal.pemiliktambalban.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fahrizal.pemiliktambalban.R;
import com.example.fahrizal.pemiliktambalban.model.SharedPref;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfilActivity extends AppCompatActivity {
    private TextView txNama;
    private TextView txNamaTambal;
    private TextView txNomor;
    private TextView txEmail;
    private TextView txPassword;
    private Button btn_edit;
    private Button btn_lihat_map;
    private ImageView img_profil;
    private ImageView img_tambal;
    private ImageView img_home;
    private String nama;
    private String namaTambal;
    private String nomorHp;
    private String email;
    private String buka = "";
    private String tutup = "";
    private String password;
    private String ulangPassword;
    private String urlfotoprofil;
    private String urlfototambal;
    private int id;
    private MapView mapView;
    private SharedPref sharedPref;
    private DatabaseReference databaseReference;
    private String key;
    private Double lat, lng;
    private String status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        sharedPref = new SharedPref(this);

        txNama = (TextView) findViewById(R.id.txNama);
        txNamaTambal = (TextView) findViewById(R.id.txNamaTambal);
        txNomor = (TextView) findViewById(R.id.txNomor);
        txEmail = (TextView) findViewById(R.id.txEmail);
        txPassword = (TextView) findViewById(R.id.txPassword);
        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_lihat_map = (Button) findViewById(R.id.btn_lihat_map);
        img_home = (ImageView) findViewById(R.id.img_home);
        img_profil = (ImageView) findViewById(R.id.img_profil);
        img_tambal = (ImageView) findViewById(R.id.img_tambal);

        databaseReference= FirebaseDatabase.getInstance().getReference();


        //Untuk tampilin map view, variabel lokasinya ada di variabel lat, lng.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.btn_lihat_map);
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
        lat = intent.getExtras().getDouble("lat");
        lng = intent.getExtras().getDouble("lang");
        buka = intent.getExtras().getString("buka");
        tutup = intent.getExtras().getString("tutup");
        status = intent.getExtras().getString("status");

        txNama.setText(nama);
        txNamaTambal.setText(namaTambal);
        txNomor.setText(nomorHp);
        txEmail.setText(email);
        txPassword.setText(password);

        Glide.with(this).load(urlfotoprofil).into(img_profil);
        Glide.with(this).load(urlfototambal).into(img_tambal);


        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfilActivity.this, EditProfilActivity.class);
                i.putExtra("KEY", key);
                i.putExtra("nama", nama);
                i.putExtra("namaTambal", namaTambal);
                i.putExtra("nomorHp", nomorHp);
                i.putExtra("email", email);
                i.putExtra("password", password);
                i.putExtra("ulangPassword", ulangPassword);
                i.putExtra("fotoProfil", urlfotoprofil);
                i.putExtra("fotoTambal", urlfototambal);
                i.putExtra("buka",buka );
                i.putExtra("tutup", tutup);
                i.putExtra("lat", lat);
                i.putExtra("lang", lng);
                i.putExtra("status", status);

                startActivity(i);
            }
        });

        btn_lihat_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfilActivity.this, MapActivity.class);
                i.putExtra("lat",lat);
                i.putExtra("lng",lng);
                i.putExtra("namaTambal",namaTambal);
                startActivity(i);
                //finish();
            }
        });

        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfilActivity.this, SetActivity.class);
                startActivity(i);
                //finish();
            }
        });


}}

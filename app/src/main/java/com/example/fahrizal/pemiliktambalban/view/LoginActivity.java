package com.example.fahrizal.pemiliktambalban.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fahrizal.pemiliktambalban.R;
import com.example.fahrizal.pemiliktambalban.model.Profil;
import com.example.fahrizal.pemiliktambalban.model.SharedPref;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity {
    private EditText edt_login_email;
    private EditText edt_login_password;
    private Button btn_masuk;
    private TextView txt_belum_daftar;
    private SharedPref sharedPref;
    private ArrayList<Profil> data;
    private DatabaseReference ref, rootRef;
    private Boolean login = false;
    private String key,nama,namaTambal,nomorHp,email,password,ulangPassword, urlfotoprofil, urlfototambal, status;
    private String buka = "";
    private String tutup = "";
    private Double lat = 0.0;
    private Double lng = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPref = new SharedPref(this);
        rootRef = FirebaseDatabase.getInstance().getReference().child("Profil");


        edt_login_email = (EditText) findViewById(R.id.edt_login_email);
        edt_login_password = (EditText) findViewById(R.id.edt_login_password);
        btn_masuk = (Button) findViewById(R.id.btn_masuk);
        txt_belum_daftar = (TextView) findViewById(R.id.txt_belum_daftar);
        ambil();


        btn_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int a = 0; a<data.size();a++){
                    if (edt_login_email.getText().toString(). equals(data.get(a).getEmail()) && edt_login_password.getText().toString().equals(data.get(a).getPassword())){
                        key= data.get(a).getKey();
                        nama= data.get(a).getNama();
                        namaTambal= data.get(a).getNamaTambal();
                        nomorHp= data.get(a).getNomorHp();
                        email= data.get(a).getEmail();
                        password= data.get(a).getPassword();
                        ulangPassword= data.get(a).getUlangPassword();
                        urlfotoprofil= data.get(a).getUrlFotoProfil();
                        urlfototambal= data.get(a).getUrlFotoTambal();
                        buka= data.get(a).getTxtbuka();
                        tutup= data.get(a).getTxttutup();
                        lat= data.get(a).getLatitude();
                        lng= data.get(a).getLongitude();
                        status= data.get(a).getStatus();

                        login = true;
                    }
                }
                if (login){
                    Intent i = new Intent(LoginActivity.this, SetActivity.class);
                    i.putExtra("KEY", key);
                    i.putExtra("nama", nama);
                    i.putExtra("namaTambal", namaTambal);
                    i.putExtra("nomorHp", nomorHp);
                    i.putExtra("email", email);
                    i.putExtra("password", password);
                    i.putExtra("ulangPassword", ulangPassword);
                    i.putExtra("fotoProfil", urlfotoprofil);
                    i.putExtra("fotoTambal", urlfototambal);
                    i.putExtra("buka", buka);
                    i.putExtra("tutup", tutup);
                    i.putExtra("lat", lat);
                    i.putExtra("lang", lng);
                    i.putExtra("status", status);
                    startActivity(i);
                    finish();
                }else {
                    Toast.makeText(LoginActivity.this, "Login gagal !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

                txt_belum_daftar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(LoginActivity.this, RegistrasiActivity.class);
                        startActivity(i);
                        //finish();
                    }
                });
    }

    public void ambil(){
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data = new ArrayList<>();
                data.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Profil profil = ds.getValue(Profil.class);
                    String key = ds.getKey();
                    profil.setKey(key);
                    data.add(profil);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}

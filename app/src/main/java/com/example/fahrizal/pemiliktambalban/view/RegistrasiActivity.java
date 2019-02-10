package com.example.fahrizal.pemiliktambalban.view;

import android.Manifest;
import android.app.Dialog;
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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fahrizal.pemiliktambalban.R;
import com.example.fahrizal.pemiliktambalban.model.ListProfil;
import com.example.fahrizal.pemiliktambalban.model.Profil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
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

public class RegistrasiActivity extends AppCompatActivity {

    private EditText tls_nama;
    private EditText tls_nama_tambal;
    private EditText tls_nomor_hp;
    private EditText tls_email;
    private EditText tls_password;
    private EditText tls_ulang_password;
    private Button btn_foto_profil;
    private Button btn_foto_tambal;
    private Button btn_daftar;
    private ImageView tls_img_foto_profil;
    private ImageView tls_img_foto_tambal;
    private String urlfototambal = "https://firebasestorage.googleapis.com/v0/b/pencarian-tambal-ban.appspot.com/o/download%20(1).jpg?alt=media&token=70298477-e37a-4ad3-b97b-ae98c1da9bf6";
    private String urlfotoprofil = "https://firebasestorage.googleapis.com/v0/b/pencarian-tambal-ban.appspot.com/o/download%20(1).jpg?alt=media&token=70298477-e37a-4ad3-b97b-ae98c1da9bf6";
    private DatabaseReference ref, rootRef;
    private StorageReference mStorageRef;
    private final int PICK_IMAGE_REQUEST = 71;
    private final int PICK_IMAGE_REQUEST2 = 72;
    private static int IMG_RESULT = 1;
    String key;
    private Uri filePath, filePath2;
    private Double lat = 0.0;
    private Double lng = 0.0;
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        rootRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        tls_img_foto_profil = (ImageView) findViewById(R.id.tls_img_foto_profil);
        tls_img_foto_tambal = (ImageView) findViewById(R.id.tls_img_foto_tambal);
        btn_foto_profil = (Button) findViewById(R.id.btn_foto_profil);
        btn_foto_tambal = (Button) findViewById(R.id.btn_foto_tambal);
        btn_daftar = (Button) findViewById(R.id.btn_daftar);
        tls_nama = (EditText) findViewById(R.id.tls_nama);
        tls_nama_tambal = (EditText) findViewById(R.id.tls_nama_tambal);
        tls_nomor_hp = (EditText) findViewById(R.id.tls_nomor_hp);
        tls_email = (EditText) findViewById(R.id.tls_email);
        tls_password = (EditText) findViewById(R.id.tls_password);
        tls_ulang_password = (EditText) findViewById(R.id.tls_ulang_password);
        rootRef = FirebaseDatabase.getInstance().getReference();
        ref = rootRef.child("Profil").push();
        key = ref.getKey();

        //Membuat listener untuk btnFotoProfil
        btn_foto_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE_REQUEST);
            }
        });

        //Membuat listener untuk btnFotoTambal
        btn_foto_tambal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE_REQUEST2);
            }
        });

        //Membuat listener untuk btnDaftar
        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_daftar.setClickable(false);

                //Memanggil method uploadImageProfil
                uploadImageProfil();
                /*if (filePath != null){
                    uploadImageProfil();
                    //Toast.makeText(AdminEditData.this, "FP not null", Toast.LENGTH_SHORT).show();
                }else {
                    simpanData();
                    //Toast.makeText(AdminEditData.this, "FP null", Toast.LENGTH_SHORT).show();

                }
                btn_daftar.setClickable(false);
                if (filePath != null){
                    uploadImageTambal();
                    //Toast.makeText(AdminEditData.this, "FP not null", Toast.LENGTH_SHORT).show();
                }else {
                    simpanData();
                    //Toast.makeText(AdminEditData.this, "FP null", Toast.LENGTH_SHORT).show();

                }*/
            }
        });
    }


    private void simpanData(){
        final Profil profil = new Profil(tls_nama.getText().toString(),
                tls_nama_tambal.getText().toString(), tls_nomor_hp.getText().toString(), tls_email.getText().toString(),
                tls_password.getText().toString(), "1", tls_ulang_password.getText().toString(), urlfotoprofil, urlfototambal);

        ref.setValue(profil, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null){
                    Toast.makeText(RegistrasiActivity.this, "Gagal Menambah Data : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(RegistrasiActivity.this, "Berhasil Menambah Data", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegistrasiActivity.this, SetActivity.class);
                    intent.putExtra("KEY", key);
                    intent.putExtra("nama", tls_nama.getText().toString());
                    intent.putExtra("namaTambal", tls_nama_tambal.getText().toString());
                    intent.putExtra("nomorHp", tls_nomor_hp.getText().toString());
                    intent.putExtra("email", tls_email.getText().toString());
                    intent.putExtra("password", tls_password.getText().toString());
                    intent.putExtra("ulangPassword", tls_ulang_password.getText().toString());
                    intent.putExtra("fotoProfil", urlfotoprofil);
                    intent.putExtra("lat", lat);
                    intent.putExtra("lang", lng);
                    intent.putExtra("status", "1");
                    intent.putExtra("fotoTambal", urlfototambal);

                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    //Method untuk mengupload foto
    private void uploadImageProfil() {

        //Mengecek apakah ada file poto yang dipilih
        if(filePath != null)
        {
            StorageReference ref = mStorageRef.child("Profil"+filePath.getLastPathSegment());
            UploadTask uploadTask = ref.putFile(filePath);

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toast.makeText(RegistrasiActivity.this, "Gagal Upload Foto "+exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    urlfotoprofil = downloadUrl.toString();
                    uploadImageTambal();
                }
            });
        }else {
            uploadImageTambal();
        }
    }

    private void uploadImageTambal() {

        if(filePath2 != null)
        {
            StorageReference ref = mStorageRef.child("Tambal"+filePath2.getLastPathSegment());
            UploadTask uploadTask = ref.putFile(filePath2);

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toast.makeText(RegistrasiActivity.this, "Gagal Upload Foto "+exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    urlfototambal = downloadUrl.toString();
                    simpanData();
                }
            });
        }else {
            simpanData();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            if (requestCode == PICK_IMAGE_REQUEST){
                filePath = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    tls_img_foto_profil.setImageBitmap(bitmap);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }else {
                filePath2 = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath2);
                    tls_img_foto_tambal.setImageBitmap(bitmap);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

        }
    }


}

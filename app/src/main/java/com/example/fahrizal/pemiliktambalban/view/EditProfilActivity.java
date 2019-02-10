package com.example.fahrizal.pemiliktambalban.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fahrizal.pemiliktambalban.R;
import com.example.fahrizal.pemiliktambalban.model.Profil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class EditProfilActivity extends AppCompatActivity {

    private String nama,namaTambal,nomorHp,email,password, urlfotoprofil, urlfototambal;
    private String buka = "";
    private String tutup = "";
    private Double lat, lng;
    private EditText edt_nama;
    private EditText edt_nama_tambal;
    private EditText edt_nomor_hp;
    private EditText edt_email;
    private Button btn_edt_foto_profil;
    private Button btn_edt_foto_tambal;
    private Button btn_simpan;
    private ImageView edt_img_foto_profil;
    private ImageView edt_img_foto_tambal;
//    private String urlfototambal = "https://firebasestorage.googleapis.com/v0/b/pencarian-tambal-ban.appspot.com/o/download%20(1).jpg?alt=media&token=70298477-e37a-4ad3-b97b-ae98c1da9bf6";
//    private String urlfotoprofil = "https://firebasestorage.googleapis.com/v0/b/pencarian-tambal-ban.appspot.com/o/download%20(1).jpg?alt=media&token=70298477-e37a-4ad3-b97b-ae98c1da9bf6";
    private DatabaseReference ref, rootRef;
    private StorageReference mStorageRef;
    private final int PICK_IMAGE_REQUEST = 71;
    private final int PICK_IMAGE_REQUEST2 = 72;
    private static int IMG_RESULT = 1;
    String key;
    private Uri filePath, filePath2;
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        rootRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        edt_img_foto_profil = (ImageView) findViewById(R.id.edt_img_foto_profil);
        edt_img_foto_tambal = (ImageView) findViewById(R.id.edt_img_foto_tambal);
        btn_edt_foto_profil = (Button) findViewById(R.id.btn_edt_foto_profil);
        btn_edt_foto_tambal = (Button) findViewById(R.id.btn_edt_foto_tambal);
        btn_simpan = (Button) findViewById(R.id.btn_simpan);
        edt_nama = (EditText) findViewById(R.id.edt_nama);
        edt_nama_tambal = (EditText) findViewById(R.id.edt_nama_tambal);
        edt_nomor_hp = (EditText) findViewById(R.id.edt_nomor_hp);
        edt_email = (EditText) findViewById(R.id.edt_email);

        Intent i = this.getIntent();

        key = i.getExtras().getString("KEY");
        nama = i.getExtras().getString("nama");
        namaTambal = i.getExtras().getString("namaTambal");
        nomorHp = i.getExtras().getString("nomorHp");
        email = i.getExtras().getString("email");
        password = i.getExtras().getString("password");
        urlfotoprofil = i.getExtras().getString("fotoProfil");
        urlfototambal = i.getExtras().getString("fotoTambal");
        lat = i.getExtras().getDouble("lat");
        lng = i.getExtras().getDouble("lang");
        buka = i.getExtras().getString("buka");
        tutup = i.getExtras().getString("tutup");
        status = i.getExtras().getString("status");

        edt_nama.setText(nama);
        edt_nama_tambal.setText(namaTambal);
        edt_nomor_hp.setText(nomorHp);
        edt_email.setText(email);

        Glide.with(this).load(urlfotoprofil).into(edt_img_foto_profil);
        Glide.with(this).load(urlfototambal).into(edt_img_foto_tambal);

        rootRef = FirebaseDatabase.getInstance().getReference();
        ref = rootRef.child("Profil").child(key);
       // key = ref.getKey();

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_simpan.setClickable(false);
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

        //Membuat listener untuk btnFotoTambal
        btn_edt_foto_tambal.setOnClickListener(new View.OnClickListener() {
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
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_simpan.setClickable(false);

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
        final Profil profil = new Profil(edt_nama.getText().toString(),
                edt_nama_tambal.getText().toString(), edt_nomor_hp.getText().toString(), edt_email.getText().toString(),
                password, status, password, urlfotoprofil, urlfototambal, lat, lng, buka, tutup);

        ref.setValue(profil, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null){
                    Toast.makeText(EditProfilActivity.this, "Gagal Menambah Data : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(EditProfilActivity.this, "Berhasil Menambah Data", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditProfilActivity.this, ProfilActivity.class);
                    intent.putExtra("KEY", key);
                    intent.putExtra("nama", edt_nama.getText().toString());
                    intent.putExtra("namaTambal", edt_nama_tambal.getText().toString());
                    intent.putExtra("nomorHp", edt_nomor_hp.getText().toString());
                    intent.putExtra("email", edt_email.getText().toString());
                    intent.putExtra("fotoProfil", urlfotoprofil);
                    intent.putExtra("fotoTambal", urlfototambal);
                    intent.putExtra("buka", buka);
                    intent.putExtra("tutup", tutup);
                    intent.putExtra("lat", lat);
                    intent.putExtra("lang", lng);
                    intent.putExtra("status", status);

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
                    Toast.makeText(EditProfilActivity.this, "Gagal Upload Foto "+exception.getMessage(), Toast.LENGTH_SHORT).show();
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
            StorageReference ref = mStorageRef.child("Tambah"+filePath2.getLastPathSegment());
            UploadTask uploadTask = ref.putFile(filePath2);

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toast.makeText(EditProfilActivity.this, "Gagal Upload Foto "+exception.getMessage(), Toast.LENGTH_SHORT).show();
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
                    edt_img_foto_profil.setImageBitmap(bitmap);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }else {
                filePath2 = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath2);
                    edt_img_foto_tambal.setImageBitmap(bitmap);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

        }
    }


}
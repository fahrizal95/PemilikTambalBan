package com.example.fahrizal.pemiliktambalban.model;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    Context context;
    SharedPreferences sharedPreferences;

    public SharedPref(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("TRANSMAPS", Context.MODE_PRIVATE);
    }

    public void setAuth(String nama) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("NAMA", nama);
        editor.commit();
    }

    public String getAuth(){
        return sharedPreferences.getString("NAMA", "");
    }

    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}

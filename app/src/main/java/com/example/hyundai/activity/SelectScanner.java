package com.example.hyundai.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.hyundai.R;
import com.google.android.material.snackbar.Snackbar;

public class SelectScanner extends AppCompatActivity {

    private TextView mNama;
    ImageButton logout;
    CardView card1, card2;

    SharedPreferences mSharedPreferences;
    private final static String APP_NAME= "POKAYOKE";
    private final static String NPK = "npk";
    private final static String NAMA = "name";
    private final static String ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_scanner);
        mSharedPreferences = getSharedPreferences(APP_NAME, MODE_PRIVATE);

        logout = findViewById(R.id.btnlogout);
        mNama = findViewById(R.id.nama_user);
        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);

        Intent intent2 = getIntent();
        String  Gnama = intent2.getStringExtra(NAMA);

        mNama.setText(Gnama);

        logout.setOnClickListener(view ->{
            SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(SelectScanner.this, Login.class);
            startActivity(intent);
            finish();
        });

        card1.setOnClickListener(view -> {
            Intent intent = new Intent(SelectScanner.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(SelectScanner.this, "asal", Toast.LENGTH_SHORT).show();
        });

        card2.setOnClickListener(view -> {
            Intent intent = new Intent(SelectScanner.this, MainActivity.class);
            startActivity(intent);
        });
    }

    public void onBackPressed() {    }

}


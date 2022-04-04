package com.example.hyundai.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import android.util.Log;
import android.widget.TextView;

import com.example.hyundai.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import javax.microedition.khronos.opengles.GL;

public class Leader extends AppCompatActivity {

    private TextView nama_user;

    private final static String NPK = "npk";
    private final static String NAMA = "name";
    private final static String TRIAL = "trial";

    SharedPreferences mSharedPreferences;
    private final static String APP_NAME= "Hyundai";
    private final static String LOCKED = "0";

    FloatingActionButton fab, fab1,fab2, fab3;
    Animation openAnim, closeAnim, toBottom, fromBottom;
    TextView txtuser, txtproduct, txtriwayat;
    boolean isOpen=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader);
        Intent Gintent = getIntent();
        nama_user = findViewById(R.id.nama_user);
        String  Gnama = Gintent.getStringExtra(NAMA);
        String GNPK = Gintent.getStringExtra(NPK);
        String GTRIAL = Gintent.getStringExtra(TRIAL);

        mSharedPreferences = getSharedPreferences(APP_NAME, MODE_PRIVATE);
        String GLOCKED = mSharedPreferences.getString(LOCKED, "0");
        Log.e("GLOCKED", "onCreate: " + GLOCKED );

        nama_user.setText(Gnama);

        if(GLOCKED.equals("1")) {
            showNotifLocked();
        }

        ImageButton logout = findViewById(R.id.btnlogout);
        logout.setOnClickListener(view ->{
            Intent intent = new Intent(Leader.this, Login.class);
            startActivity(intent);
            finish();
        });

        CardView card1 = findViewById(R.id.card1);
        card1.setOnClickListener(view -> {
            Intent intent = new Intent(Leader.this, MainActivity.class);
            intent.putExtra(NPK, GNPK);
            intent.putExtra(TRIAL, GTRIAL);
            startActivity(intent);
        });

        CardView card2 = findViewById(R.id.card2);
        card2.setOnClickListener(view -> {
            Intent intent = new Intent(Leader.this, MainActivity.class);
            intent.putExtra(NPK, GNPK);
            intent.putExtra(TRIAL, GTRIAL);
            startActivity(intent);
        });



        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab1 = (FloatingActionButton) findViewById(R.id.floatingActionButton1);
        fab2 = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
        fab3 = (FloatingActionButton) findViewById(R.id.floatingActionButton3);

        txtuser = findViewById(R.id.txtUser);
        txtproduct = findViewById(R.id.product);
        txtriwayat = findViewById(R.id.riwayat);

        openAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_open);
        closeAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_close);

        toBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.to_botoom);

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                animationFab();
            }
        });

        fab1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                animationFab();
                Intent intent = new Intent(Leader.this,list_user.class);
                startActivity(intent);
            }
        });

        fab2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                animationFab();
                Intent intent = new Intent(Leader.this,list_product.class);
                startActivity(intent);

            }
        });

        fab3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                animationFab();
                Intent intent = new Intent(Leader.this,list_riwayat.class);
                startActivity(intent);

            }
        });

    }


    private void showNotifLocked() {
        notif_locked cdd = new notif_locked(Leader.this);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();
        cdd.setCanceledOnTouchOutside(false);
    }

    private void animationFab(){
        if(isOpen){
            fab.startAnimation(fromBottom);
            fab1.startAnimation(closeAnim); fab1.setClickable(false);
            fab2.startAnimation(closeAnim); fab2.setClickable(false);
            fab3.startAnimation(closeAnim); fab3.setClickable(false);

            txtuser.startAnimation(closeAnim);
            txtproduct.startAnimation(closeAnim);
            txtriwayat.startAnimation(closeAnim);
            isOpen=false;
        }
        else{
            fab.startAnimation(toBottom);
            fab1.startAnimation(openAnim); fab1.setClickable(true);
            fab2.startAnimation(openAnim); fab2.setClickable(true);
            fab3.startAnimation(openAnim); fab3.setClickable(true);

            txtuser.startAnimation(openAnim);
            txtproduct.startAnimation(openAnim);
            txtriwayat.startAnimation(openAnim);
            isOpen=true;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Leader.this, Login.class);
        startActivity(intent);
        finish();
    }
}
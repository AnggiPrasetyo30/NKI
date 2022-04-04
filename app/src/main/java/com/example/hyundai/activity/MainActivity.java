package com.example.hyundai.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hyundai.R;
import com.example.hyundai.SQLite.shopping;
import com.example.hyundai.Scanner.DWUtilities;
import com.example.hyundai.SQLite.DatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;

import javax.microedition.khronos.opengles.GL;

public class MainActivity extends AppCompatActivity {


    DatabaseHelper mDatabaseHelper;
    TextView output;

    //untuk menyimpan alasan NG
    public static String alasan_NG;

    //variabel yang akan diinput kedalam tabel result
    String pn_api, pn_cust, customer;
    String data_api = null;
    String data_cust = null;

    //untuk menghitung sudah berapa kali scan
    int hasilScan;

    //untuk menampung value dari intent
    private String GNPK;
    private String GTRIAL;
    private String GLOCK;


    //nama string yang dilempar pada intent
    private final static String NPK = "npk";
    private final static String TRIAL = "trial";
    private final static String LOCKED = "0";


    //menyimpan sound
    public static MediaPlayer mp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DWUtilities.CreateDWProfile(this);
        mDatabaseHelper = new DatabaseHelper(this);

        Intent intent2 = getIntent();
        GNPK = intent2.getStringExtra(NPK);
        GTRIAL = intent2.getStringExtra(TRIAL);

        hasilScan = 0;

        output = findViewById(R.id.kodekanban);
        Button btnReset = findViewById(R.id.btnReset);

        mp = MediaPlayer.create(this, R.raw.sound_ng);

        btnReset.setOnClickListener(view -> {
            output.setText(R.string.kode_kanban);
            hasilScan = 0;
            pn_cust = null;
            pn_api = null;
        });

    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String a = displayScanResult(intent);
        String npk = GNPK;
        String trial = GTRIAL;
        GLOCK = intent.getStringExtra(LOCKED);

        //Cek Parsing API dan Customer
        if (a.length() == 32) { //Kanban Hyundai
            data_cust = a;
            String kanban = GetSubstrCustBack(a);
            output.setText(kanban);
            pn_cust = kanban;
            hasilScan++;
        } else if (a.length() == 29) { //Kanban Hyundai
            data_cust = a;
            String kanban = GetSubstrCustFront(a);
            output.setText(kanban);
            pn_cust = kanban;
            hasilScan++;
        }
        else if (a.length() > 35) { //Kanban DENSO
            data_cust = a;
            String kanban = GetSringDENSO(a);
            output.setText(kanban);
            pn_cust = kanban;
            hasilScan++;
        }
        else {
            data_api = a;
            String kanban = GetSubstrApi(a);
            if (mDatabaseHelper.CekKanbanAPI(kanban) != null) {
                pn_api = mDatabaseHelper.CekKanbanAPI(kanban);
                customer = mDatabaseHelper.CekCustomer(kanban);
                output.setText(pn_api);
                hasilScan++;
            } else {
                alasan_NG = "Kanban tidak ditemukan";
                mp.start();
                showNotifNotGood(); // alasan : data tidak ditemukan
            }
        }


        //Cek Kanban API dan Customer
        if(pn_api!=null&&pn_cust!=null) {
            if (!mDatabaseHelper.cekOnResult(data_api) && !mDatabaseHelper.cekOnResultC(data_cust)) {

                    mDatabaseHelper.InsertResult(new shopping(npk, customer, pn_api, pn_cust,
                        "Double", String.valueOf(Calendar.getInstance().getTime()), data_api, data_cust, trial));
                    intent.putExtra(GLOCK, "1");
                    alasan_NG = "Data kanban dobel";
                    mp.start();
                    showNotifNotGood();

                    Log.e("LOCKED", "onCreate: " + GLOCK );
                    pn_api=null;
                    hasilScan = 0;
            } else if (hasilScan > 1) {
                if (pn_cust.contains(pn_api)) {
                    showNotifGood();
                    mDatabaseHelper.InsertResult(new shopping(npk, customer, pn_api, pn_cust,
                            "OK", String.valueOf(Calendar.getInstance().getTime()), data_api, data_cust, trial));
                    pn_api=null;
//                    intent.putExtra(LOCKED, 0);
                    hasilScan = 0;
                    output.setText(R.string.kode_kanban);
                } else {
                    mDatabaseHelper.InsertResult(new shopping(npk, customer, pn_api, pn_cust,
                            "NG", String.valueOf(Calendar.getInstance().getTime()), data_api, data_cust, trial));
                    alasan_NG = "Kanban tidak cocok";
                    pn_api=null;

                    mp.start();

                    intent.putExtra(GLOCK, "1");

                    showNotifNotGood();
                    Log.e("LOCKED", "onCreate: " + GLOCK );
                    hasilScan = 0;
                }
            }
        }
    }

    private String displayScanResult(Intent scanIntent) {
        String decodedData = scanIntent.getStringExtra(getResources()
                .getString(R.string.datawedge_intent_key_data));
        return decodedData;
    }

    private void showNotifNotGood() {
        notif_ng cdd = new notif_ng(MainActivity.this);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();
        cdd.setCanceledOnTouchOutside(false);
    }

    private void showNotifGood() {
        notif_good cdd = new notif_good(MainActivity.this);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();
        cdd.setCanceledOnTouchOutside(false);
    }

    private String GetSubstrCustBack(String kanban) {
        return kanban.substring(4, 17);
    }

    private String GetSubstrCustFront(String kanban) {
        return kanban.substring(4, 14);
    }

    private String GetSubstrApi(String kanban) {
        return kanban.substring(8, 25);
    }

    private String GetSringDENSO(String kanban) {
        return kanban.substring(0,13);
    }



}
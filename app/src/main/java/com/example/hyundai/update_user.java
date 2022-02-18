package com.example.hyundai;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hyundai.SQLite.DatabaseHelper;

import java.util.Calendar;

public class update_user extends AppCompatActivity {
    protected Cursor cursor;
    DatabaseHelper mDatabaseHelper;
    EditText NPK, Username, Password, RFIDTag, Nama, OpSkill, Status;
    Spinner Usergroup, Department;
    Button Simpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        mDatabaseHelper = new DatabaseHelper(this);
        NPK = (EditText) findViewById(R.id.fieldNpk);
        Username = (EditText) findViewById(R.id.fieldUsername);
        Password = (EditText) findViewById(R.id.fieldPassword);
        RFIDTag = (EditText) findViewById(R.id.fieldRFID);
        Nama = (EditText) findViewById(R.id.fieldNama);
        OpSkill = (EditText) findViewById(R.id.fieldOpSkill);
        Usergroup = (Spinner) findViewById(R.id.fieldUsergroup);
        Department = (Spinner) findViewById(R.id.fieldDepartement);
        Status = (EditText) findViewById(R.id.fieldStatus);
        Simpan = (Button) findViewById(R.id.btnBuat);

        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM user WHERE npk = '" +
                getIntent().getStringExtra("NPK") + "'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            NPK.setText(cursor.getString(1).toString());
            Username.setText(cursor.getString(2).toString());
            Password.setText(cursor.getString(3).toString());
            RFIDTag.setText(cursor.getBlob(4).toString());
            Nama.setText(cursor.getString(5).toString());
            OpSkill.setText(cursor.getString(8).toString());
            Usergroup.setSelection(cursor.getInt(6));
            Department.setSelection(cursor.getInt(7));
            Status.setText(cursor.getString(10).toString());
        }
        final Spinner ListUserGroup = Usergroup;
        final Spinner ListDepartement = Department;
        Simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
                db.execSQL("update user set username='" +
                        Username.getText().toString() + "', password='" +
                        Password.getText().toString() + "', rfid_tag='" +
                        RFIDTag.getText().toString() + "', name='" +
                        Nama.getText().toString() + "', usergroup='" +
                        ListUserGroup.getSelectedItem().toString() + "', departement='" +
                        ListDepartement.getSelectedItem().toString() + "', op_skill='" +
                        OpSkill.getText().toString() + "', last_login='" +
                        " " + "', status='" + Status.getText().toString() + "', created_at='" +
                        " " + "', updated_at='" + String.valueOf(Calendar.getInstance().getTime()) + "', TRIAL857='" +
                        "T" + "', status_akun='" + "0" + "' where npk='" + NPK.getText().toString() + "'");
                Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
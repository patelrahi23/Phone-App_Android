package com.rahi.call_log_reader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    List<String> allcontacts;
    ArrayAdapter<String> adapter;
    FloatingActionButton fab;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        inIt();

        readContacts();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String contact=allcontacts.get(i);
                String[] dd=contact.split("\n");
                showDia(dd[0],dd[1]);
                Toast.makeText(getApplicationContext(),"Click : "+dd[1],Toast.LENGTH_LONG).show();
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });
    }


    void showDia(final String nm,final String no)
    {

        final Dialog d=new Dialog(MainActivity.this);
        d.setContentView(R.layout.popup_callsms);

        //d.setCancelable(false);

        TextView name=d.findViewById(R.id.name);
        TextView number=d.findViewById(R.id.number);
        RelativeLayout relcall=d.findViewById(R.id.relcall);
        RelativeLayout relsms=d.findViewById(R.id.relsms);

        name.setText(nm);
        number.setText(no);

        relcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeCall(no);
            }
        });
        relsms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                d.dismiss();
            }
        });

        d.show();
    }

    void inIt()
    {
        lv=findViewById(R.id.lv);
        allcontacts=new ArrayList<>();
        adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,allcontacts);
        lv.setAdapter(adapter);

        fab= findViewById(R.id.fab);
    }
    void readContacts()
    {
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            String names=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            allcontacts.add(names+"\n"+phoneNumber);

        }
        adapter.notifyDataSetChanged();
    }
    void makeCall(String number) {
        String callstring = "tel:" + number;
        Intent i = new Intent(Intent.ACTION_CALL, Uri.parse(callstring));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(getApplicationContext(),"Give Permission..",Toast.LENGTH_LONG).show();

            return;
        }
        startActivity(i);


    }

}


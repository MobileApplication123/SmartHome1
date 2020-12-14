package com.example.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import id.co.telkom.iot.AntaresHTTPAPI;
import id.co.telkom.iot.AntaresResponse;

public class window extends AppCompatActivity implements AntaresHTTPAPI.OnResponseListener{


    private Button btnOn;
    private Button btnOff;

    private TextView txtData;
    private String TAG = "c512e91e464f9119:2b31c59a19d78a99";
    private AntaresHTTPAPI antaresAPIHTTP;
    private String dataDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Smart Window");
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // --- Inisialisasi UI yang digunakan di aplikasi --- //
        btnOff = (Button) findViewById(R.id.btnOff);
        btnOn = (Button) findViewById(R.id.btnOn);

        txtData = (TextView) findViewById(R.id.txtData);

        // --- Inisialisasi API Antares --- //
        //antaresAPIHTTP = AntaresHTTPAPI.getInstance();
        antaresAPIHTTP = new AntaresHTTPAPI();
        antaresAPIHTTP.addListener(this);

        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                antaresAPIHTTP.storeDataofDevice(1,"c512e91e464f9119:2b31c59a19d78a99", "androidantares", "Windows", "{\\\"status\\\":\\\"1\\\"}");
                Toast.makeText(getApplicationContext(),"Window is Open",Toast.LENGTH_SHORT).show();
            }
        });

        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                antaresAPIHTTP.storeDataofDevice(1,"c512e91e464f9119:2b31c59a19d78a99", "androidantares", "Windows", "{\\\"status\\\":\\\"0\\\"}");
                Toast.makeText(getApplicationContext(),"Window is Closed",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.btnRefresh:
                antaresAPIHTTP.getLatestDataofDevice("c512e91e464f9119:2b31c59a19d78a99","androidantares","Windows");
                break;

        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onResponse(AntaresResponse antaresResponse) {
        // --- Cetak hasil yang didapat dari ANTARES ke System Log --- //
        //Log.d(TAG,antaresResponse.toString());
        Log.d(TAG,Integer.toString(antaresResponse.getRequestCode()));
        if(antaresResponse.getRequestCode()==0){
            try {
                JSONObject body = new JSONObject(antaresResponse.getBody());
                dataDevice = body.getJSONObject("m2m:cin").getString("con");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtData.setText(dataDevice);
                    }
                });
                Log.d(TAG,dataDevice);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
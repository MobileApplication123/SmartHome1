package com.example.smarthome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import id.co.telkom.iot.AntaresHTTPAPI;
import id.co.telkom.iot.AntaresResponse;


public class MainActivity extends AppCompatActivity implements AntaresHTTPAPI.OnResponseListener {

    TextView tcase;
    TextView thealed;
    TextView tdead;
    View youtube;
    TextView addressView;
    TextView dateToday;
    GridLayout mainGrid;
    CardView hospital;
    CardView symptoms;
    CardView tracker;
    String UserID;
    String qAddress;
    boolean moved = false;
    String sessionId = null;
    private TextView txtData;
    private String TAG = "d3d770bbc012ee6c:8bdedd264a080fa0";
    private AntaresHTTPAPI antaresAPIHTTP;
    private String dataDevice;
    private ImageView refresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        tcase = findViewById(R.id.totalCase);
        thealed = findViewById(R.id.totalHealed);
        tdead = findViewById(R.id.totalDeath);
        hospital = findViewById(R.id.hospitalNearby);
        symptoms = findViewById(R.id.checkSymptoms);
        dateToday = findViewById(R.id.Date);
        tracker = findViewById(R.id.tracker);
        youtube = findViewById(R.id.video);
        addressView = findViewById(R.id.setAddress);
        mainGrid = findViewById(R.id.gridLayout);
        txtData = (TextView) findViewById(R.id.textView3);
        refresh = findViewById(R.id.refresh);


        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        dateToday.setText(date);

        sessionId = getIntent().getStringExtra("result");
        // --- Inisialisasi API Antares --- //
        //antaresAPIHTTP = AntaresHTTPAPI.getInstance();
        antaresAPIHTTP = new AntaresHTTPAPI();
        antaresAPIHTTP.addListener(this);
        antaresAPIHTTP.getLatestDataofDevice("d3d770bbc012ee6c:8bdedd264a080fa0", "Tubesmobcomm", "SmartLamp");


        if (qAddress != null) {
            addressView.setText(qAddress);
            //Log.d("what1",qAddress);
        }

        hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, lamp.class));
            }
        });

        symptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ac.class));
            }
        });

        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, fan.class));
            }
        });

        tracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, window.class);
                startActivity(intent);
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                antaresAPIHTTP.getLatestDataofDevice("d3d770bbc012ee6c:8bdedd264a080fa0","Tubesmobcomm","SmartLamp");
            }
        });


    }






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.refresh:
                antaresAPIHTTP.getLatestDataofDevice("d3d770bbc012ee6c:8bdedd264a080fa0","Tubesmobcomm","SmartLamp");
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







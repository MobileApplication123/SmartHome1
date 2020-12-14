package com.example.smarthome;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import id.co.telkom.iot.AntaresHTTPAPI;
import id.co.telkom.iot.AntaresResponse;

public class ac extends AppCompatActivity implements AntaresHTTPAPI.OnResponseListener{

    private Button btnOn;
    private Button btnOff;

    private TextView txtData;
    private String TAG = "c512e91e464f9119:2b31c59a19d78a99";
    private AntaresHTTPAPI antaresAPIHTTP;
    private String dataDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Air Conditioner");
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
        antaresAPIHTTP.getLatestDataofDevice("c512e91e464f9119:2b31c59a19d78a99","androidantares","AirConditioner");


        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                antaresAPIHTTP.storeDataofDevice(1,"c512e91e464f9119:2b31c59a19d78a99", "androidantares", "AirConditioner", "{\\\"Status\\\":1}");
                Toast.makeText(getApplicationContext(),"AC is On",Toast.LENGTH_SHORT).show();
                txtData.setText("On");
            }
        });

        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                antaresAPIHTTP.storeDataofDevice(1,"c512e91e464f9119:2b31c59a19d78a99", "androidantares", "AirConditioner", "{\\\"Status\\\":0}");
                Toast.makeText(getApplicationContext(),"AC is Off",Toast.LENGTH_SHORT).show();
                txtData.setText("Off");
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
                antaresAPIHTTP.getLatestDataofDevice("c512e91e464f9119:2b31c59a19d78a99","androidantares","AirConditioner");
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
                String numberOnly = dataDevice.replaceAll("[^0-9]", "");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (numberOnly.contains("0")){
                            txtData.setText("Off");
                        } else if (numberOnly.contains("1")){
                            txtData.setText("On");
                        }
                    }
                });
                Log.d(TAG,dataDevice);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
package com.twotr.twotr.globalpackfiles;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fujiyuu75.sequent.Sequent;
import com.twotr.twotr.R;
import com.twotr.twotr.guestfiles.GuestControlBoard;


public class MainActivity extends AbsRuntimePermission {
RelativeLayout layout;
TextView signupbut;
Button Bsignin,Bguest;
    private static final int REQUEST_PERMISSION = 10;

    Context context;
    boolean GpsStatus ;
    TextView textview;
    LocationManager locationManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        layout =  findViewById(R.id.rel_layout);
       Sequent.origin(layout).anim(getApplicationContext(), com.fujiyuu75.sequent.Animation.FADE_IN_UP).start();
        signupbut=findViewById(R.id.sign_up);
        Bsignin=findViewById(R.id.main_signin);
        Bguest=findViewById(R.id.main_guest);
        requestAppPermissions(new String[]{
                        Manifest.permission.READ_SMS,

                        Manifest.permission.ACCESS_COARSE_LOCATION,

                        Manifest.permission.READ_EXTERNAL_STORAGE,

                        Manifest.permission.CAMERA,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,

                },

                R.string.msg,REQUEST_PERMISSION);
        Bsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
CheckEnableGPS();

            }
        });
        signupbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SignupActivity.class));
            }
        });
Bguest.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(MainActivity.this, GuestControlBoard.class));

    }
});




    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    private void CheckEnableGPS() {
        String provider = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!provider.equals(""))
        {
            checkPermission();
            startActivity(new Intent(MainActivity.this, SigninActivity.class));

        } else {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }

    private void checkPermission(){
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);

        if (result == PackageManager.PERMISSION_GRANTED){

        }
        else {

        }
    }






}

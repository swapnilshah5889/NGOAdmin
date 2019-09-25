package com.example.ngoadmin.GeneralFunctions;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class RequestPermisions extends AppCompatActivity {

    String permission;
    int MY_PERMISSIONS_REQUEST;
    Activity activity;

    public RequestPermisions(String permission, int MY_PERMISSIONS_REQUEST, Activity activity) {
        this.permission = permission;
        this.MY_PERMISSIONS_REQUEST = MY_PERMISSIONS_REQUEST;
        this.activity = activity;
    }

    public boolean checkPermission() {

        if (ContextCompat.checkSelfPermission(activity, permission)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }

        else
            return true;
    }

    public void getPermission(){

        if (ContextCompat.checkSelfPermission(activity,
                permission)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity,
                        permission)
                        != PackageManager.PERMISSION_DENIED) {

            // Permission is not granted

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(activity,
                        new String[]{permission},
                        MY_PERMISSIONS_REQUEST);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.

        } else {
            // Permission has already been granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 110: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                }else if(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_DENIED){
                    getPermission();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

}

package com.kandktech.ezivizi;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class QR_Scan extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;
    private TextView txt_view;
    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 0;
    String checked = "";

    SharedPreferences permissionPref ;
    private DbHandler myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);


//        try{
//            SharedPreferences sharedpreferences = getSharedPreferences("time", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedpreferences.edit();
//            editor.putLong("ExpiredDate", System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(60));
//            editor.apply();
//        }catch (Exception e){
//            e.printStackTrace();
//        }



        mScannerView = new ZXingScannerView(this);
        Toast.makeText(this, "Scan table code", Toast.LENGTH_SHORT).show();
//        txt_view = (TextView)findViewById(R.id.txt_view);
        // Set the scanner view as the content view
        setContentView(mScannerView);
        try {

            checked = permissionPref.getString("allowed","true");
            System.out.println("Checked : "+checked);
            if (checked.equals("false")){
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_STORAGE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA)) {


                } else {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_STORAGE);
                }
            }


    }

    @Override
    public void onResume() {
        super.onResume();

        // Register ourselves as a handler for scan results.
        mScannerView.setResultHandler(this);
        // Start camera on resume
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void onBackPressed() {
        finish();

    }

    @Override
    public void handleResult(Result result) {


        Log.v("scanResult : ", result.getText());
//        Toast.makeText(this, "ScanResult : "+result.getText() + " + "+ result.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();
        // Prints the scan format (qrcode, pdf417 etc.)
        Log.v("scanResult : ", result.getBarcodeFormat().toString());

//        String data = result.getText();

//        System.out.println("qrcodeData : "+result.getText().toString());

//        String[] separated = data.split("/");

        //If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);


        Intent intent = new Intent(getApplicationContext(), ScannedDataActivity.class);
        intent.putExtra("qrData",result.getText());
        startActivity(intent);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    try {

                        SharedPreferences.Editor editor = permissionPref.edit();
                        editor.putString("allowed","true");
                        editor.apply();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                } else {

                    try {

                        SharedPreferences.Editor editor = permissionPref.edit();
                        editor.putString("allowed","false");
                        editor.apply();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    AlertDialog alertDialog = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT).create();
//
                alertDialog.setTitle(R.string.app_name);
                alertDialog.setMessage("You denied the camera permission!! Without permission you are not able to scan the table and order food");

                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Leave",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                dialog.dismiss();
                            }
                        });

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Retry",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                dialog.dismiss();

                                ActivityCompat.requestPermissions(QR_Scan.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        MY_PERMISSIONS_REQUEST_STORAGE);
                            }
                        });

                alertDialog.show();
                final Button neutralButton = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                final Button positveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                neutralButton.setTextColor(getResources().getColor(R.color.black));
                positveButton.setTextColor(getResources().getColor(R.color.black));
//                    AlertDialog alertDialog = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT).create();
//
//
//                    alertDialog.setTitle(R.string.app_name);
//                    alertDialog.setMessage("You denied the camera permission!!");
//
//
//                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    finish();
//                                    dialog.dismiss();
//                                }
//                            });
//
//                    alertDialog.show();
//                    final Button neutralButton = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
//                    LinearLayout.LayoutParams neutralButtonLL = (LinearLayout.LayoutParams) neutralButton.getLayoutParams();
//                    neutralButtonLL.gravity = Gravity.CENTER;
//                    neutralButton.setTextColor(getResources().getColor(R.color.black));
//                    neutralButton.setLayoutParams(neutralButtonLL);
//                    Toast.makeText(this, "You denied the camera permission!!", Toast.LENGTH_SHORT).show();

                }
                return;
            }

        }
    }


}

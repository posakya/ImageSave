package com.kandktech.ezivizi.fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.Result;
import com.kandktech.ezivizi.DbHandler;
import com.kandktech.ezivizi.FirstPageActivity;
import com.kandktech.ezivizi.R;
import com.kandktech.ezivizi.image_saver.ImageSaver;
import com.kandktech.ezivizi.model_class.UserModelClass;
import com.kandktech.ezivizi.progressDialog.ProgressDialogBox;
import com.kandktech.ezivizi.retrofit_api_client.RetrofitClient;
import com.kandktech.ezivizi.retrofit_api_interface.ApiInterface;
import com.scottyab.aescrypt.AESCrypt;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class QRScanFragment extends Fragment implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;
    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 0;
    String checked = "";
    SharedPreferences permissionPref ;
    DbHandler dbHandler;
    String[] separated;
    ProgressDialogBox progressDialogBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mScannerView = new ZXingScannerView(getActivity());

        try {
            permissionPref = getActivity().getSharedPreferences("permission",Context.MODE_PRIVATE);
            dbHandler = new DbHandler(getActivity());
            checked = permissionPref.getString("allowed","true");
            System.out.println("Checked : "+checked);
            if (checked.equals("false")){
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_STORAGE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)) {


            } else {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_STORAGE);
            }
        }

        progressDialogBox = new ProgressDialogBox(getActivity());


        return mScannerView;
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
    public void handleResult(Result result) {

        String data = result.getText();

        String password = String.valueOf(R.string.app_name);
        String decryptedMsg = "";
        try {
            decryptedMsg = AESCrypt.decrypt(password, data);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        separated = decryptedMsg.split("EZVZ");

        System.out.println("Data : "+separated[8]);

        getUserDetails(separated[8]);


        mScannerView.resumeCameraPreview(this);


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

                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_LIGHT).create();
                    alertDialog.setTitle(R.string.app_name);
                    alertDialog.setMessage("You denied the camera permission!! Without permission you are not able to scan the table and order food");

                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Leave",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                }
                            });

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Retry",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();

                                    if (getActivity() != null){
                                        ActivityCompat.requestPermissions(getActivity(),
                                                new String[]{Manifest.permission.CAMERA},
                                                MY_PERMISSIONS_REQUEST_STORAGE);
                                    }

                                }
                            });

                    alertDialog.show();
                    final Button neutralButton = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                    final Button positveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    neutralButton.setTextColor(getResources().getColor(R.color.black));
                    positveButton.setTextColor(getResources().getColor(R.color.black));

                }

                return;
            }

        }
    }

    public void getUserDetails(final String deviceId){

        try {
            if (progressDialogBox == null){

                progressDialogBox.showProgress();
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        ApiInterface userInterface = RetrofitClient.getFormData().create(ApiInterface.class);

        Call<UserModelClass> userModelClassCall = userInterface.userDetails(deviceId);

        userModelClassCall.enqueue(new Callback<UserModelClass>() {
            @Override
            public void onResponse(Call<UserModelClass> call, final Response<UserModelClass> response) {
                if (response.isSuccessful()){
                    if (response.body() != null) {
                        new Thread(new Runnable(){
                            @Override
                            public void run() {
                                Bitmap bm = null;
                                URL url;
                                try {
                                    url = new URL(RetrofitClient.imageUrl+response.body().getUser_detail().getImage());
                                    bm = BitmapFactory.decodeStream(url.openStream());
                                } catch(Exception e) {
                                    e.printStackTrace();
                                }

                                new ImageSaver(getActivity()).
                                        setFileName(""+response.body().getUser_detail().getImage().replaceAll(RetrofitClient.imageUrl,"")).
                                        setDirectoryName(".ezvz").
                                        setExternal(true).
                                        save(bm);

                                if (bm != null){

                                    dbHandler.deleteDataSingle(deviceId);
                                    dbHandler.insertData(separated[0],separated[1],separated[2],separated[4],separated[3],separated[6],separated[8],"/storage/emulated/0/Pictures/.ezvz/"+response.body().getUser_detail().getImage().replaceAll(RetrofitClient.imageUrl,""),separated[7],separated[9]);

                                    try {
                                        if (progressDialogBox != null){
                                            progressDialogBox.hideProgress();
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                    Intent intent = new Intent(getActivity(), FirstPageActivity.class);
                                    startActivity(intent);
                                }

                            }
                        }).start();

                    }else{
                        try {
                            if (progressDialogBox != null){
                                progressDialogBox.hideProgress();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        Toast.makeText(getActivity(), "Failed to get image..", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModelClass> call, Throwable t) {

            }
        });

    }

//    private void saveBitMap(Bitmap drawView){
//
//        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), ".ezivizi");
//
//        if (!pictureFileDir.exists()) {
//            boolean isDirectoryCreated = pictureFileDir.mkdirs();
//            if(!isDirectoryCreated)
//                Log.i("TAG", "Can't create directory to save the image");
//            return;
//        }
//
//        String filename = pictureFileDir.getPath() +File.separator+System.currentTimeMillis()+".jpg";
//
//        File pictureFile = new File(filename);
//
//        try {
//            pictureFile.createNewFile();
//            FileOutputStream oStream = new FileOutputStream(pictureFile);
//            drawView.compress(Bitmap.CompressFormat.PNG, 100, oStream);
//            System.out.println("FileName : "+filename);
//            oStream.flush();
//            oStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.i("TAG", "There was an issue saving the image.");
//        }
//
//    }
}

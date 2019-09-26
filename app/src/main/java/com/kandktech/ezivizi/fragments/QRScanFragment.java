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

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.Result;
import com.kandktech.ezivizi.DbHandler;
import com.kandktech.ezivizi.MainActivity;
import com.kandktech.ezivizi.R;
import com.kandktech.ezivizi.authentication.SharedPreferenceClass;
import com.kandktech.ezivizi.corporate.IndividualActivity;
import com.kandktech.ezivizi.image_saver.ImageSaver;
import com.kandktech.ezivizi.model_class.user_model.UserModelClass;
import com.kandktech.ezivizi.model_class.user_model.Data;
import com.kandktech.ezivizi.model_class.service_model.ServiceModelClass;
import com.kandktech.ezivizi.progressDialog.ShowProgress;
import com.kandktech.ezivizi.retrofit_api_client.RetrofitClient;
import com.kandktech.ezivizi.retrofit_api_interface.ApiInterface;
import com.scottyab.aescrypt.AESCrypt;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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
    ShowProgress showProgress;
    SharedPreferenceClass sharedPreferenceClass;
    String image = "http://192.168.43.94:82/image/1566453815image_f77623cb-5440-43ed-9f18-3e1d36a606f920181128_211916.jpg";



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

        sharedPreferenceClass = new SharedPreferenceClass(getActivity());


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

        showProgress = new ShowProgress(getActivity());


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

        String password = "EzVz";
        String decryptedMsg = null;
        try {
            decryptedMsg = AESCrypt.decrypt(password, data);

            System.out.println("Decrypted : "+decryptedMsg);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        separated = decryptedMsg.split("EZVZ");


        System.out.println("Data : "+separated[8]);

        System.out.println("Separated : "+ Arrays.toString(separated));

        /*
        userName + "EZVZ" + address + "EZVZ" + phone + "EZVZ" + email + "EZVZ" + website + "EZVZ" + filename1 + "EZVZ" + position + "EZVZ" + ColorCode + "EZVZ" + userId + "EZVZ" + usedLayout + "EZVZ" + company + "EZVZ" + fax_no + "EZVZ" + po_box_no + "EZVZ" + colorCodeSecond;
[corporate, Mahalaxmi, Tikathali, Lalitpur, Nepal, 9863859745, Halfwaiter.system@gmail.com, www.halfwaiter.com, /storage/emulated/0/Pictures/.ezvz/1566354605IMG_20190704_191711_452.jpg, position, -14861313, 106448657719359170470, 5, Half Waiter Software Pvt. Ltd., 977-1-425637, 3269, -14861313]

         */

        getUserDetails(separated[8]);



        getServices(separated[8]);
        saveId(sharedPreferenceClass.getUid(),separated[8]);
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

    private void getUserDetails(final String deviceId){

        try {
            if (showProgress == null){

                showProgress.showProgress();
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

                        if (response.body().getData().size() != 0){
                            for (Data userModelClass : response.body().getData()){

                                saveImage(userModelClass.getImage(),deviceId);

                            }
                        }





                    }else{
                        try {
                            if (showProgress != null){
                                showProgress.hideProgress();
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


    public void saveImage(final String image, final String deviceId){
        System.out.println("I am here1 !!!");
        new Thread(new Runnable(){
            @Override
            public void run() {
                Bitmap bm = null;
                URL url;
                try {
                    url = new URL(image);
                    bm = BitmapFactory.decodeStream(url.openStream());
                } catch(Exception e) {
                    e.printStackTrace();
                }

                new ImageSaver(getActivity()).
                        setFileName(""+image.replaceAll(RetrofitClient.imageUrl,"")).
                        setDirectoryName(".ezvz").
                        setExternal(true).
                        save(bm);

                if (bm != null){
                    dbHandler.deleteDataSingle(deviceId);

                    /*
                    (String user_name1, String user_address1, String user_phone1, String user_website1,String user_email1,String user_position1, String user_device_id1, String logo1,String color_code1,String used_layout1,String company1,String color_code_second1,String fax_no1,String po_box_no1){
                     */

                    dbHandler.insertData(separated[0],separated[1],separated[2],separated[4],separated[3],separated[6],separated[8],"/storage/emulated/0/Pictures/.ezvz/"+image.replaceAll(RetrofitClient.imageUrl,""),separated[7],separated[9],separated[10],separated[13],separated[11],separated[12]);


                    try {
                        if (showProgress != null){
                            showProgress.hideProgress();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }




                }

            }

        }).start();
    }

    private void saveId(String current_user_Id, final String saved_user_id){

        ApiInterface saveInterface = RetrofitClient.getFormData().create(ApiInterface.class);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("current_user_id",current_user_Id)
                .addFormDataPart("saved_user_id",saved_user_id)
                .build();

        saveInterface.saveId(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    System.out.println("I am here2 !!!");
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String status = jsonObject.optString("status");

                        if (status.equals("1")){
                            dbHandler.deleteDataSingle(separated[8]);

                    /*
                    (String user_name1, String user_address1, String user_phone1, String user_website1,String user_email1,String user_position1, String user_device_id1, String logo1,String color_code1,String used_layout1,String company1,String color_code_second1,String fax_no1,String po_box_no1){
                     */

                            startActivity(new Intent(getActivity(), MainActivity.class));

//                            if (separated[0].equals("corporate")){
//                                Intent intent = new Intent(getActivity(), CorporateList.class);
//                                startActivity(intent);
//                            }else{
//                                Intent intent = new Intent(getActivity(), IndividualActivity.class);
//                                startActivity(intent);
//                            }
                            Toast.makeText(getActivity(), "Save", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }

    private void getServices(String id){

        ApiInterface serviceInterface = RetrofitClient.getFormData().create(ApiInterface.class);

        Call<ServiceModelClass> classCall = serviceInterface.getSingleServices(id);
        System.out.println("ID : "+id);

        classCall.enqueue(new Callback<ServiceModelClass>() {
            @Override
            public void onResponse(Call<ServiceModelClass> call, Response<ServiceModelClass> response) {

                if (response.isSuccessful()){

                    System.out.println("Response : "+response.body().toString());
                    if (response.body() != null){
                        if (response.body().getData().size() != 0)
                        {
                            for (com.kandktech.ezivizi.model_class.service_model.Data data : response.body().getData()){
                                System.out.println("I am here3 !!!");
                                dbHandler.addService(data.getUser_id(),data.getService_1(),data.getService_2(),data.getService_3(),data.getService_4(),data.getService_5(),data.getService_6());
                            }
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<ServiceModelClass> call, Throwable t) {

            }
        });

    }

}

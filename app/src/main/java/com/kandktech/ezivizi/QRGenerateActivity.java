package com.kandktech.ezivizi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Color;

import android.net.Uri;

import android.provider.MediaStore;

import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ScrollView;
import android.widget.Toast;


import com.kandktech.ezivizi.image_saver.ImageSaver;
import com.kandktech.ezivizi.progressDialog.ProgressDialogBox;
import com.kandktech.ezivizi.retrofit_api_client.RetrofitClient;
import com.kandktech.ezivizi.retrofit_api_interface.ApiInterface;
import com.kandktech.ezivizi.welcome_screen.WelcomeScreenActivity;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;

import java.io.IOException;

import java.net.URL;


import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.defaults.colorpicker.ColorPickerPopup;

public class QRGenerateActivity extends AppCompatActivity {
    Button button2,btnSave;
    ScrollView scrollView;
    EditText txtFullName,txtEmail,txtAddress,txtPhone,txtPosition,txtWebsite;
    String colorCode = "0";
    DbHandler dbHandler;

    String name,position,email,phone,web,address;
    FloatingActionButton btnLoad;
    final int RQS_IMAGE1 = 1;
    Uri source1;
    Bitmap bm1;
    CircleImageView userImg;
    File file;

    ProgressDialogBox progressDialogBox;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrgenerate);
        button2 = findViewById(R.id.button2);
        scrollView = findViewById(R.id.scrollView);
        dbHandler = new DbHandler(getApplicationContext());
        txtAddress = findViewById(R.id.txtAddress);
        txtEmail = findViewById(R.id.txtEmail);
        txtFullName = findViewById(R.id.txtFullname);
        txtPhone = findViewById(R.id.txtPhone);
        txtPosition = findViewById(R.id.txtPosition);
        txtWebsite = findViewById(R.id.txtWebsite);
        btnSave = findViewById(R.id.button);
        btnLoad = findViewById(R.id.floatingActionButton);
        userImg = findViewById(R.id.imageView2);

        progressDialogBox = new ProgressDialogBox(QRGenerateActivity.this);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectColor();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    saveData();
            }
        });




        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RQS_IMAGE1);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case RQS_IMAGE1:
                    source1 = data.getData();

                    try {
                        System.out.println("Bitmap path = "+source1.getPath());
                        bm1 = BitmapFactory.decodeStream(
                                getContentResolver().openInputStream(source1));
                        ByteArrayOutputStream out = new ByteArrayOutputStream();

                        bm1.compress(Bitmap.CompressFormat.PNG,100,out);
                        userImg.setImageBitmap(bm1);

                        String[] filePathColumn = { MediaStore.Images.Media.DATA };
                        Cursor cursor = getContentResolver().query(source1, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String filePath = cursor.getString(columnIndex);
                        file = new Compressor(this).compressToFile(new File(filePath));
                        cursor.close();


                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    }

    public void selectColor(){

        new ColorPickerPopup.Builder(this)
                .initialColor(Color.RED) // Set initial color
                .enableAlpha(true) // Enable alpha slider or not
                .okTitle("OK")
                .cancelTitle("Cancel")
                .showIndicator(true)
                .build()
                .show(scrollView, new ColorPickerPopup.ColorPickerObserver() {
                    @Override
                    public void onColorPicked(int color) {
                        System.out.println("Color : "+color);
                        colorCode = String.valueOf(color);
                        System.out.println("Color Code : "+colorCode);
                        button2.setBackgroundColor(color);
                    }
                });
    }

    public void saveData(){

        name = txtFullName.getText().toString().trim();
        email = txtEmail.getText().toString().trim();
        address = txtAddress.getText().toString().trim();
        phone = txtPhone.getText().toString().trim();
        position = txtPosition.getText().toString().trim();
        web = txtWebsite.getText().toString().trim();

        if (name.isEmpty()){
            txtFullName.setError("Enter Name");
            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.isEmpty()){
            txtEmail.setError("Enter Email");
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (phone.isEmpty()){
            txtPhone.setError("Enter Phone");
            Toast.makeText(this, "Enter Phone", Toast.LENGTH_SHORT).show();
            return;
        }
        if (web.isEmpty()){
            txtWebsite.setError("Enter Website");
            Toast.makeText(this, "Enter Website", Toast.LENGTH_SHORT).show();
            return;
        }
        if (position.isEmpty()){
            txtPosition.setError("Enter Position");
            Toast.makeText(this, "Enter Position", Toast.LENGTH_SHORT).show();
            return;
        }
        if (address.isEmpty()){
            txtAddress.setError("Enter Address");
            Toast.makeText(this, "Enter Address", Toast.LENGTH_SHORT).show();
            return;
        }
        if (colorCode.equals("0")){
            Toast.makeText(this, "Please Choose Color", Toast.LENGTH_SHORT).show();
            return;
        }
        if (file == null){
            Toast.makeText(this, "Please Select Logo", Toast.LENGTH_SHORT).show();
            return;
        }

        saveUserImage(WelcomeScreenActivity.deviceId,file);

    }

//    private File saveBitMap(Context context, Bitmap drawView,String imgName){
//
//        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), ".ezivizi");
//
//        if (!pictureFileDir.exists()) {
//            boolean isDirectoryCreated = pictureFileDir.mkdirs();
//            if(!isDirectoryCreated)
//                Log.i("TAG", "Can't create directory to save the image");
//            return null;
//        }
//
//       String filename = pictureFileDir.getPath() +File.separator+ deviceId+".jpg";
//
//        File pictureFile = new File(filename);
//
//        try {
//            pictureFile.createNewFile();
//            FileOutputStream oStream = new FileOutputStream(pictureFile);
//            drawView.compress(Bitmap.CompressFormat.PNG, 100, oStream);
//
//            oStream.flush();
//            oStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.i("TAG", "There was an issue saving the image.");
//        }
//
//        return pictureFile;
//    }


    public void saveUserImage(final String deviceId, File image){

        progressDialogBox.showProgress();

        ApiInterface imageInterface = RetrofitClient.getFormData().create(ApiInterface.class);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id",deviceId)
                .addFormDataPart("image", image.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), image))
                .build();

        imageInterface.saveImage(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());

                        String status = jsonObject.optString("status");
                        JSONObject user_detail = jsonObject.getJSONObject("user_detail");
                        final String image = user_detail.optString("image");

                        if (status.equals("1")){

                            new Thread(new Runnable(){
                                @Override
                                public void run() {

                                    Bitmap bm = null;
                                    URL url;
                                    try {
                                        url = new URL(RetrofitClient.imageUrl+image);
                                        System.out.println("URL : "+url);
                                        bm = BitmapFactory.decodeStream(url.openStream());
                                    } catch(IOException e) {
                                        System.out.println(e);
                                    }

                                    new ImageSaver(getApplicationContext()).
                                            setFileName(""+image.replaceAll(RetrofitClient.imageUrl,"")).
                                            setDirectoryName(".ezvz").
                                            setExternal(true).
                                            save(bm);

                                    if (progressDialogBox != null) {
                                        progressDialogBox.hideProgress();
                                    }

                                    if (bm != null){
                                        Intent i = new Intent(getApplicationContext(),MainActivity.class);
                                        i.putExtra("color",colorCode);
                                        i.putExtra("name",name);
                                        i.putExtra("email",email);
                                        i.putExtra("position",position);
                                        i.putExtra("address",address);
                                        i.putExtra("web",web);
                                        i.putExtra("phone",phone);
                                        i.putExtra("device_id",deviceId);
                                        i.putExtra("image",RetrofitClient.imageUrl+image);
                                        startActivity(i);
                                    }

                                }
                            }).start();
                        }else{
                            if (progressDialogBox != null) {
                                progressDialogBox.hideProgress();
                            }
                            Toast.makeText(QRGenerateActivity.this, "Failed to load image!!", Toast.LENGTH_SHORT).show();
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
}

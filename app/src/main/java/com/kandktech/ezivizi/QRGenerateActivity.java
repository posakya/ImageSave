package com.kandktech.ezivizi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Color;

import android.net.Uri;

import android.provider.MediaStore;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ScrollView;
import android.widget.Toast;


import com.kandktech.ezivizi.authentication.SharedPreferenceClass;
import com.kandktech.ezivizi.colorSlider.ColorPickerPopup;
import com.kandktech.ezivizi.image_saver.ImageSaver;
import com.kandktech.ezivizi.progressDialog.ShowProgress;
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

public class QRGenerateActivity extends AppCompatActivity {
    Button button2,btnSave;
    ScrollView scrollView;
    EditText txtFullName,txtEmail,txtAddress,txtPhone,txtPosition,txtWebsite,txtCompanyName;
    String colorCode = "0";
    DbHandler dbHandler;

    String name,position,email,phone,web,address,company;
    FloatingActionButton btnLoad;
    final int RQS_IMAGE1 = 1;
    Uri source1;
    Bitmap bm1;
    CircleImageView userImg;
    File file;

    ShowProgress showProgress;
    SharedPreferenceClass sharedPreferenceClass;

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
        txtCompanyName = findViewById(R.id.txtCompanyName);

        showProgress = new ShowProgress(QRGenerateActivity.this);

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

        sharedPreferenceClass = new SharedPreferenceClass(getApplicationContext());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case RQS_IMAGE1:
                    source1 = data.getData();

                    try {

                        bm1 = BitmapFactory.decodeStream(
                                getContentResolver().openInputStream(source1));
                        ByteArrayOutputStream out = new ByteArrayOutputStream();

                        bm1.compress(Bitmap.CompressFormat.JPEG,100,out);
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
        company = txtCompanyName.getText().toString().trim();

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

        if (company.isEmpty()){
            company = "";
        }

        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        i.putExtra("color_code",colorCode);
        i.putExtra("name",name);
        i.putExtra("email",email);
        i.putExtra("position",position);
        i.putExtra("address",address);
        i.putExtra("weblink",web);
        i.putExtra("phone",phone);
        i.putExtra("user_id",sharedPreferenceClass.getUid());
        i.putExtra("image", source1.toString());
        i.putExtra("company",company);
        startActivity(i);

//        saveUserImage(name,address,phone,web,email,position,sharedPreferenceClass.getUid(),colorCode,colorCode,"1","1","1",company,"1",file);

    }

//    public void saveUserImage(final String name, final String address, final String phone, String weblink, final String email, final String position, final String user_id, String color_code, String color_code_second, String layout, String fax_no, String po_box_no, String company_name, String paid_status, File image){
//
////        showProgress.showProgress();
//
//        System.out.println("Image : "+image.getName());
//
//        ApiInterface imageInterface = RetrofitClient.getFormData().create(ApiInterface.class);
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("name",name)
//                .addFormDataPart("address",address)
//                .addFormDataPart("phone",phone)
//                .addFormDataPart("email",email)
//                .addFormDataPart("weblink",weblink)
//                .addFormDataPart("position",position)
//                .addFormDataPart("user_id",user_id)
//                .addFormDataPart("layout",layout)
//                .addFormDataPart("fax_no",fax_no)
//                .addFormDataPart("color_code",color_code)
//                .addFormDataPart("color_code_second",color_code_second)
//                .addFormDataPart("po_box_no",po_box_no)
//                .addFormDataPart("company_name",company_name)
//                .addFormDataPart("paid_status",paid_status)
//                .addFormDataPart("image", image.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), image))
//                .build();
//
//        imageInterface.saveQr(requestBody).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.isSuccessful()){
//                    Toast.makeText(QRGenerateActivity.this, "Success", Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(QRGenerateActivity.this, "Failed", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });
//
//
//    }


}

package com.kandktech.ezivizi.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kandktech.ezivizi.DbHandler;

import com.kandktech.ezivizi.R;
import com.kandktech.ezivizi.colorSlider.ColorPickerPopup;
import com.kandktech.ezivizi.corporate.CorporateActivity;
import com.kandktech.ezivizi.model_class.ServicesModelClass;
import com.kandktech.ezivizi.progressDialog.ShowProgress;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;


public class CorporateQRGenerate extends AppCompatActivity {

    List<ServicesModelClass> servicesModelClassList;

    Button button2,btnSave,btnAddServices;
    ScrollView scrollView;
    EditText txtFullName,txtEmail,txtAddress,txtPhone,txtPosition,txtWebsite,txtCompanyName,txtFax,txtPoBox;
    String colorCode = "0";
    DbHandler dbHandler;
    String alreadyExist = "no";
    String name,position,email,phone,web,address,company,fax,pobox;
    FloatingActionButton btnLoad;
    final int RQS_IMAGE1 = 1;
    Uri source1;
    Bitmap bm1;
    CircleImageView userImg;
    File file;
    String service1,service2,service3,service4,service5,service6;
    ShowProgress showProgress;
    SharedPreferenceClass sharedPreferenceClass;
    Dialog dialog;

    EditText editSer1,editSer2,editSer3,editSer4,editSer5,editSer6;
    Button btnSaveService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corporate_login);

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
        txtFax = findViewById(R.id.txtFaxNo);
        txtPoBox = findViewById(R.id.txtPoBox);
        btnAddServices = findViewById(R.id.btnAddServices);


        servicesModelClassList = new ArrayList<>();

        showProgress = new ShowProgress(CorporateQRGenerate.this);

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

        btnAddServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(CorporateQRGenerate.this, R.style.Dialog);
                dialog.setContentView(R.layout.services);
                dialog.setTitle("Add Your Services");


                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                dialog.setCanceledOnTouchOutside(true);

                editSer1 = dialog.findViewById(R.id.editService1);
                editSer2 = dialog.findViewById(R.id.editService2);
                editSer3 = dialog.findViewById(R.id.editService3);
                editSer4 = dialog.findViewById(R.id.editService4);
                editSer5 = dialog.findViewById(R.id.editService5);
                editSer6 = dialog.findViewById(R.id.editService6);
                btnSaveService = dialog.findViewById(R.id.btnSaveServices);


                btnSaveService.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saveServices();
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });}

    public void saveServices(){

        service1 = editSer1.getText().toString().trim();
        service2 = editSer2.getText().toString().trim();
        service3 = editSer3.getText().toString().trim();
        service4 = editSer4.getText().toString().trim();
        service5 = editSer5.getText().toString().trim();
        service6 = editSer6.getText().toString().trim();

        if (service6.isEmpty()){
            service6 = "no service";
        }

        if (service1.isEmpty()){
            service1 = "no service";
        }

        if (service2.isEmpty()){
            service2 = "no service";
        }
        if (service3.isEmpty()){
            service3 = "no service";
        }

        if (service4.isEmpty()){
            service4 = "no service";
        }

        if (service5.isEmpty()){
            service5 = "no service";
        }

        dbHandler.addService(sharedPreferenceClass.getUid(),service1,service2,service3,service4,service5,service6);


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
        fax = txtFax.getText().toString();
        pobox = txtPoBox.getText().toString();

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
            Toast.makeText(this, "Please Enter Company Name", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent i = new Intent(getApplicationContext(), CorporateActivity.class);
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
        i.putExtra("fax",fax);
        i.putExtra("pobox",pobox);
        i.putExtra("exists",alreadyExist);
        startActivity(i);

    }


}

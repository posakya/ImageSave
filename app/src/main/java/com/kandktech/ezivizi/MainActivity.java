package com.kandktech.ezivizi;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.kandktech.ezivizi.image_saver.ImageSaver;
import com.kandktech.ezivizi.retrofit_api_client.RetrofitClient;
import com.kandktech.ezivizi.welcome_screen.WelcomeScreenActivity;
import com.scottyab.aescrypt.AESCrypt;
import com.squareup.picasso.Picasso;

import java.io.File;

import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {

    DbHandler dbHandler;
    String filename = null;
    String colorCode;
    CardView halfView, rightView;
    CardView semiView;
    Button btnDone;
    QRGenerateActivity qrGenerateActivity;
    ImageView phImg,locImg,webImg,emailImg,phImg1,locImg1,webImg1,emailImg1,phImg2,locImg2,webImg2,emailImg2,img,img1,img2;
    TextView txtPh,txtEmail,txtWeb,txtAddress,txtPh1,txtEmail1,txtWeb1,txtAddress1,txtPh2,txtEmail2,txtWeb2,txtAddress2,txtName,txtName1,txtName2,txtPos,txtPos1,txtPos2;
    RadioButton rbSemi,rbHalf,rbRight;
    RadioGroup radioGroup1;
    String usedLayout = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        qrGenerateActivity = new QRGenerateActivity();
        dbHandler = new DbHandler(getApplicationContext());

        halfView = findViewById(R.id.halfView);
        semiView = findViewById(R.id.semiView);
        rightView = findViewById(R.id.rightView);
        btnDone = findViewById(R.id.btnDone);

        /*
         image
        */
        phImg = findViewById(R.id.phImg);
        locImg = findViewById(R.id.locImg);
        webImg = findViewById(R.id.webImg);
        emailImg = findViewById(R.id.emailImg);
        phImg1 = findViewById(R.id.phImg1);
        locImg1 = findViewById(R.id.locImg1);
        webImg1 = findViewById(R.id.webImg1);
        emailImg1 = findViewById(R.id.emailImg1);
        phImg2 = findViewById(R.id.phImg2);
        locImg2 = findViewById(R.id.locImg2);
        webImg2 = findViewById(R.id.webImg2);
        emailImg2 = findViewById(R.id.emailImg2);
        img = findViewById(R.id.imageView);
        img1 = findViewById(R.id.imageView1);
        img2 = findViewById(R.id.imageView3);


        /*
            text
        */
        txtPh = findViewById(R.id.txtPh);
        txtAddress = findViewById(R.id.txtAddress);
        txtEmail = findViewById(R.id.txtEmail);
        txtWeb = findViewById(R.id.txtWeb);
        txtPh1 = findViewById(R.id.txtPh1);
        txtAddress1 = findViewById(R.id.txtAddress1);
        txtEmail1 = findViewById(R.id.txtEmail1);
        txtWeb1 = findViewById(R.id.txtWeb1);
        txtPh2 = findViewById(R.id.txtPh2);
        txtAddress2 = findViewById(R.id.txtAddress2);
        txtEmail2 = findViewById(R.id.txtEmail2);
        txtWeb2 = findViewById(R.id.txtWeb2);
        txtName = findViewById(R.id.textView);
        txtName1 = findViewById(R.id.textView5);
        txtName2 = findViewById(R.id.textView1);
        txtPos = findViewById(R.id.textView6);
        txtPos1 = findViewById(R.id.textView12);
        txtPos2 = findViewById(R.id.textView2);

        /*
        radio button
        */
        rbHalf = findViewById(R.id.rbHalf);
        rbRight = findViewById(R.id.rbRight);
        rbSemi = findViewById(R.id.rbSemi);
        radioGroup1 = findViewById(R.id.radioGroup1);

        rbHalf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });

        rbHalf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioGroup1.clearCheck();
                rbHalf.setChecked(true);
                rbRight.setChecked(false);
                rbSemi.setChecked(false);
                usedLayout = "3";
            }
        });

        rbSemi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioGroup1.clearCheck();
                rbHalf.setChecked(false);
                rbRight.setChecked(false);
                rbSemi.setChecked(true);
                usedLayout = "2";
            }
        });

        rbRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioGroup1.clearCheck();
                rbHalf.setChecked(false);
                rbRight.setChecked(true);
                rbSemi.setChecked(false);
                usedLayout = "1";
            }
        });

        filename = getIntent().getExtras().getString("image");

        Picasso.get().load(filename).into(img);
        Picasso.get().load(filename).into(img1);
        Picasso.get().load(filename).into(img2);

        try {

            colorCode = getIntent().getExtras().getString("color");

            GradientDrawable drawable = (GradientDrawable) phImg.getBackground();
            drawable.setColor(Integer.parseInt(colorCode));

            GradientDrawable drawable1 = (GradientDrawable) webImg.getBackground();
            drawable1.setColor(Integer.parseInt(colorCode));

            GradientDrawable drawable2 = (GradientDrawable) emailImg.getBackground();
            drawable2.setColor(Integer.parseInt(colorCode));

            GradientDrawable drawable3 = (GradientDrawable) locImg.getBackground();
            drawable3.setColor(Integer.parseInt(colorCode));

            GradientDrawable drawable4 = (GradientDrawable) phImg1.getBackground();
            drawable4.setColor(Integer.parseInt(colorCode));

            GradientDrawable drawable5 = (GradientDrawable) webImg1.getBackground();
            drawable5.setColor(Integer.parseInt(colorCode));

            GradientDrawable drawable6 = (GradientDrawable) emailImg1.getBackground();
            drawable6.setColor(Integer.parseInt(colorCode));

            GradientDrawable drawable7 = (GradientDrawable) locImg1.getBackground();
            drawable7.setColor(Integer.parseInt(colorCode));

            GradientDrawable drawable8 = (GradientDrawable) phImg2.getBackground();
            drawable8.setColor(Integer.parseInt(colorCode));

            GradientDrawable drawable9 = (GradientDrawable) webImg2.getBackground();
            drawable9.setColor(Integer.parseInt(colorCode));

            GradientDrawable drawable10 = (GradientDrawable) emailImg2.getBackground();
            drawable10.setColor(Integer.parseInt(colorCode));

            GradientDrawable drawable11 = (GradientDrawable) locImg2.getBackground();
            drawable11.setColor(Integer.parseInt(colorCode));

            txtPh.setLinkTextColor(Integer.parseInt(colorCode));
            txtWeb.setLinkTextColor(Integer.parseInt(colorCode));
            txtEmail.setLinkTextColor(Integer.parseInt(colorCode));
            txtAddress.setTextColor(Integer.parseInt(colorCode));
            txtPh1.setLinkTextColor(Integer.parseInt(colorCode));
            txtWeb1.setLinkTextColor(Integer.parseInt(colorCode));
            txtEmail1.setLinkTextColor(Integer.parseInt(colorCode));
            txtAddress1.setTextColor(Integer.parseInt(colorCode));
            txtPh2.setLinkTextColor(Integer.parseInt(colorCode));
            txtWeb2.setLinkTextColor(Integer.parseInt(colorCode));
            txtEmail2.setLinkTextColor(Integer.parseInt(colorCode));
            txtAddress2.setTextColor(Integer.parseInt(colorCode));

            txtPos.setText(getIntent().getExtras().getString("position"));
            txtName.setText(getIntent().getExtras().getString("name"));
            txtAddress.setText(getIntent().getExtras().getString("address"));
            txtPh.setText(getIntent().getExtras().getString("phone"));
            txtWeb.setText(getIntent().getExtras().getString("web"));
            txtEmail.setText(getIntent().getExtras().getString("email"));

            txtPos1.setText(getIntent().getExtras().getString("position"));
            txtName1.setText(getIntent().getExtras().getString("name"));
            txtAddress1.setText(getIntent().getExtras().getString("address"));
            txtPh1.setText(getIntent().getExtras().getString("phone"));
            txtWeb1.setText(getIntent().getExtras().getString("web"));
            txtEmail1.setText(getIntent().getExtras().getString("email"));

            txtPos2.setText(getIntent().getExtras().getString("position"));
            txtName2.setText(getIntent().getExtras().getString("name"));
            txtAddress2.setText(getIntent().getExtras().getString("address"));
            txtPh2.setText(getIntent().getExtras().getString("phone"));
            txtWeb2.setText(getIntent().getExtras().getString("web"));
            txtEmail2.setText(getIntent().getExtras().getString("email"));

            halfView.setBackgroundColor(Integer.parseInt(colorCode));
            semiView.setCardBackgroundColor(Integer.parseInt(colorCode));
            rightView.setBackgroundColor(Integer.parseInt(colorCode));



        } catch (Exception e) {
            e.printStackTrace();
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (usedLayout.equals("0")){
                    Toast.makeText(MainActivity.this, "Please Select View!!", Toast.LENGTH_SHORT).show();
                }else{
                    dbHandler.deleteDataSingle(WelcomeScreenActivity.deviceId);
                    dbHandler.insertData(getIntent().getExtras().getString("name"),getIntent().getExtras().getString("address"),getIntent().getExtras().getString("phone"),getIntent().getExtras().getString("web"),getIntent().getExtras().getString("email"),getIntent().getExtras().getString("position"),WelcomeScreenActivity.deviceId,"/storage/emulated/0/Pictures/.ezvz/"+filename.replaceAll(RetrofitClient.imageUrl,""),getIntent().getExtras().getString("color"),usedLayout);
                    generateQrCode(getIntent().getExtras().getString("name"),getIntent().getExtras().getString("address"),getIntent().getExtras().getString("email"),getIntent().getExtras().getString("phone"),getIntent().getExtras().getString("web"),getIntent().getExtras().getString("position"),"/storage/emulated/0/Pictures/.ezvz/"+filename.replaceAll(RetrofitClient.imageUrl,""),getIntent().getExtras().getString("color"),usedLayout);
                    startActivity(new Intent(getApplicationContext(),FirstPageActivity.class));
                }

            }
        });
    }


    public void generateQrCode(String userName,String address, String email, String phone,String website,String position,String filename1,String ColorCode,String usedLayout){
        QRCodeWriter writer = new QRCodeWriter();

        String qrCodeData = userName+"EZVZ"+address+"EZVZ"+phone+"EZVZ"+email+"EZVZ"+website+"EZVZ"+filename1+"EZVZ"+position+"EZVZ"+ColorCode+"EZVZ"+ WelcomeScreenActivity.deviceId +"EZVZ"+usedLayout;
        String password = String.valueOf(R.string.app_name);
        String encryptedMsg = "";
        try {
            encryptedMsg = AESCrypt.encrypt(password, qrCodeData);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        try {
            BitMatrix bitMatrix = writer.encode(encryptedMsg, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }

            }

            new ImageSaver(getApplicationContext()).
                    setFileName("_image_"+WelcomeScreenActivity.deviceId+".jpg").
                    setDirectoryName(".ezvz").
                    setExternal(true).
                    save(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

}

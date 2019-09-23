package com.kandktech.ezivizi.corporate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esewa.android.sdk.payment.ESewaConfiguration;
import com.esewa.android.sdk.payment.ESewaPayment;
import com.esewa.android.sdk.payment.ESewaPaymentActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.kandktech.ezivizi.DbHandler;
import com.kandktech.ezivizi.FirstPageActivity;
import com.kandktech.ezivizi.MainActivity;
import com.kandktech.ezivizi.QRGenerateActivity;
import com.kandktech.ezivizi.R;
import com.kandktech.ezivizi.authentication.SharedPreferenceClass;
import com.kandktech.ezivizi.image_saver.ImageSaver;
import com.kandktech.ezivizi.model_class.ServicesModelClass;
import com.kandktech.ezivizi.progressDialog.ShowProgress;
import com.kandktech.ezivizi.retrofit_api_client.RetrofitClient;
import com.kandktech.ezivizi.retrofit_api_interface.ApiInterface;
import com.kandktech.ezivizi.welcome_screen.WelcomeScreenActivity;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.scottyab.aescrypt.AESCrypt;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CorporateActivity extends AppCompatActivity implements View.OnClickListener {


    RecyclerView listView,listView1,listView2,listView3,listView4,listView5;
    String[] name = {"- Mobile Exchange Offer", "- Laptop Exchange Offer", "- Mobile Repairing", "- Laptop Repairing", "- Iphone Unlock", "- Samsung Device Unlock"};


    DbHandler dbHandler;
    Dialog dialog;
    String colorCode;

    CardView semiView;

    Button btnDone;
    QRGenerateActivity qrGenerateActivity;
    ImageView phImg, locImg, webImg, emailImg, phImg1, locImg1, webImg1, emailImg1, phImg2, locImg2, webImg2, emailImg2;
    TextView txtPh, txtEmail, txtWeb, txtAddress, txtPh1, txtEmail1, txtWeb1, txtAddress1, txtPh2, txtEmail2, txtWeb2, txtAddress2, txtName, txtName1, txtName2, txtPos, txtPos1, txtPos2;
    RadioButton rbSemi, rbHalf, rbRight, rbCurve, rbHalfCurve, rbSide, rbUpdown;
    RadioGroup radioGroup1;
    String usedLayout = "0";
    ImageView view12;
    ImageView phImg3, locImg3, webImg3, emailImg3, phImg4, locImg4, webImg4, emailImg4;
    TextView txtPh3, txtEmail3, txtWeb3, txtAddress3, txtPh4, txtEmail4, txtWeb4, txtAddress4, txtPh5, txtEmail5, txtWeb5, txtAddress5, txtPh6, txtEmail6, txtWeb6, txtAddress6;
    TextView txtName3, txtPos3, txtName4, txtPos4, txtName5, txtPos5, txtName6, txtPos6;
    CircleImageView img3, img4, img5, img6, img, img1, img2;
    TextView txtCompany1, txtCompany2, txtCompany3, txtCompany4, txtCompany5, txtCompany6,txtCompany7;
    TextView txtFax1, txtFax2, txtFax3, txtFax4, txtFax5, txtFax6, txtPo1, txtPo2, txtPo3, txtPo4, txtPo5, txtPo6;
    String faxNo, poBoxNo;
    SharedPreferenceClass sharedPreferenceClass;

    RelativeLayout sideView;
    RelativeLayout halfView, rightView;
    RelativeLayout curveView, halfCurveView, up_downView;
    EasyFlipView easyFlipView,easyFlipView1,easyFlipView2,easyFlipView3,easyFlipView4,easyFlipView5;
    ConstraintLayout conHalfView,conRightView,conSideView,conCurView,conHalfCurveView,conUpDownView;
    ImageView bckImg,bckImg1,bckImg2,bckImg3,bckImg4,bckImg5;


    Bitmap bm1;
    File file;
    Uri source1;

    ShowProgress showProgress;
    ImageView btnPayPal, btnEsewa;

    private final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";

    private int REQUEST_CODE_PAYMENT​ = 1011;

    PayPalConfiguration m_configuration;
    String screte_key = "AYTFYWPYt-8DwmNYGg0N3kJSlrR6eaTEuopVvpAQP30ivzsmIg9P9PBL1XeD34LkIFlDp_r3xYIurO_b";
    Intent m_service;
    int m_paypalRequestCode = 999;

    RelativeLayout flip_back,flip_back1,flip_back2,flip_back3,flip_back4,flip_back5;

    List<ServicesModelClass> servicesModelClassesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corporate);

        qrGenerateActivity = new QRGenerateActivity();
        dbHandler = new DbHandler(getApplicationContext());

        servicesModelClassesList = new ArrayList<>();


        showProgress = new ShowProgress(CorporateActivity.this);
        sharedPreferenceClass = new SharedPreferenceClass(getApplicationContext());

//        view12 = findViewById(R.id.view12);
        easyFlipView = findViewById(R.id.cardFlipView);
        easyFlipView1 = findViewById(R.id.cardFlipView1);
        easyFlipView2 = findViewById(R.id.cardFlipView2);
        easyFlipView3 = findViewById(R.id.cardFlipView3);
        easyFlipView4 = findViewById(R.id.cardFlipView4);
        easyFlipView5 = findViewById(R.id.cardFlipView5);



        halfView = findViewById(R.id.halfView);
        semiView = findViewById(R.id.semiView);
        rightView = findViewById(R.id.rightView);
        curveView = findViewById(R.id.curveView);
        sideView = findViewById(R.id.sideView);
        halfCurveView = findViewById(R.id.halfCurveView);
        up_downView = findViewById(R.id.UpDownView);

        flip_back = findViewById(R.id.flip_back);
        flip_back1 = findViewById(R.id.flip_back1);
        flip_back2 = findViewById(R.id.flip_back2);
        flip_back3 = findViewById(R.id.flip_back3);
        flip_back4 = findViewById(R.id.flip_back4);
        flip_back5 = findViewById(R.id.flip_back5);


        easyFlipView.setFlipDuration(1000);
        easyFlipView.setFlipEnabled(true);
        easyFlipView1.setFlipDuration(1000);
        easyFlipView1.setFlipEnabled(true);
        easyFlipView2.setFlipDuration(1000);
        easyFlipView2.setFlipEnabled(true);
        easyFlipView3.setFlipDuration(1000);
        easyFlipView3.setFlipEnabled(true);
        easyFlipView5.setFlipDuration(1000);
        easyFlipView5.setFlipEnabled(true);
        easyFlipView4.setFlipDuration(1000);
        easyFlipView4.setFlipEnabled(true);

        listView = findViewById(R.id.listView);
        listView1 = findViewById(R.id.listView1);
        listView2= findViewById(R.id.listView2);
        listView3 = findViewById(R.id.listView3);
        listView4 = findViewById(R.id.listView4);
        listView5 = findViewById(R.id.listView5);


        bckImg = findViewById(R.id.bckimg);
        bckImg1 = findViewById(R.id.bckimg1);
        bckImg2 = findViewById(R.id.bckimg2);
        bckImg3 = findViewById(R.id.bckimg3);
        bckImg4 = findViewById(R.id.bckimg4);
        bckImg5= findViewById(R.id.bckimg5);

        conCurView = findViewById(R.id.conCurveView);
        conHalfCurveView = findViewById(R.id.conHalfCurveView);
        conHalfView = findViewById(R.id.conHalfView);
        conRightView = findViewById(R.id.conRightView);
        conSideView = findViewById(R.id.consSideView);
        conUpDownView = findViewById(R.id.conUpDownView);


        sideView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easyFlipView.flipTheView();
            }
        });

        halfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easyFlipView5.flipTheView();
            }
        });

        curveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easyFlipView1.flipTheView();
            }
        });

        halfCurveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easyFlipView2.flipTheView();
            }
        });

        up_downView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easyFlipView3.flipTheView();
            }
        });

        rightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easyFlipView4.flipTheView();
            }
        });

        flip_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easyFlipView.flipTheView();
            }
        });

        flip_back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easyFlipView1.flipTheView();
            }
        });

        flip_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easyFlipView2.flipTheView();
            }
        });

        flip_back3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easyFlipView3.flipTheView();
            }
        });

        flip_back4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easyFlipView4.flipTheView();
            }
        });

        flip_back5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easyFlipView5.flipTheView();
            }
        });


        try {
            Cursor cursor = dbHandler.viewServices(sharedPreferenceClass.getUid());
            if (cursor != null){
                if (cursor.moveToFirst()) {
                    do {

                        ServicesModelClass servicesModelClass1 = new ServicesModelClass();

                        servicesModelClass1.setService6(cursor.getString(cursor.getColumnIndex(DbHandler.service_six)));
                        servicesModelClass1.setService5(cursor.getString(cursor.getColumnIndex(DbHandler.service_five)));
                        servicesModelClass1.setService4(cursor.getString(cursor.getColumnIndex(DbHandler.service_four)));
                        servicesModelClass1.setService3(cursor.getString(cursor.getColumnIndex(DbHandler.service_three)));
                        servicesModelClass1.setService2(cursor.getString(cursor.getColumnIndex(DbHandler.service_two)));
                        servicesModelClass1.setService1(cursor.getString(cursor.getColumnIndex(DbHandler.service_one)));

                        servicesModelClassesList.add(servicesModelClass1);

                    } while (cursor.moveToNext());

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        ServiceAdapterClass adapter = new ServiceAdapterClass(servicesModelClassesList);
        listView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listView.setHasFixedSize(true);
        listView.setAdapter(adapter);

        listView1.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listView1.setHasFixedSize(true);
        listView1.setAdapter(adapter);

        listView2.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listView2.setHasFixedSize(true);
        listView2.setAdapter(adapter);

        listView3.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listView3.setHasFixedSize(true);
        listView3.setAdapter(adapter);

        listView4.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listView4.setHasFixedSize(true);
        listView4.setAdapter(adapter);

        listView5.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listView5.setHasFixedSize(true);
        listView5.setAdapter(adapter);

        btnDone = findViewById(R.id.btnDone);


        faxNo = getIntent().getExtras().getString("fax");
        poBoxNo = getIntent().getExtras().getString("pobox");

        if (faxNo == null) {
            faxNo = "0";
        }

        if (poBoxNo == null) {
            poBoxNo = "0";
        }

        if (faxNo.equals("")) {
            faxNo = "0";
        }

        if (poBoxNo.equals("")) {
            poBoxNo = "0";
        }

        /*
        fax and post box number textview
         */

        txtFax1 = findViewById(R.id.txtFax1);
        txtFax2 = findViewById(R.id.txtFax2);
        txtFax3 = findViewById(R.id.txtFax3);
        txtFax4 = findViewById(R.id.txtFax4);
        txtFax5 = findViewById(R.id.txtFax5);
        txtFax6 = findViewById(R.id.txtFax6);

        txtPo1 = findViewById(R.id.txtPo1);
        txtPo2 = findViewById(R.id.txtPo2);
        txtPo3 = findViewById(R.id.txtPo3);
        txtPo4 = findViewById(R.id.txtPo4);
        txtPo5 = findViewById(R.id.txtPo5);
        txtPo6 = findViewById(R.id.txtPo6);

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
        locImg3 = findViewById(R.id.imageView6);
        locImg4 = findViewById(R.id.imageView11);
        webImg3 = findViewById(R.id.imageView8);
        webImg4 = findViewById(R.id.imageView13);
        emailImg3 = findViewById(R.id.imageView9);
        emailImg4 = findViewById(R.id.imageView14);
        phImg3 = findViewById(R.id.imageView7);
        phImg4 = findViewById(R.id.imageView12);
        img = findViewById(R.id.imageView);
        img1 = findViewById(R.id.imageView1);
        img2 = findViewById(R.id.imageView3);
        img3 = findViewById(R.id.imageView4);
        img4 = findViewById(R.id.imgLogo1);
        img5 = findViewById(R.id.imgLogo2);
        img6 = findViewById(R.id.imgLogo3);


        /*
        company text view
         */
        txtCompany1 = findViewById(R.id.txtCompanyName1);
        txtCompany3 = findViewById(R.id.txtCompanyName3);
        txtCompany2 = findViewById(R.id.textView18);
        txtCompany4 = findViewById(R.id.txtCompanyName2);
        txtCompany5 = findViewById(R.id.textView17);
        txtCompany6 = findViewById(R.id.textView19);
        txtCompany7 = findViewById(R.id.textView12);


        /*
            text
        */
        txtPh = findViewById(R.id.txtPh);
        txtPh1 = findViewById(R.id.txtPh1);
        txtPh2 = findViewById(R.id.txtPh2);
        txtPh3 = findViewById(R.id.txtPh3);
        txtPh4 = findViewById(R.id.txtPh4);
        txtPh5 = findViewById(R.id.txtPh5);
        txtPh6 = findViewById(R.id.textView8);

        txtWeb = findViewById(R.id.txtWeb);
        txtWeb1 = findViewById(R.id.txtWeb1);
        txtWeb2 = findViewById(R.id.txtWeb2);
        txtWeb3 = findViewById(R.id.txtWeb3);
        txtWeb4 = findViewById(R.id.txtWeb4);
        txtWeb5 = findViewById(R.id.txtWeb5);
        txtWeb6 = findViewById(R.id.textView10);

        txtEmail = findViewById(R.id.txtEmail);
        txtEmail1 = findViewById(R.id.txtEmail1);
        txtEmail2 = findViewById(R.id.txtEmail2);
        txtEmail3 = findViewById(R.id.txtEmail3);
        txtEmail4 = findViewById(R.id.txtEmail4);
        txtEmail5 = findViewById(R.id.txtEmail5);
        txtEmail6 = findViewById(R.id.textView9);

        txtAddress = findViewById(R.id.txtAddress);
        txtAddress1 = findViewById(R.id.txtAddress1);
        txtAddress2 = findViewById(R.id.txtAddress2);
        txtAddress6 = findViewById(R.id.textView7);
        txtAddress5 = findViewById(R.id.txtAddress5);
        txtAddress3 = findViewById(R.id.txtAddress3);
        txtAddress4 = findViewById(R.id.txtAddress4);

        /*
        username
        */
        txtName = findViewById(R.id.txtUserName);
        txtName1 = findViewById(R.id.textView5);
        txtName2 = findViewById(R.id.textView);
        txtName3 = findViewById(R.id.txtUserName1);
        txtName4 = findViewById(R.id.txtUserName2);
        txtName5 = findViewById(R.id.txtUserName3);
        txtName6 = findViewById(R.id.textView11);

        /*
        position
         */
        txtPos = findViewById(R.id.textView6);
        txtPos1 = findViewById(R.id.txtPosition);
        txtPos2 = findViewById(R.id.textView2);
        txtPos3 = findViewById(R.id.txtPosition1);
        txtPos4 = findViewById(R.id.textView13);
        txtPos5 = findViewById(R.id.txtPosition2);
        txtPos6 = findViewById(R.id.txtPosition3);

        /*
        radio button
        */
        rbHalf = findViewById(R.id.rbHalf);
        rbRight = findViewById(R.id.rbRight);
        rbSemi = findViewById(R.id.rbSemi);
        radioGroup1 = findViewById(R.id.radioGroup1);
        rbCurve = findViewById(R.id.rbCurveView);
        rbHalfCurve = findViewById(R.id.rbHalfCurve);
        rbSide = findViewById(R.id.rbSide);
        rbUpdown = findViewById(R.id.rbupDownView);


        /*
         radio button click listener
         */
        rbHalfCurve.setOnClickListener(this);
        rbSemi.setOnClickListener(this);
        rbHalf.setOnClickListener(this);
        rbSide.setOnClickListener(this);
        rbUpdown.setOnClickListener(this);
        rbRight.setOnClickListener(this);
        rbCurve.setOnClickListener(this);


        colorCode = getColorCode(Integer.parseInt(getIntent().getExtras().getString("color_code"))).replace("#","");


        getCompanyName();
        getAddressData();
        getAddressTextColor(colorCode);
        getIconColor(colorCode);
        getViewColor(colorCode);

        source1 = Uri.parse(getIntent().getExtras().getString("image"));
        System.out.println("Bitmap path = " + source1.getPath());
        Image image = new Image();
        image.execute();

        FaxPost();


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (usedLayout.equals("0")) {
                    Toast.makeText(CorporateActivity.this, "Please Select View!!", Toast.LENGTH_SHORT).show();
                }else if(getIntent().getExtras().getString("exists").equals("yes")){

                    saveUserImage(getIntent().getExtras().getString("name"), getIntent().getExtras().getString("address"), getIntent().getExtras().getString("phone"), getIntent().getExtras().getString("weblink"), getIntent().getExtras().getString("email"), getIntent().getExtras().getString("position"), sharedPreferenceClass.getUid(), getColorCode(Integer.parseInt(getIntent().getExtras().getString("color_code"))).replace("#",""), getColorCode(Integer.parseInt(getIntent().getExtras().getString("color_code"))).replace("#",""), usedLayout, faxNo, poBoxNo, getIntent().getExtras().getString("company"), "1", file);


                } else {
                    dialog = new Dialog(CorporateActivity.this, R.style.Dialog);
                    dialog.setContentView(R.layout.payment);
                    dialog.setTitle("Payment Method");

                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                    dialog.setCanceledOnTouchOutside(true);

                    btnEsewa = dialog.findViewById(R.id.btnEsewa);
                    btnPayPal = dialog.findViewById(R.id.btnPayPal);

                    btnPayPal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            m_configuration = new PayPalConfiguration()
                                    .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                                    .clientId(screte_key);
                            m_service = new Intent(CorporateActivity.this, PaymentActivity.class);
                            m_service.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, m_configuration);
                            startActivity(m_service);

                            PayPalPayment payment = new PayPalPayment(new BigDecimal(2.5), "USD", "Individual Visiting Card", PayPalPayment.PAYMENT_INTENT_SALE);
                            Intent intent = new Intent(CorporateActivity.this, PaymentActivity.class);
                            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, m_configuration);
                            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                            startActivityForResult(intent, m_paypalRequestCode);
                            dialog.dismiss();
                        }
                    });

                    btnEsewa.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            final ESewaConfiguration eSewaConfiguration = new ESewaConfiguration()
                                    .clientId("JB0BBQ4aD0UqIThFJwAKBgAXEUkEGQUBBAwdOgABHD4DChwUAB0R")
                                    .secretKey("BhwIWQQADhIYSxILExMcAgFXFhcOBwAKBgAXEQ==")
                                    .environment(ESewaConfiguration.ENVIRONMENT_TEST);

                            String code = randomAlphaNumeric(4);

                            ESewaPayment eSewaPayment = new ESewaPayment("220", "Individual Visiting Card", "EzVZ" + code, "https://ir-user.esewa.com.np/epay/main");

                            Intent intent = new Intent(getApplicationContext(), ESewaPaymentActivity.class);
                            intent.putExtra(ESewaConfiguration.ESEWA_CONFIGURATION, eSewaConfiguration);
                            intent.putExtra(ESewaPayment.ESEWA_PAYMENT, eSewaPayment);
                            startActivityForResult(intent, REQUEST_CODE_PAYMENT​);
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }


            }
        });



    }

    public void FaxPost() {
        if (faxNo.equals("") || faxNo == null) {
            txtFax1.setVisibility(View.GONE);
            txtFax2.setVisibility(View.GONE);
            txtFax3.setVisibility(View.GONE);
            txtFax4.setVisibility(View.GONE);
            txtFax5.setVisibility(View.GONE);
            txtFax6.setVisibility(View.GONE);
        } else {
            txtFax1.setText("Fax No. : "+faxNo);
            txtFax2.setText("Fax No. : "+faxNo);
            txtFax3.setText("Fax No. : "+faxNo);
            txtFax4.setText("Fax No. : "+faxNo);
            txtFax5.setText("Fax No. : "+faxNo);
            txtFax6.setText("Fax No. : "+faxNo);
        }

        if (poBoxNo.equals("") || poBoxNo == null) {
            txtPo1.setVisibility(View.GONE);
            txtPo2.setVisibility(View.GONE);
            txtPo3.setVisibility(View.GONE);
            txtPo4.setVisibility(View.GONE);
            txtPo5.setVisibility(View.GONE);
            txtPo6.setVisibility(View.GONE);
        } else {
            txtPo1.setText("Po Box No. : "+poBoxNo);
            txtPo2.setText("Po Box No. : "+poBoxNo);
            txtPo3.setText("Po Box No. : "+poBoxNo);
            txtPo4.setText("Po Box No. : "+poBoxNo);
            txtPo5.setText("Po Box No. : "+poBoxNo);
            txtPo6.setText("Po Box No. : "+poBoxNo);
        }

    }

    public void getCompanyName() {

        txtCompany1.setText(getIntent().getExtras().getString("company"));
        txtCompany2.setText(getIntent().getExtras().getString("company"));
        txtCompany3.setText(getIntent().getExtras().getString("company"));
        txtCompany4.setText(getIntent().getExtras().getString("company"));
        txtCompany5.setText(getIntent().getExtras().getString("company"));
        txtCompany6.setText(getIntent().getExtras().getString("company"));
        txtCompany7.setText(getIntent().getExtras().getString("company"));

    }

    public void getAddressData() {

        txtPos.setText(getIntent().getExtras().getString("position"));
        txtName.setText(getIntent().getExtras().getString("name"));
        txtAddress.setText(getIntent().getExtras().getString("address"));
        txtPh.setText(getIntent().getExtras().getString("phone"));
        txtWeb.setText(getIntent().getExtras().getString("weblink"));
        txtEmail.setText(getIntent().getExtras().getString("email"));

        txtPos1.setText(getIntent().getExtras().getString("position"));
        txtName1.setText(getIntent().getExtras().getString("name"));
        txtAddress1.setText(getIntent().getExtras().getString("address"));
        txtPh1.setText(getIntent().getExtras().getString("phone"));
        txtWeb1.setText(getIntent().getExtras().getString("weblink"));
        txtEmail1.setText(getIntent().getExtras().getString("email"));

        txtPos2.setText(getIntent().getExtras().getString("position"));
        txtName2.setText(getIntent().getExtras().getString("name"));
        txtAddress2.setText(getIntent().getExtras().getString("address"));
        txtPh2.setText(getIntent().getExtras().getString("phone"));
        txtWeb2.setText(getIntent().getExtras().getString("weblink"));
        txtEmail2.setText(getIntent().getExtras().getString("email"));

        txtPos3.setText(getIntent().getExtras().getString("position"));
        txtName3.setText(getIntent().getExtras().getString("name"));
        txtAddress3.setText(getIntent().getExtras().getString("address"));
        txtPh3.setText(getIntent().getExtras().getString("phone"));
        txtWeb3.setText(getIntent().getExtras().getString("weblink"));
        txtEmail3.setText(getIntent().getExtras().getString("email"));

        txtPos4.setText(getIntent().getExtras().getString("position"));
        txtName4.setText(getIntent().getExtras().getString("name"));
        txtAddress4.setText(getIntent().getExtras().getString("address"));
        txtPh4.setText(getIntent().getExtras().getString("phone"));
        txtWeb4.setText(getIntent().getExtras().getString("weblink"));
        txtEmail4.setText(getIntent().getExtras().getString("email"));

        txtPos5.setText(getIntent().getExtras().getString("position"));
        txtName5.setText(getIntent().getExtras().getString("name"));
        txtAddress5.setText(getIntent().getExtras().getString("address"));
        txtPh5.setText(getIntent().getExtras().getString("phone"));
        txtWeb5.setText(getIntent().getExtras().getString("weblink"));
        txtEmail5.setText(getIntent().getExtras().getString("email"));

        txtPos6.setText(getIntent().getExtras().getString("position"));
        txtName6.setText(getIntent().getExtras().getString("name"));
        txtAddress6.setText(getIntent().getExtras().getString("address"));
        txtPh6.setText(getIntent().getExtras().getString("phone"));
        txtWeb6.setText(getIntent().getExtras().getString("weblink"));
        txtEmail6.setText(getIntent().getExtras().getString("email"));

    }

    public void getAddressTextColor(String colorCode) {

        txtPh.setLinkTextColor(Color.parseColor("#"+colorCode));
        txtWeb.setLinkTextColor(Color.parseColor("#"+colorCode));
        txtEmail.setLinkTextColor(Color.parseColor("#"+colorCode));
        txtAddress.setTextColor(Color.parseColor("#"+colorCode));
        txtPh1.setLinkTextColor(Color.parseColor("#"+colorCode));
        txtWeb1.setLinkTextColor(Color.parseColor("#"+colorCode));
        txtEmail1.setLinkTextColor(Color.parseColor("#"+colorCode));
        txtAddress1.setTextColor(Color.parseColor("#"+colorCode));
        txtPh2.setLinkTextColor(Color.parseColor("#"+colorCode));
        txtWeb2.setLinkTextColor(Color.parseColor("#"+colorCode));
        txtEmail2.setLinkTextColor(Color.parseColor("#"+colorCode));
        txtAddress2.setTextColor(Color.parseColor("#"+colorCode));
        txtPh3.setLinkTextColor(Color.parseColor("#"+colorCode));
        txtWeb3.setLinkTextColor(Color.parseColor("#"+colorCode));
        txtEmail3.setLinkTextColor(Color.parseColor("#"+colorCode));
        txtAddress3.setTextColor(Color.parseColor("#"+colorCode));
        txtPh4.setLinkTextColor(Color.parseColor("#"+colorCode));
        txtWeb4.setLinkTextColor(Color.parseColor("#"+colorCode));
        txtEmail4.setLinkTextColor(Color.parseColor("#"+colorCode));
        txtAddress4.setTextColor(Color.parseColor("#"+colorCode));
        txtPh5.setLinkTextColor(Color.parseColor("#"+colorCode));
        txtWeb5.setLinkTextColor(Color.parseColor("#"+colorCode));
        txtEmail5.setLinkTextColor(Color.parseColor("#"+colorCode));
        txtAddress5.setTextColor(Color.parseColor("#"+colorCode));
        txtPh6.setLinkTextColor(Color.parseColor("#"+colorCode));
        txtWeb6.setLinkTextColor(Color.parseColor("#"+colorCode));
        txtEmail6.setLinkTextColor(Color.parseColor("#"+colorCode));
        txtAddress6.setTextColor(Color.parseColor("#"+colorCode));
        txtFax5.setBackgroundColor(Color.parseColor("#"+colorCode));
    }

    public void getIconColor(String colorCode) {

        GradientDrawable drawable = (GradientDrawable) phImg.getBackground();
        drawable.setColor(Color.parseColor("#"+colorCode));

        GradientDrawable drawable1 = (GradientDrawable) webImg.getBackground();
        drawable1.setColor(Color.parseColor("#"+colorCode));

        GradientDrawable drawable2 = (GradientDrawable) emailImg.getBackground();
        drawable2.setColor(Color.parseColor("#"+colorCode));

        GradientDrawable drawable3 = (GradientDrawable) locImg.getBackground();
        drawable3.setColor(Color.parseColor("#"+colorCode));

        GradientDrawable drawable4 = (GradientDrawable) phImg1.getBackground();
        drawable4.setColor(Color.parseColor("#"+colorCode));

        GradientDrawable drawable5 = (GradientDrawable) webImg1.getBackground();
        drawable5.setColor(Color.parseColor("#"+colorCode));

        GradientDrawable drawable6 = (GradientDrawable) emailImg1.getBackground();
        drawable6.setColor(Color.parseColor("#"+colorCode));

        GradientDrawable drawable7 = (GradientDrawable) locImg1.getBackground();
        drawable7.setColor(Color.parseColor("#"+colorCode));

        GradientDrawable drawable8 = (GradientDrawable) phImg2.getBackground();
        drawable8.setColor(Color.parseColor("#"+colorCode));

        GradientDrawable drawable9 = (GradientDrawable) webImg2.getBackground();
        drawable9.setColor(Color.parseColor("#"+colorCode));

        GradientDrawable drawable10 = (GradientDrawable) emailImg2.getBackground();
        drawable10.setColor(Color.parseColor("#"+colorCode));

        GradientDrawable drawable11 = (GradientDrawable) locImg2.getBackground();
        drawable11.setColor(Color.parseColor("#"+colorCode));

        GradientDrawable drawable12 = (GradientDrawable) phImg3.getBackground();
        drawable12.setColor(Color.parseColor("#"+colorCode));

        GradientDrawable drawable13 = (GradientDrawable) webImg3.getBackground();
        drawable13.setColor(Color.parseColor("#"+colorCode));

        GradientDrawable drawable14 = (GradientDrawable) emailImg3.getBackground();
        drawable14.setColor(Color.parseColor("#"+colorCode));

        GradientDrawable drawable15 = (GradientDrawable) locImg3.getBackground();
        drawable15.setColor(Color.parseColor("#"+colorCode));

        GradientDrawable drawable16 = (GradientDrawable) phImg4.getBackground();
        drawable16.setColor(Color.parseColor("#"+colorCode));

        GradientDrawable drawable17 = (GradientDrawable) webImg4.getBackground();
        drawable17.setColor(Color.parseColor("#"+colorCode));

        GradientDrawable drawable18 = (GradientDrawable) emailImg4.getBackground();
        drawable18.setColor(Color.parseColor("#"+colorCode));

        GradientDrawable drawable19 = (GradientDrawable) locImg4.getBackground();
        drawable19.setColor(Color.parseColor("#"+colorCode));

    }

    public void getViewColor(String colorCode) {

        conHalfView.setBackgroundColor(Color.parseColor("#"+colorCode));
        conUpDownView.setBackgroundColor(Color.parseColor("#"+colorCode));
        conSideView.setBackgroundColor(Color.parseColor("#"+colorCode));
        conRightView.setBackgroundColor(Color.parseColor("#"+colorCode));
        conHalfCurveView.setBackgroundColor(Color.parseColor("#"+colorCode));
        conCurView.setBackgroundColor(Color.parseColor("#"+colorCode));

        bckImg.setBackgroundColor(Color.parseColor("#"+colorCode));
        bckImg1.setBackgroundColor(Color.parseColor("#"+colorCode));
        bckImg2.setBackgroundColor(Color.parseColor("#"+colorCode));
        bckImg3.setBackgroundColor(Color.parseColor("#"+colorCode));
        bckImg4.setBackgroundColor(Color.parseColor("#"+colorCode));
        bckImg5.setBackgroundColor(Color.parseColor("#"+colorCode));

    }

    public void saveServices(String userId,String service1,String service2,String service3, String service4, String service5, String service6){
        ApiInterface serviceInterface = RetrofitClient.getFormData().create(ApiInterface.class);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id",userId)
                .addFormDataPart("service_1",service1)
                .addFormDataPart("service_2",service2)
                .addFormDataPart("service_3",service3)
                .addFormDataPart("service_4",service4)
                .addFormDataPart("service_5",service5)
                .addFormDataPart("service_6",service6)
                .build();

        serviceInterface.saveServices(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    System.out.println("Saved");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View view) {

        if (view == rbCurve) {
            radioGroup1.clearCheck();
            rbHalf.setChecked(false);
            rbRight.setChecked(false);
            rbSemi.setChecked(false);
            rbCurve.setChecked(true);
            rbUpdown.setChecked(false);
            rbHalfCurve.setChecked(false);
            rbSide.setChecked(false);
            usedLayout = "3";
        }

        if (view == rbHalf) {
            radioGroup1.clearCheck();
            rbHalf.setChecked(true);
            rbRight.setChecked(false);
            rbSemi.setChecked(false);
            rbCurve.setChecked(false);
            rbUpdown.setChecked(false);
            rbHalfCurve.setChecked(false);
            rbSide.setChecked(false);
            usedLayout = "1";
        }

        if (view == rbHalfCurve) {
            radioGroup1.clearCheck();
            rbHalf.setChecked(false);
            rbRight.setChecked(false);
            rbSemi.setChecked(false);
            rbCurve.setChecked(false);
            rbUpdown.setChecked(false);
            rbHalfCurve.setChecked(true);
            rbSide.setChecked(false);
            usedLayout = "4";
        }

        if (view == rbRight) {
            radioGroup1.clearCheck();
            rbHalf.setChecked(false);
            rbRight.setChecked(true);
            rbSemi.setChecked(false);
            rbCurve.setChecked(false);
            rbUpdown.setChecked(false);
            rbHalfCurve.setChecked(false);
            rbSide.setChecked(false);
            usedLayout = "6";
        }

        if (view == rbSemi) {
            radioGroup1.clearCheck();
            rbHalf.setChecked(false);
            rbRight.setChecked(false);
            rbSemi.setChecked(true);
            rbCurve.setChecked(false);
            rbUpdown.setChecked(false);
            rbHalfCurve.setChecked(false);
            rbSide.setChecked(false);
            usedLayout = "7";
        }

        if (view == rbUpdown) {
            radioGroup1.clearCheck();
            rbHalf.setChecked(false);
            rbRight.setChecked(false);
            rbSemi.setChecked(false);
            rbCurve.setChecked(false);
            rbUpdown.setChecked(true);
            rbHalfCurve.setChecked(false);
            rbSide.setChecked(false);
            usedLayout = "5";
        }

        if (view == rbSide) {
            radioGroup1.clearCheck();
            rbHalf.setChecked(false);
            rbRight.setChecked(false);
            rbSemi.setChecked(false);
            rbCurve.setChecked(false);
            rbUpdown.setChecked(false);
            rbHalfCurve.setChecked(false);
            rbSide.setChecked(true);
            usedLayout = "2";
        }


    }

    public class Image extends AsyncTask<String, Bitmap, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {

            try {

                bm1 = BitmapFactory.decodeStream(
                        getContentResolver().openInputStream(source1
                        ));
                ByteArrayOutputStream out = new ByteArrayOutputStream();

                bm1.compress(Bitmap.CompressFormat.JPEG, 100, out);


                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(source1, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                file = new Compressor(CorporateActivity.this).compressToFile(new File(filePath));
                cursor.close();


            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bm1;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            bitmap = bm1;
            img.setImageBitmap(bitmap);
            img2.setImageBitmap(bitmap);
            img1.setImageBitmap(bitmap);
            img3.setImageBitmap(bitmap);
            img4.setImageBitmap(bitmap);
            img5.setImageBitmap(bitmap);
            img6.setImageBitmap(bitmap);

        }
    }

    //userName:String,address : String,email:String,phone:String,website:String,position:String,filename1:String,ColorCode:String,usedLayout:String,company:String,fax_no:String,po_box_no:String,colorCodeSecond:String,userId:String
    public void generateQrCode(String userName, String address, String email, String phone, String website, String position, String filename1, String ColorCode, String usedLayout, String company, String fax_no, String po_box_no, String colorCodeSecond, String userId) {
        QRCodeWriter writer = new QRCodeWriter();

        String qrCodeData = userName + "EZVZ" + address + "EZVZ" + phone + "EZVZ" + email + "EZVZ" + website + "EZVZ" + filename1 + "EZVZ" + position + "EZVZ" + ColorCode + "EZVZ" + userId + "EZVZ" + usedLayout + "EZVZ" + company + "EZVZ" + fax_no + "EZVZ" + po_box_no + "EZVZ" + colorCodeSecond;
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

            SharedPreferences sharedPreferences = getSharedPreferences("qrImage", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("imgName", WelcomeScreenActivity.imgName);
            editor.apply();

            new ImageSaver(getApplicationContext()).
                    setFileName("_image_" + WelcomeScreenActivity.imgName + ".jpg").
                    setDirectoryName(".ezvz").
                    setExternal(true).
                    save(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PAYMENT​) {
            if (resultCode == RESULT_OK) {
                String message = data.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE);

                if (message != null)
                    try {
                        JSONObject jObj = new JSONObject(message);
                        String productId = jObj.optString("productId");
                        String totalAmount = jObj.optString("totalAmount");
                        String refID = jObj.optJSONObject("transactionDetails").optString("referenceId");
                        message = jObj.getJSONObject("message").optString("successMessage");
                        System.out.println("Pid : " + productId);
                        System.out.println("refid : " + refID);
                        System.out.println("message : " + message);
                        System.out.println("totlaAmount : " + totalAmount);
                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();


                        saveUserImage(getIntent().getExtras().getString("name"), getIntent().getExtras().getString("address"), getIntent().getExtras().getString("phone"), getIntent().getExtras().getString("weblink"), getIntent().getExtras().getString("email"), getIntent().getExtras().getString("position"), sharedPreferenceClass.getUid(), getColorCode(Integer.parseInt(getIntent().getExtras().getString("color_code"))).replace("#",""), getColorCode(Integer.parseInt(getIntent().getExtras().getString("color_code"))).replace("#",""), usedLayout, faxNo, poBoxNo, getIntent().getExtras().getString("company"), "1", file);


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Oops ! something went wrong", Toast.LENGTH_SHORT).show();
                    }


            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancel By User", Toast.LENGTH_SHORT).show();
            } else if (resultCode == ESewaPayment.RESULT_EXTRAS_INVALID) {
                String s = data.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE);
                System.out.println("Proof of payment1 : " + s);
            }
        } else if (requestCode == m_paypalRequestCode) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    String state = confirmation.getProofOfPayment().getState();

                    if (state.equals("approved")) {
                        saveUserImage(getIntent().getExtras().getString("name"), getIntent().getExtras().getString("address"), getIntent().getExtras().getString("phone"), getIntent().getExtras().getString("weblink"), getIntent().getExtras().getString("email"), getIntent().getExtras().getString("position"), sharedPreferenceClass.getUid(), getColorCode(Integer.parseInt(getIntent().getExtras().getString("color_code"))).replace("#",""),getColorCode(Integer.parseInt(getIntent().getExtras().getString("color_code"))).replace("#",""), usedLayout, faxNo, poBoxNo, getIntent().getExtras().getString("company"), "1", file);

                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    } else {
//                        m_response.setText("error in the payment");
                        Toast.makeText(getApplicationContext(), "Error in the payment", Toast.LENGTH_SHORT).show();
                    }
                } else {
//                    m_response.setText("Confirmation is null");
                    Toast.makeText(getApplicationContext(), "Confirmation is null", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    public void saveUserImage(String name, String address, String phone, String weblink, String email, String position, final String user_id, String color_code, final String color_code_second, String layout, final String fax_no, final String po_box_no, String company_name, String paid_status, File image) {

        showProgress.showProgress();

        System.out.println("Image : " + image.getName());

        ApiInterface imageInterface = RetrofitClient.getFormData().create(ApiInterface.class);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", name)
                .addFormDataPart("address", address)
                .addFormDataPart("phone", phone)
                .addFormDataPart("email", email)
                .addFormDataPart("weblink", weblink)
                .addFormDataPart("position", position)
                .addFormDataPart("color_code", color_code)
                .addFormDataPart("color_code_second", color_code_second)
                .addFormDataPart("user_id", user_id)
                .addFormDataPart("layout", layout)
                .addFormDataPart("fax_no", fax_no)
                .addFormDataPart("po_box_no", po_box_no)
                .addFormDataPart("company_name", company_name)
                .addFormDataPart("paid_status", paid_status)
                .addFormDataPart("image", image.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), image))
                .build();

        imageInterface.saveQr(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                System.out.println("Response : " + response.body());

                if (response.isSuccessful()) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());

                        String status = jsonObject.optString("status");

                        if (status.equals("1")) {
                            try {
                                Cursor cursor = dbHandler.viewServices(sharedPreferenceClass.getUid());
                                if (cursor != null){
                                    if (cursor.moveToFirst()) {
                                        do {

                                            saveServices(sharedPreferenceClass.getUid(),cursor.getString(cursor.getColumnIndex(DbHandler.service_one)),cursor.getString(cursor.getColumnIndex(DbHandler.service_two)),cursor.getString(cursor.getColumnIndex(DbHandler.service_three)),cursor.getString(cursor.getColumnIndex(DbHandler.service_four)),cursor.getString(cursor.getColumnIndex(DbHandler.service_five)),cursor.getString(cursor.getColumnIndex(DbHandler.service_six)));

                                        } while (cursor.moveToNext());

                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            final String image = jsonObject.optString("image");
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    Bitmap bm = null;
                                    URL url;
                                    try {
                                        url = new URL(image);
                                        System.out.println("URL : " + url);
                                        bm = BitmapFactory.decodeStream(url.openStream());
                                    } catch (IOException e) {
                                        System.out.println(e);
                                    }

                                    new ImageSaver(getApplicationContext()).
                                            setFileName("" + image.replaceAll(RetrofitClient.imageUrl, "")).
                                            setDirectoryName(".ezvz").
                                            setExternal(true).
                                            save(bm);


                                    if (showProgress != null) {
                                        showProgress.hideProgress();
                                    }

                                    if (bm != null) {
                                        dbHandler.deleteDataSingle(user_id);
                                        dbHandler.insertData(getIntent().getExtras().getString("name"), getIntent().getExtras().getString("address"), getIntent().getExtras().getString("phone"), getIntent().getExtras().getString("weblink"), getIntent().getExtras().getString("email"), getIntent().getExtras().getString("position"), user_id, "/storage/emulated/0/Pictures/.ezvz/" + image.replaceAll(RetrofitClient.imageUrl, ""), getColorCode(Integer.parseInt(getIntent().getExtras().getString("color_code"))).replace("#",""), usedLayout, getIntent().getExtras().getString("company"), getColorCode(Integer.parseInt(getIntent().getExtras().getString("color_code"))).replace("#",""), fax_no, po_box_no);
                                        System.out.println("Inserted");
                                        saveId(user_id,user_id);
                                        generateQrCode(getIntent().getExtras().getString("name"), getIntent().getExtras().getString("address"), getIntent().getExtras().getString("email"), getIntent().getExtras().getString("phone"), getIntent().getExtras().getString("weblink"), getIntent().getExtras().getString("position"), "/storage/emulated/0/Pictures/.ezvz/" + image.replaceAll(RetrofitClient.imageUrl, ""), getColorCode(Integer.parseInt(getIntent().getExtras().getString("color_code"))).replace("#",""), usedLayout, getIntent().getExtras().getString("company"), fax_no, po_box_no, getColorCode(Integer.parseInt(getIntent().getExtras().getString("color_code"))).replace("#",""), user_id);


                                    }

                                }
                            }).start();
                        } else {
                            if (showProgress != null) {
                                showProgress.hideProgress();
                            }
                            Toast.makeText(CorporateActivity.this, "Failed to load image!!", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (showProgress != null) {
                        showProgress.hideProgress();
                    }
                    Toast.makeText(CorporateActivity.this, "Failed to save detail!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (showProgress != null) {
                    showProgress.hideProgress();
                }
                Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    private void saveId(final String current_user_Id, final String saved_user_id){

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
                            startActivity(new Intent(getApplicationContext(), CorporateList.class));
                            Toast.makeText(getApplicationContext(), "Saved Data Successfully", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Failed to save", Toast.LENGTH_SHORT).show();
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

    public String getColorCode(int intColor){
        String hexColor = String.format("#%06X", (0xFFFFFFFF & intColor));
        System.out.println("HexCor : "+hexColor);
        return hexColor;
    }

    public class ServiceAdapterClass extends RecyclerView.Adapter<ServiceAdapterClass.MyViewHolder>{

        List<ServicesModelClass> list;

        public ServiceAdapterClass(List<ServicesModelClass> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_item,parent,false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            ServicesModelClass servicesModelClass = list.get(position);
            if (servicesModelClass.getService6().equals("no service")){
                holder.txtView6.setVisibility(View.GONE);
            }

            if (servicesModelClass.getService1().equals("no service")){
                holder.txtView1.setVisibility(View.GONE);
            }

            if (servicesModelClass.getService2().equals("no service")){
                holder.txtView2.setVisibility(View.GONE);
            }

            if (servicesModelClass.getService3().equals("no service")){
                holder.txtView3.setVisibility(View.GONE);
            }

            if (servicesModelClass.getService4().equals("no service")){
                holder.txtView4.setVisibility(View.GONE);
            }

            if (servicesModelClass.getService5().equals("no service")){
                holder.txtView5.setVisibility(View.GONE);
            }

            holder.txtView1.setText("1"+") "+servicesModelClass.getService1());
            holder.txtView2.setText("2"+") "+servicesModelClass.getService2());
            holder.txtView3.setText("3"+") "+servicesModelClass.getService3());
            holder.txtView4.setText("4"+") "+servicesModelClass.getService4());
            holder.txtView5.setText("5"+") "+servicesModelClass.getService5());
            holder.txtView6.setText("6"+") "+servicesModelClass.getService6());

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{

            TextView txtView1,txtView2,txtView3,txtView4,txtView5,txtView6;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                txtView1 = itemView.findViewById(R.id.txtView1);
                txtView2 = itemView.findViewById(R.id.txtView2);
                txtView3 = itemView.findViewById(R.id.txtView3);
                txtView4 = itemView.findViewById(R.id.txtView4);
                txtView5 = itemView.findViewById(R.id.txtView5);
                txtView6 = itemView.findViewById(R.id.txtView6);
            }
        }
    }
}

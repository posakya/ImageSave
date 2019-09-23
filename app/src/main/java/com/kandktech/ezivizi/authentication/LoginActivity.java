package com.kandktech.ezivizi.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.kandktech.ezivizi.DbHandler;
import com.kandktech.ezivizi.FirstPageActivity;
import com.kandktech.ezivizi.R;
import com.kandktech.ezivizi.image_saver.ImageSaver;
import com.kandktech.ezivizi.model_class.qr_code_detail.Data;
import com.kandktech.ezivizi.model_class.qr_code_detail.Product;
import com.kandktech.ezivizi.model_class.qr_code_detail.QrDetailModelClass;
import com.kandktech.ezivizi.model_class.qr_services_detail.QRServicesModelClass;
import com.kandktech.ezivizi.progressDialog.ShowProgress;
import com.kandktech.ezivizi.retrofit_api_client.RetrofitClient;
import com.kandktech.ezivizi.retrofit_api_interface.ApiInterface;
import com.kandktech.ezivizi.welcome_screen.WelcomeScreenActivity;
import com.scottyab.aescrypt.AESCrypt;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ShowProgress progress;
    SharedPreferences sp;

    DbHandler dbHandler;

    Button myGoogleBtn,myFbBtn;
    GoogleSignInClient mGoogleSignInClient;

    final static int RC_SIGN_IN = 111;

    String email,name,first_name,last_name;
    CallbackManager callbackManager;

    SharedPreferenceClass sharedPreferenceClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferenceClass = new SharedPreferenceClass(getApplicationContext());

        dbHandler = new DbHandler(getApplicationContext());

        myGoogleBtn = findViewById(R.id.google_login_button);
        myFbBtn = findViewById(R.id.fb_login_button);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestId()
                .requestProfile()
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        myGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });

        myFbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
            }
        });

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {

                        GraphRequest graphRequest   =   GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback()
                        {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response)
                            {
                                Log.d("JSON", ""+response.getJSONObject().toString());

                                try
                                {


                                    name        =   object.optString("name");
                                    first_name  =   object.optString("first_name");
                                    last_name   =   object.optString("last_name");
                                    JSONObject picture = object.getJSONObject("picture");
                                    JSONObject data = picture.getJSONObject("data");
                                    String url = data.optString("url");

                                    System.out.println("Url : "+url);
                                    System.out.println("Email : "+email);


                                    if (object.has("email")){

                                        email       =   object.optString("email");

                                    }else{
                                        email = "";
                                    }

                                    if (url == ""){
                                        url = "";
                                    }

                                    registerData(object.optString("id"),name,"roshan","","",url);

                                    sharedPreferenceClass.saveData(object.optString("id"),name,email,url);

                                    LoginManager.getInstance().logOut();





                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,first_name,last_name,picture.type(large)");
                        graphRequest.setParameters(parameters);
                        graphRequest.executeAsync();


                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        progress = new ShowProgress(LoginActivity.this);

        sp = getSharedPreferences("Login",MODE_PRIVATE);

    }


    @Override
    public void onStart() {
        super.onStart();

        try {
            if (sharedPreferenceClass.isLoggedIn()){
                startActivity(new Intent(getApplicationContext(), FirstPageActivity.class));
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account = null;
            try {
                account = task.getResult(ApiException.class);
            } catch (ApiException e) {
                e.printStackTrace();
            }


            String idToken = account.getIdToken();
            if (account.getPhotoUrl() == null){
                registerData(account.getId(),account.getDisplayName(),account.getEmail(),"","","");
                sharedPreferenceClass.saveData(account.getId(),account.getDisplayName(),account.getEmail(),"");
            }else{
                registerData(account.getId(),account.getDisplayName(),account.getEmail(),"","",account.getPhotoUrl().toString());
                sharedPreferenceClass.saveData(account.getId(),account.getDisplayName(),account.getEmail(),account.getPhotoUrl().toString());
            }

            System.out.println("Token : "+idToken);
            System.out.println("TokenID : "+account.getId());
        }
    }


    public void registerData(final String uid, String fullName, String email, String phone, String date, String profile){

        progress.showProgress();

        ApiInterface registerInterface = RetrofitClient.getFormData().create(ApiInterface.class);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id",uid)
                .addFormDataPart("name",fullName)
                .addFormDataPart("email",email)
                .addFormDataPart("profile_img",profile)
                .build();

        registerInterface.addUserData(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                System.out.println("Response : "+response.body());

                if (response.isSuccessful()){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String status = jsonObject.optString("status");
                        String message = jsonObject.optString("message");



                        if (status.equals("0")){

                            String data = jsonObject.optString("data");
                            Toast.makeText(LoginActivity.this, data, Toast.LENGTH_SHORT).show();

                        }else{
                            getQRCode(uid);
                            getServices(uid);
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                            sharedPreferenceClass.isLooggedIn();


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (progress != null) progress.hideProgress();
                }else{
                    if (progress != null) progress.hideProgress();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    public void getQRCode(String id){
        ApiInterface codeInterface = RetrofitClient.getFormData().create(ApiInterface.class);
        Call<QrDetailModelClass> qrDetailModelClassCall = codeInterface.qrCodeDetail(id);
        qrDetailModelClassCall.enqueue(new Callback<QrDetailModelClass>() {
            @Override
            public void onResponse(Call<QrDetailModelClass> call, Response<QrDetailModelClass> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){

                        if (response.body().getData().size() != 0){
                            for (Data data : response.body().getData()){
                                for (final Product product : data.getProduct()){
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {

                                            Bitmap bm = null;
                                            URL url;
                                            try {
                                                url = new URL(product.getImage());
                                                System.out.println("URL : " + url);
                                                bm = BitmapFactory.decodeStream(url.openStream());
                                            } catch (IOException e) {
                                                System.out.println(e);
                                            }

                                            new ImageSaver(getApplicationContext()).
                                                    setFileName("" + product.getImage().replaceAll(RetrofitClient.imageUrl, "")).
                                                    setDirectoryName(".ezvz").
                                                    setExternal(true).
                                                    save(bm);




                                            if (bm != null) {
                                                dbHandler.deleteDataSingle(product.getUser_id());
                                                dbHandler.insertData(product.getName(),product.getAddress(),product.getPhone(),product.getWeblink(),product.getEmail(),product.getPosition(),product.getUser_id(),"/storage/emulated/0/Pictures/.ezvz/" + product.getImage().replaceAll(RetrofitClient.imageUrl, ""),product.getColor_code(),product.getLayout(),product.getCompany_name(),product.getColor_code_second(),product.getFax_no(),product.getPo_box_no());
                                                generateQrCode(product.getName(),product.getAddress(),product.getEmail(),product.getPhone(),product.getWeblink(),product.getPosition(),"/storage/emulated/0/Pictures/.ezvz/" + product.getImage().replaceAll(RetrofitClient.imageUrl, ""),product.getColor_code(),product.getLayout(),product.getCompany_name(),product.getFax_no(),product.getPo_box_no(),product.getColor_code_second(),product.getUser_id());
                                                startActivity(new Intent(getApplicationContext(), FirstPageActivity.class));
                                            }

                                        }
                                    }).start();

                                }
                            }
                        }




                    }
                }
            }

            @Override
            public void onFailure(Call<QrDetailModelClass> call, Throwable t) {

            }
        });
    }

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

    public void getServices(String id){

        ApiInterface serviceInterface = RetrofitClient.getFormData().create(ApiInterface.class);

        Call<QRServicesModelClass> qrServicesModelClassCall = serviceInterface.getServices(id);

        qrServicesModelClassCall.enqueue(new Callback<QRServicesModelClass>() {
            @Override
            public void onResponse(Call<QRServicesModelClass> call, Response<QRServicesModelClass> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().getData().size() != 0){
                            for (com.kandktech.ezivizi.model_class.qr_services_detail.Data data : response.body().getData()){
                                for (com.kandktech.ezivizi.model_class.qr_services_detail.Product product : data.getProduct()){
                                    dbHandler.addService(product.getUser_id(),product.getService_1(),product.getService_2(),product.getService_3(),product.getService_4(),product.getService_5(),product.getService_6());
                                }
                            }
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<QRServicesModelClass> call, Throwable t) {

            }
        });

    }
}

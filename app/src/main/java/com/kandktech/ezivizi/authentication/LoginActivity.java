package com.kandktech.ezivizi.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.kandktech.ezivizi.FirstPageActivity;
import com.kandktech.ezivizi.R;
import com.kandktech.ezivizi.progressDialog.ShowProgress;
import com.kandktech.ezivizi.retrofit_api_client.RetrofitClient;
import com.kandktech.ezivizi.retrofit_api_interface.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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


    public void registerData(String uid,String fullName,String email,String phone,String date,String profile){

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
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), FirstPageActivity.class));
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
}

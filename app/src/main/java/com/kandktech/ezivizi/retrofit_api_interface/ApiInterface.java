package com.kandktech.ezivizi.retrofit_api_interface;



import com.kandktech.ezivizi.model_class.UserModelClass;
import com.kandktech.ezivizi.model_class.qr_code_detail.QrDetailModelClass;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Created by lokesh on 6/1/18.
 */

public interface ApiInterface {

    ///// post products api interface
    @POST("saveQr")
    Call<ResponseBody> saveQr(@Body RequestBody body);

    /*
    get user detail
     */
    @GET("getImage/{current_user_id}")
    Call<UserModelClass> userDetails(@Query("current_user_id") String current_user_id);

    /*
   get user detail
    */
    @GET("getViewData/{current_user_id}")
    Call<QrDetailModelClass> qrCodeDetail(@Query("current_user_id") String current_user_id);

    /*
    user_login
     */
    @POST("user_login")
    Call<ResponseBody> addUserData(@Body RequestBody body);

    /*
    saveId
     */
    @POST("saveId")
    Call<ResponseBody> saveId(@Body RequestBody body);
}

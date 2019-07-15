package com.kandktech.ezivizi.retrofit_api_interface;



import com.kandktech.ezivizi.model_class.UserModelClass;

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
    @POST("save_image.php")
    Call<ResponseBody> saveImage(@Body RequestBody body);

    /*
    get user detail
     */
    @GET("get_image_api.php")
    Call<UserModelClass> userDetails(@Query("user_id") String user_id);

}

package com.kandktech.ezivizi.retrofit_api_client;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClient {

    public static Retrofit retrofit = null;

//    public static String url = "http://EzVz.halfwaiter.com/ezivizi/";
    public static String imageUrl = "http://ezvz.ofoodesk.com/public/image/";

    public static String url = "http://ezvz.ofoodesk.com/public/api/";

    public static Retrofit getFormData() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}

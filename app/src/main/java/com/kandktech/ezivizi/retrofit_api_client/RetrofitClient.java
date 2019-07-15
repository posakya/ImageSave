package com.kandktech.ezivizi.retrofit_api_client;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lokesh on 5/20/18.
 */

public class RetrofitClient {

    public static Retrofit retrofit = null;

    public static String url = "http://EzVz.halfwaiter.com/ezivizi/";
    public static String imageUrl = "http://EzVz.halfwaiter.com/ezivizi/image/";

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

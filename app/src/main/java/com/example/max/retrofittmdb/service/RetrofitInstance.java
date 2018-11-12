package com.example.max.retrofittmdb.service;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit retrofit = null;
    private static String BASE_URL = "https://api.themoviedb.org/3/";


    // der return Typ ist das Interface (hier: GetCountryDataService), da hierüber die Retrofit instance angesprochen wird
    public static MovieDataService getService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            Log.v("***********retrofit","RETROFITINSTANCE: " + retrofit.toString());
            Log.v("***********baseurl","RETROFITINSTANCE: " + BASE_URL.toString());

        }
        // return retrofit instance mit create und dem interface
        return retrofit.create(MovieDataService.class);
    }


}

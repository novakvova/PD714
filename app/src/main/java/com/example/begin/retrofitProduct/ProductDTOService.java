package com.example.begin.retrofitProduct;

import com.example.begin.utils.network.ConnectivityInterceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductDTOService {
    private static ProductDTOService mInstance;
    private static final String BASE_URL = "http://10.0.2.2/api/";//"https://apppeter.azurewebsites.net/api/";
    private Retrofit mRetrofit;

    private ProductDTOService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Interceptor interJWT = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request newRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer sdsssdfsfsdfsdfsdfsdf")
                        .build();
                return chain.proceed(newRequest);
            }
        };

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityInterceptor())
                .addInterceptor(interJWT)
                .addInterceptor(interceptor);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
    }

    public static ProductDTOService getInstance() {
        if (mInstance == null) {
            mInstance = new ProductDTOService();
        }
        return mInstance;
    }

    public ProductDTOHolderApi getJSONApi() {
        return mRetrofit.create(ProductDTOHolderApi.class);
    }
}
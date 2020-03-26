package com.example.begin.utils.network;

import android.content.Context;
import android.util.Log;

import com.example.begin.application.BeginApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ConnectivityInterceptor implements Interceptor {
    private final Context context;
    public ConnectivityInterceptor() {
        context = BeginApplication.getAppContext();
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.d("dddd","------------------77777777-----------sadfasdf--------777777777---------");
        if (!NetworkUtil.isOnline(context)) {
            Log.d("dddd","-----------------------------sadfasdf-----------------");
            //Toast.makeText(mContext.getApplicationContext(), "ERROR Internet",Toast.LENGTH_LONG);
            throw new NoConnectivityException();
        }
        Request.Builder builder = chain.request().newBuilder();
        return chain.proceed(builder.build());
    }
}

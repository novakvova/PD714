package com.example.begin.network.interceptors;

import android.content.Context;

import com.example.begin.ConnectionInternetError;
import com.example.begin.NavigationHost;
import com.example.begin.application.BeginApplication;
import com.example.begin.network.NetworkUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ConnectivityInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Context context= BeginApplication.getAppContext();
        Request originalRequest = chain.request();

        if (!NetworkUtil.isOnline(context)) {
            BeginApplication beginApplication = (BeginApplication) context;
            ((ConnectionInternetError) beginApplication.getCurrentActivity()).navigateErrorPage();
        }
        Request newRequest = originalRequest.newBuilder()
                .build();
        return chain.proceed(newRequest);
    }
}

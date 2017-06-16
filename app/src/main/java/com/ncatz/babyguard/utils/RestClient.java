package com.ncatz.babyguard.utils;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ncatz.babyguard.Babyguard_Application;

import cz.msebera.android.httpclient.client.params.ClientPNames;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by yeray697 on 16/06/17.
 */

public class RestClient {
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, AsyncHttpResponseHandler responseHandler) {
        client.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
        client.get(url, responseHandler);
    }

    public static void get(String url, String authorization, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.removeAllHeaders();
        client.addHeader("Authorization","key="+authorization);
        client.addHeader("Content-Type:", "application/json");
        client.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
        client.get(url, params, responseHandler);
    }

    public static void post(String url, String authorization, StringEntity entity, AsyncHttpResponseHandler responseHandler) {
        client.removeAllHeaders();
        client.addHeader("Authorization", "key="+authorization);
        client.addHeader("Content-Type:", "application/json");
        client.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
        client.post(Babyguard_Application.getContext(), url, entity, "application/json", responseHandler);
    }

    public static void cancelRequests(Context c, boolean  flag) {
        client.cancelRequests(c, flag);
    }
}

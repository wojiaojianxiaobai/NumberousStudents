package com.wb.numerousstudents.Utils;

import com.socks.library.KLog;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyOKhttpUtil {
    private ResponseInterface responseInterface;

    private static MyOKhttpUtil myOkhttpUtil;
    private OkHttpClient mOkHttpClient;
    private Request.Builder mBuilder;
    private RequestBody mRequestBody;
    private Call mCall;
    public static MyOKhttpUtil getInstance(){
        if (myOkhttpUtil == null){
            myOkhttpUtil = new MyOKhttpUtil();
        }
        return myOkhttpUtil;
    }

    public void get(String url,FormBody.Builder formBodyBuild){
        mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).post(formBodyBuild.build()).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                responseInterface.findOnFail(e.toString());
                KLog.v("wb.z :e :" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                KLog.v("wb.z :response :" + response.body().string());
                responseInterface.findOnSuccess(response.body().string());

            }
        });
    }


    public void setMyOKHttpUtilListener(ResponseInterface responseInterface){
        this.responseInterface = responseInterface;

    }

    public interface ResponseInterface{

        void findOnSuccess(String response);

        void findOnFail(String response);
    }


}
package com.homechef.ict.ict_homechef.ConnectUtil;

import android.content.Context;

import com.homechef.ict.ict_homechef.ConnectUtil.ResponseBody.LoginGet;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConnectUtil {

    private HttpApi apiService;
    public static String baseUrl = HttpApi.Base_URL;
    private static Context mContext;
    private static Retrofit retrofit;

    private static class SingletonHolder {
        private static ConnectUtil INSTANCE = new ConnectUtil(mContext);
    }

    public static ConnectUtil getInstance(Context context) {
        if (context != null) {
            mContext = context;
        }
        return SingletonHolder.INSTANCE;
    }

    private ConnectUtil(Context context) {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }


    public ConnectUtil createBaseApi() {
        apiService = create(HttpApi.class);
        return this;
    }

    public  <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }

    public void postLogin(HashMap<String, String> parameters, final HttpCallback callback) {
        apiService.postLogin(parameters).enqueue(new Callback<LoginGet>() {
            @Override
            public void onResponse(Call<LoginGet> call, Response<LoginGet> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginGet> call, Throwable t) {
                callback.onError(t);
            }
        });
    }


}

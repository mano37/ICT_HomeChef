package com.homechef.ict.ict_homechef.ConnectUtil;

import android.content.Context;

import com.homechef.ict.ict_homechef.ConnectUtil.ErrorBody.APIError;
import com.homechef.ict.ict_homechef.ConnectUtil.RequestBody.LoginPut;
import com.homechef.ict.ict_homechef.ConnectUtil.RequestBody.RecipePut;
import com.homechef.ict.ict_homechef.ConnectUtil.RequestBody.SessionPut;
import com.homechef.ict.ict_homechef.ConnectUtil.ResponseBody.PostRecipeGet;
import com.homechef.ict.ict_homechef.ConnectUtil.ResponseBody.RecipeGet;
import com.homechef.ict.ict_homechef.ConnectUtil.ResponseBody.RecipeListGet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
ConnectUtil 사용법
실제 사용되는 코드에 주석만 넣은것

1. HashMap으로 서버에 보낼 신호를 작성한다.
    이때 작성되는 key의 이름들은 API Docs 또는 RequestBody 폴더를 참고한다.

 HashMap<String , String> loginPara = new HashMap<String , String>();

                loginPara.put("auth_code", authCode);
                loginPara.put("auth_type", "google");

2. 아래와 같이 ConnectUtil 생성한다

                ConnectUtil connectUtil;

                // 언제나 똑같음
                connectUtil = ConnectUtil.getInstance(this).createBaseApi();

                // 원하는 함수 ConnectUtil 폴더 안의 HttpApi에서 찾기
                // ex) /auth/login같은 경우 postLogin 함수

                connectUtil.postLogin(loginPara, new HttpCallback() {
                            @Override
                            public void onError(Throwable t) {
                            // 내부적 에러 발생할 경우
                            }
                            @Override
                            public void onSuccess(int code, Object receivedData) {
                            // 성공적으로 완료한 경우

3. 성공하는 경우의 결과값의 종류와 처리는 HttpApi 를 참고한다.

                                String data = (String) receivedData;

                            }

                            @Override
                            public void onFailure(int code) {
                            // 통신에 실패한 경우
                            // 결과값이 없다거나, 서버에서 오류를 리턴했거나
                            // 또는 ResponseBody 안의 key 값이 이상하거나
                            }
                        });


 */

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
        apiService.postLogin(new LoginPut(parameters)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("onResponse@@@@@@ ON Connect UITL postLogin@@@@@@@@");
                if (response.isSuccessful()) {
                    System.out.println("onResponse OK and Success on postLogin @@@@@@@@");
                    try {
                        callback.onSuccess(response.code(), response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("onResponse OK But Failure on postLogin @@@@@@@@@@@@");
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("onFailure@@@@@@ ON ConnectUtil postLogin@@@@@@@@");
                callback.onError(t);
            }
        });
    }

    public void postSession(String jwt_token, final HttpCallback callback) {
        apiService.postSession(new SessionPut(jwt_token)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("onResponse@@@@@@ ON Connect UITL postSession@@@@@@@@");
                if (response.isSuccessful()) {
                    System.out.println("onResponse OK and Success on postSession @@@@@@@@");
                    try {
                        callback.onSuccess(response.code(), response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("onResponse OK But Failure on postSession @@@@@@@@@@@@");
                    callback.onFailure(response.code());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("onFailure@@@@@@ ON ConnectUtil postSession@@@@@@@@");
                callback.onError(t);
            }
        });
    }

    public void postRecipe(Map<String, String> header, RecipePut parameter, final HttpCallback callback){

        apiService.postRecipe(header, parameter).enqueue(new Callback<PostRecipeGet>() {
            @Override
            public void onResponse(Call<PostRecipeGet> call, Response<PostRecipeGet> response) {
                if (response.isSuccessful()) {
                    System.out.println("onResponse OK and Success on postRecipe @@@@@@@@");
                    callback.onSuccess(response.code(), response.body());
                } else {
                    System.out.println("onResponse OK But Failure on postRecipe @@@@@@@@@@@@");
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<PostRecipeGet> call, Throwable t) {
                System.out.println("onFailure@@@@@@ ON ConnectUtil postRecipe@@@@@@@@");
                callback.onError(t);
            }
        });
    }

    public void getRecipeList(Map<String, String> header,
                              String contain,
                              String offset,
                              String limit,
                              String except,
                              final HttpCallback callback){
        apiService.getRecipeList(header,
                contain, offset, limit, except).enqueue(new Callback<List<RecipeListGet>>() {
            @Override
            public void onResponse(Call<List<RecipeListGet>> call, Response<List<RecipeListGet>> response) {
                System.out.println("onResponse@@@@@@ ON Connect UITL getRecipeList @@@@@@@@");
                if (response.isSuccessful()) {
                    System.out.println("onResponse OK and Success on getRecipeList @@@@@@@@");
                    callback.onSuccess(response.code(), response.body());
                } else {
                    System.out.println("onResponse OK But Failure on getRecipeList @@@@@@@@@@@@");
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<RecipeListGet>> call, Throwable t) {
                System.out.println("onFailure@@@@@@ ConnectUtil on getRecipeList @@@@@@@@");
                callback.onError(t);
            }
        });

    }

    public void getRecipe(Map<String,String> header, String id, final HttpCallback callback){
        apiService.getRecipe(header, id).enqueue(new Callback<RecipeGet>() {
            @Override
            public void onResponse(Call<RecipeGet> call, Response<RecipeGet> response) {
                System.out.println("onResponse@@@@@@ ON Connect UITL getRecipe@@@@@@@@");
                if (response.isSuccessful()) {
                    System.out.println("onResponse OK and Success on getRecipe @@@@@@@@");
                    callback.onSuccess(response.code(), response.body());
                } else {
                    System.out.println("onResponse OK But Failure on getRecipe @@@@@@@@@@@@");
                    APIError error = ErrorUtil.parseError(response);
                    System.out.println("Error message : " + error.message());
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<RecipeGet> call, Throwable t) {
                System.out.println("onFailure@@@@@@ ConnectUtil on getRecipe @@@@@@@@");
                callback.onError(t);
            }
        });
    }


    public void putRecipe(Map<String,String> header, String id, final HttpCallback callback){

        apiService.putRecipe(header, id).enqueue(new Callback<RecipeGet>() {
            @Override
            public void onResponse(Call<RecipeGet> call, Response<RecipeGet> response) {
                System.out.println("onResponse@@@@@@ ON ConnectUtil putRecipe@@@@@@@@");
                if (response.isSuccessful()) {
                    System.out.println("onResponse OK and Success on putRecipe @@@@@@@@");
                    callback.onSuccess(response.code(), response.body());
                } else {
                    System.out.println("onResponse OK But Failure on putRecipe @@@@@@@@@@@@");
                    APIError error = ErrorUtil.parseError(response);
                    System.out.println("Error message : " + error.message());
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<RecipeGet> call, Throwable t) {
                System.out.println("onFailure@@@@@@ ConnectUtil on putRecipe @@@@@@@@");
                callback.onError(t);
            }
        });
    }

    public void deleteRecipe(Map<String,String> header, String id, final HttpCallback callback){

        apiService.deleteRecipe(header, id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    System.out.println("onResponse OK and Delete Complete @@@@@@@@");
                    callback.onSuccess(response.code(), response.body());
                } else {
                    System.out.println("onResponse OK But Failure on Delete @@@@@@@@@@@@");
                    APIError error = ErrorUtil.parseError(response);
                    System.out.println("Error message : " + error.message());
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onError(t);
            }
        });
    }


}

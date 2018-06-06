package com.homechef.ict.ict_homechef.ConnectUtil;

import com.homechef.ict.ict_homechef.ConnectUtil.ResponseBody.LoginGet;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HttpApi {

    final String Base_URL = "http://13.124.27.141:80";

    @GET("/posts/{userId}")
    Call<ResponseGet> getFirst(@Path("userId") String id);

    @GET("/posts")
    Call<List<ResponseGet>> getSecond(@Query("userId") String id);

    @FormUrlEncoded
    @POST("/posts")
    Call<LoginGet> postLogin(@FieldMap HashMap<String, String> parameters);

    @PUT("/posts/1")
    Call<ResponseGet> putFirst(@Body RequestPut parameters);

    @FormUrlEncoded
    @PATCH("/posts/1")
    Call<ResponseGet> patchFirst(@Field("title") String title);

    /*
    @DELETE("/posts/1")
    Call<ResponseBody> deleteFirst();
    */

}

package com.homechef.ict.ict_homechef;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/* 사용법

HttpUtil(Base Url 뒤의 주소 ":80/auth/login", loginJsonData, HttpUtilCallback callback).execute();
콜백 : 피호출자 -> 호출자 데이터보내는방식  구글링 + http://devmingsa.tistory.com/14 참고

 */
public class HttpUtil extends AsyncTask<Void, Void, String> {

    private HttpUtilCallback mCallBack;
    private Exception mException;
    private String addUrl;
    private String jsonStr;
    private JSONObject jsonObj;

    public HttpUtil(String str, String jStr, HttpUtilCallback callback) {

        this.mCallBack = callback;
        this.addUrl = str;
        this.jsonStr = jStr;

    }

    @Override
    protected String doInBackground(Void... params) {

        String result; // 요청 결과를 저장할 변수.
        RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
        result = requestHttpURLConnection.request(addUrl, jsonStr); // 해당 URL로 부터 결과물을 얻어온다.

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (mCallBack != null && mException == null){
            mCallBack.onSuccess(result);
        } else {
            mCallBack.onFailure(mException);
        }


    }


    // URL Connection 생성

    private class RequestHttpURLConnection {

        public String request(String _url, String jStr){

            // HttpURLConnection 변수.
            HttpURLConnection urlConn = null;
            OutputStream os = null;
            BufferedReader res = null;
            String line = null;
            StringBuffer resJson = new StringBuffer();


            String baseUrl = "http://13.124.27.141";
            String strUrl = baseUrl + _url;
            try{
                URL url = new URL(strUrl);
                urlConn = (HttpURLConnection) url.openConnection();

                // urlConn 설정.
                urlConn.setRequestMethod("POST"); // URL 요청에 대한 메소드 설정 : POST.
                urlConn.setConnectTimeout(5000);
                urlConn.setDoInput(true);
                urlConn.setDoOutput(true);


                // 서버에 보내기
                os = urlConn.getOutputStream();
                System.out.println("TestCode : job.toString() = "+ jStr);
                os.write(jStr.getBytes());
                os.flush();


                // response 받기
                int responseCode = urlConn.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK) {

                    System.out.println("TestCode2 Connect OK@@@@@@");

                    res = new BufferedReader(
                            new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
                    while ((line = res.readLine()) != null){
                        resJson.append(line);
                    }

                    System.out.println("the String is @@@@ " + resJson.toString());
                    urlConn.disconnect();
                    if(resJson != null) return resJson.toString();
                }

            } catch (MalformedURLException e) { // for URL.
                e.printStackTrace();
                mException = e;
            } catch (IOException e) { // for openConnection().
                e.printStackTrace();
                mException = e;
            } finally {
                if (urlConn != null)
                    urlConn.disconnect();
            }

            System.out.println("I am returning null@@@@@@@@@");
            return null;

        }

    }

    public interface HttpUtilCallback{

        void onSuccess(String result);
        void onFailure(Exception e);

    }

}


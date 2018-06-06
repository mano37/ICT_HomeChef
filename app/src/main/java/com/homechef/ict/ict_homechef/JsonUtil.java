package com.homechef.ict.ict_homechef;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/*

생성자 작업 후에 getJson으로 json object 얻음

생성자작업에서..
String type : Base url 뒤에 붙는 주소, ex) ":80/auth/login"
sArr에는 [0]~[n]까지 서버에 보낼 값이 들어감 API에 있는 순서
현재 Login것밖에 없음 필요한것 추가 ㄱ
직접 json 만들어도 상관 x

 */

public class JsonUtil {

    private Context ctx;
    private JSONObject json;
    private JSONObject stoj;
    private String[] sArr;
    private String jStr;



    public JsonUtil(String type,String[] sArr){

        json = new JSONObject();

        if(type.equals(":80/auth/login")) {

            try {
                json.put("auth_code", sArr[0]);
                json.put("auth_type", sArr[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getJsonStr() {
        jStr = json.toString().replace("\\","");
        return jStr;
    }

    public String[] getsArr(String type,String s){

        try {
            stoj = new JSONObject(s);
            if(type.equals(ctx.getString(R.string.login_url))){

                sArr = new String[6];
                sArr[0] = stoj.getString("picture").replace("\\","");
                sArr[1] = stoj.getString("name");
                sArr[2] = stoj.getString("jwt_token");
                sArr[3] = stoj.getString("email");
                sArr[4] = stoj.getString("id");
                sArr[5] = stoj.getString("auth_type");

                return sArr;
            }
            else if(type.equals(ctx.getString(R.string.session_url))){

                sArr = new String[8];
                sArr[0] = stoj.getString("user_id");
                sArr[1] = stoj.getString("email");
                sArr[2] = stoj.getString("name");
                sArr[4] = stoj.getString("picture").replace("\\", "");
                sArr[5] = stoj.getString("auth_id").replace("\\", "");
                sArr[6] = stoj.getString("auth_type");
                sArr[7] = stoj.getString("jwt_token");
                sArr[8] = stoj.getString("created_at");

                return sArr;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }
}

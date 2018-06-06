package com.homechef.ict.ict_homechef.ConnectUtil.RequestBody;

import java.util.HashMap;

public class LoginPut {

    public final String auth_code;
    public final String auth_type;

    public LoginPut(HashMap<String, String> parameters) {
        this.auth_code = (String) parameters.get("auth_code");
        this.auth_type = (String) parameters.get("auth_type");
    }

}

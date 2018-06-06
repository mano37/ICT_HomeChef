package com.homechef.ict.ict_homechef.ConnectUtil;

import java.util.HashMap;

public class RequestPut {

    public final String auth_code;
    public final String auth_type;

    public RequestPut(HashMap<String, String> parameters) {
        this.auth_code = (String) parameters.get("auth_code");
        this.auth_type = (String) parameters.get("auth_code");
    }


}

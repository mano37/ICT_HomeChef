package com.homechef.ict.ict_homechef.ConnectUtil.ResponseBody;

public class LoginGet {

    public final int user_id;
    public final String email;
    public final String name;
    public final String picture;
    public final String auth_id;
    public final String auth_type;
    public final String jwt_token;
    public final String created_at;

    public LoginGet(int user_id, String email, String name, String picture, String auth_id, String auth_type, String jwt_token, String created_at){

        this.user_id = user_id;
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.auth_id = auth_id;
        this.auth_type = auth_type;
        this.jwt_token = jwt_token;
        this.created_at = created_at;

    }


}

package com.homechef.ict.ict_homechef.ConnectUtil.RequestBody;

import java.util.Map;

public class RecipePut {

    public final String title;
    public final String serve;
    public final String time_cost;
    public final String ingre_count;
    public final String steps;
    public final String image_url;

    public RecipePut(Map<String,String> parameters) {

        this.title = parameters.get("title");
        this.serve = parameters.get("serve");
        this.time_cost = parameters.get("time_cost");
        this.ingre_count = parameters.get("ingre_count");
        this.steps = parameters.get("steps");
        this.image_url = parameters.get("image_url");

    }
}

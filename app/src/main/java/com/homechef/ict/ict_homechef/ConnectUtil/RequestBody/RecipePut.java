package com.homechef.ict.ict_homechef.ConnectUtil.RequestBody;

import java.util.Map;

public class RecipePut {

    public final String title;
    public final String serve;
    public final String time_cost;
    public final Map<String,String> ingre_count;
    public final String steps;
    public final String image_url;

    public RecipePut(RecipePut parameters) {

        this.title = parameters.title;
        this.serve = parameters.serve;
        this.time_cost = parameters.time_cost;
        this.ingre_count = parameters.ingre_count;
        this.steps = parameters.steps;
        this.image_url = parameters.image_url;

    }
}

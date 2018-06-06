package com.homechef.ict.ict_homechef.ConnectUtil.RequestBody;

public class RecipePut {

    public final String title;
    public final String serve;
    public final String time_cost;
    public final String ingre_count;
    public final String steps;
    public final String image_url;

    public RecipePut(String title, String serve, String time_cost,
                     String ingre_count, String steps, String image_url) {

        this.title = title;
        this.serve = serve;
        this.time_cost = time_cost;
        this.ingre_count = ingre_count;
        this.steps = steps;
        this.image_url = image_url;

    }
}

package com.homechef.ict.ict_homechef.ConnectUtil.ResponseBody;

import java.util.HashMap;
import java.util.Map;

public class RecipeGet {

    public int recipe_id;
    public String title;
    public String serve;
    public Map<String, String> ingre_count = new HashMap<>();
    public String steps;
    public String time_cost;
    public String image_url;
    public int author_id;
    public int recommend_count;
    public String created_at;
    public String updated_at;
    public String author_name;

    public RecipeGet(int recipe_id, String title, String serve, Map<String, String> ingre_count,
                      String steps, String time_cost, String image_url, int author_id,
                      int recommend_count, String created_at, String updated_at,
                     String author_name) {

        this.recipe_id = recipe_id;
        this.title = title;
        this.serve = serve;
        this.ingre_count = ingre_count;
        this.steps = steps;
        this.time_cost = time_cost;
        this.image_url = image_url;
        this.author_id = author_id;
        this.recommend_count = recommend_count;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.author_name = author_name;

    }
}

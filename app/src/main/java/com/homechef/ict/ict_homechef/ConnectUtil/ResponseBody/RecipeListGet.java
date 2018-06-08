package com.homechef.ict.ict_homechef.ConnectUtil.ResponseBody;

import java.util.HashMap;
import java.util.Map;

public class RecipeListGet {

    public int recipe_id;
    public String title;
    public String image_url;
    public Map<String,String> ingre_count = new HashMap<>();
    public String created_at;
    public String recommend_count;
    public String author_name;
    public int author_id;
    
    public RecipeListGet(int recipe_id, String title, String image_url, Map<String,String> ingre_count,
                         String created_at, String recommend_count, String author_name,
                         int author_id){

        this.recipe_id = recipe_id;
        this.title = title;
        this.image_url = image_url;
        this.ingre_count = ingre_count;
        this.created_at = created_at;
        this.recommend_count = recommend_count;
        this.author_name = author_name;
        this.author_id = author_id;

    }



}

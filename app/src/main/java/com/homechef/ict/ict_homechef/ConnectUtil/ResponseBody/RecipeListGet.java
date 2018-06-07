package com.homechef.ict.ict_homechef.ConnectUtil.ResponseBody;

public class RecipeListGet {

    String id;
    String title;
    String ingre_count;
    String author_name;
    String created_at;
    String recommend_count;

    public RecipeListGet(String id, String title, String ingre_count, String author_name,
                         String created_at, String recommend_count){

        this.id = id;
        this.title = title;
        this.ingre_count = ingre_count;
        this.author_name = author_name;
        this.created_at = created_at;
        this.recommend_count = recommend_count;

    }



}

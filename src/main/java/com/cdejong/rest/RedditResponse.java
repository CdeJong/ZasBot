package com.cdejong.rest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RedditResponse {
    public Data data;

    public class Data {
        public List<Post> children;

        public String id;

        public String title;

        @SerializedName("subreddit_name_prefixed")
        public String subRedditName;

        public String author;

        public int score;

        public String permalink;

        public String url;
    }

    public class Post {
        public Data data;
    }
}






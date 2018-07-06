package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Tweet {
    // list out the attributes
    public String body;
    public long uid;
    public User user;
    public String createdAt;
    public String handle;
    public String retweet_count;
    public String like_count;

    // deserialize the JSOM
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException{
        Tweet tweet = new Tweet();

        // extract the values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.handle = tweet.user.screenName;
        tweet.retweet_count =jsonObject.getString("retweet_count");
        tweet.like_count =jsonObject.getString("favorite_count");
        return tweet;
    }
}

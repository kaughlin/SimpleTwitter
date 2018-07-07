package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.ReplyToTweet;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class TwitterDetailActivity extends AppCompatActivity {

    Tweet tweet;
    private List<Tweet> mTweets;

    public ImageView ivProfileImage;
    public TextView tvUsername;
    public TextView tvBody;
    public TextView tvTweetTime;
    public TextView tvHandle;
    public ImageButton ibReply;
    public TextView tvNumRetweets;
    public TextView tvNumLikes;
    public ImageButton ibLikeButton;
    public ImageButton ibRetweetButton;

    public TwitterClient actionClient;
    public AsyncHttpResponseHandler actionHandler;


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_detail);

        // resolve the view objects
        //tvTitle = (TextView) findViewById(R.id.tvTitle);
        //tvOverview = (TextView) findViewById(R.id.tvOverview);
        //tvOverview.setMovementMethod(new ScrollingMovementMethod());
        //rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
            ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) findViewById(R.id.tvUserName);
            tvBody = (TextView) findViewById(R.id.tvBody);
            tvTweetTime = (TextView) findViewById(R.id.tvTweetTime);
            tvHandle = (TextView) findViewById(R.id.tvHandle);
            ibReply = (ImageButton) findViewById(R.id.ibReply);
            tvNumLikes = (TextView) findViewById(R.id.tvNumLikes);
            tvNumRetweets = (TextView) findViewById(R.id.tvNumRetweets);
            ibLikeButton = (ImageButton) findViewById(R.id.ibLikeButton);
            ibRetweetButton = (ImageButton) findViewById(R.id.ibRetweetButton);


        // unwrap the movie passed in via intent, using its simple name as a key
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        //Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        tvUsername.setText(tweet.user.name);
        tvBody.setText(tweet.body);
        tvTweetTime.setText(getRelativeTimeAgo(tweet.createdAt));
        tvHandle.setText("@" + tweet.handle);
        tvNumLikes.setText(tweet.like_count);
        tvNumRetweets.setText(tweet.retweet_count);


            // load image using glide
            GlideApp.with(this)
                    .load(tweet.user.profileImageUrl)
                    .transform(new RoundedCornersTransformation(75,0 ))
                    .into(ivProfileImage);


            actionClient = TwitterApp.getRestClient(this);
            actionHandler = new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        tweet = Tweet.fromJSON(response);
                        Intent i = new Intent(TwitterDetailActivity.this, TimelineActivity.class);
                        i.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                        setResult(2, i);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

        // set the title and overview
        //ivProfileImage.(movie.getTitle());
        //tvUsername
        //tvOverview.setText(movie.getOverview());


    }
    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
    //TODO fix this because this regular intent does not work

    public void goToReply(View view){

        Intent i = new Intent(this, ReplyToTweet.class);
        i.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
        startActivity(i);
    }

    public void onLike(View view){
        long id = tweet.uid;
        actionClient.likeTweet(id, actionHandler);
        Toast.makeText(this,"Like Complete", Toast.LENGTH_LONG ).show();

    }
    public void onRetweet(View view){
        long id = tweet.uid;
        actionClient.retweetTweet(id, actionHandler);
        Toast.makeText(this,"Retweet Complete", Toast.LENGTH_LONG ).show();

    }


}

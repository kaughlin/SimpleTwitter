package com.codepath.apps.restclienttemplate.models;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TimelineActivity;
import com.codepath.apps.restclienttemplate.TweetAdapter;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ReplyToTweet extends AppCompatActivity {
    EditText textField;
    public AsyncHttpResponseHandler handler;
    public TwitterClient client;
    private Tweet tweet;
    private Tweet tweet_info;
    private TextView tvCount;
    private EditText tvHandle;


    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;

    // count text method
    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            tvCount.setText(String.valueOf(s.length())+ " /280");
        }

        public void afterTextChanged(Editable s) {
        }
    };
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tool, menu);
        return true;
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_to_tweet);



        //tvHandle =  findViewById(R.id.etUserTweetReply);
        textField =  findViewById(R.id.etUserTweetReply);
        client = TwitterApp.getRestClient(this);
        tvCount = (TextView) findViewById(R.id.tvReplyCounter);
        handler = new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    tweet_info = Tweet.fromJSON(response);
                    Intent i = new Intent(ReplyToTweet.this, TimelineActivity.class);
                    i.putExtra("tweet", Parcels.wrap(tweet_info));
                    setResult( 15, i);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        textField.addTextChangedListener(mTextEditorWatcher);

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        textField.setText("@" + tweet.handle);
    }



    public void sendReply(View view){
        String text = textField.getText().toString();
        client.sendTweet(text, handler);
    }


    public void xbutton(View view){
        Intent i = new Intent(this, TimelineActivity.class);
        startActivity(i);
    }
}

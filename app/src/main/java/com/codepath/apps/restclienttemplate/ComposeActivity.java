package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;


public class ComposeActivity extends AppCompatActivity {
    EditText textField;
    public AsyncHttpResponseHandler handler;
    public TwitterClient client;
    private Tweet tweet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        textField = (EditText) findViewById(R.id.etUserTweet);
        client = TwitterApp.getRestClient(this);
        handler = new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    tweet = Tweet.fromJSON(response);
                    Intent i = new Intent(ComposeActivity.this, TimelineActivity.class);
                    i.putExtra("tweet", Parcels.wrap(tweet));
                    setResult( 19, i);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

    }

    public void sendtw(View view){
        String text = textField.getText().toString();
        client.sendTweet(text, handler);
    }
    public void xbutton(View view){
        Intent i = new Intent(this, TimelineActivity.class);
        startActivity(i);
    }
}

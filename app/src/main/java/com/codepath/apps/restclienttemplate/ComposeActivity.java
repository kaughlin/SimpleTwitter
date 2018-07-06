package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
    private TextView tvCount;

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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        textField = (EditText) findViewById(R.id.etUserTweet);
        client = TwitterApp.getRestClient(this);
        tvCount = (TextView) findViewById(R.id.tvCounter);
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
        textField.addTextChangedListener(mTextEditorWatcher);

        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setLogo(R.drawable.vector_compose_shortcut);
        //getSupportActionBar().setDisplayUseLogoEnabled(true);


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

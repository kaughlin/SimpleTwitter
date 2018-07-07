package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{

    private final Handlers updatehandler;
    private List<Tweet> mTweets;
    Context context;
    //Tweet tweet;
    public TwitterClient actionClient = TwitterApp.getRestClient(context);
    public AsyncHttpResponseHandler actionHandler = new JsonHttpResponseHandler();

    // pass in the tweets array in the constructor

    interface Handlers{
        void onItemClicked(Tweet tw, Context co);
    }

    public TweetAdapter(List<Tweet> tweets, Handlers handlers){ mTweets = tweets; updatehandler = handlers;}
    // for each row, inflate the layout and cache references into ViewHolder

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;

    }


    // bind the values based on the position of the element

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        // get the data according to position
        final Tweet tweet = mTweets.get(position);
        // populate the views according to this data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvTweetTime.setText(getRelativeTimeAgo(tweet.createdAt));
        holder.tvHandle.setText("@" +tweet.handle); // display tweet handle
        holder.tvNumRetweets.setText(tweet.retweet_count);
        holder.tvNumLikes.setText(tweet.like_count);
        holder.ibReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Tweet whotweeted = mTweets.get(position);
                updatehandler.onItemClicked(whotweeted, context);


                /*
                Tweet whotweeted = mTweets.get(position);
                Intent intent = new Intent(context, ReplyToTweet.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(whotweeted));

                context.startActivity(intent);
                */

            }
        });

        holder.ibRetweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                actionClient.retweetTweet(mTweets.get(position).uid, actionHandler);
                Toast.makeText(context, "Retweet Complete", Toast.LENGTH_LONG).show();
            }
        });
        holder.ibLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                actionClient.likeTweet(mTweets.get(position).uid, actionHandler);
                Toast.makeText(context, "Like Complete", Toast.LENGTH_LONG).show();
            }
        });






        GlideApp.with(context)
                .load(tweet.user.profileImageUrl)
                .transform(new RoundedCornersTransformation(75,0 ))
                .into(holder.ivProfileImage);
    }

    @Override
    public int getItemCount() { return mTweets.size(); }

    // create ViewHolder class

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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

        public ViewHolder(View itemView){
            super(itemView);

            // perform findViewById lookups

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvTweetTime = (TextView) itemView.findViewById(R.id.tvTweetTime);
            tvHandle = (TextView) itemView.findViewById(R.id.tvHandle);
            ibReply = (ImageButton) itemView.findViewById(R.id.ibReply);
            tvNumLikes = (TextView) itemView.findViewById(R.id.tvNumLikes);
            tvNumRetweets = (TextView) itemView.findViewById(R.id.tvNumRetweets);
            ibLikeButton = (ImageButton) itemView.findViewById(R.id.ibLikeButton);
            ibRetweetButton = (ImageButton) itemView.findViewById(R.id.ibRetweetButton);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Tweet whotweet = mTweets.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, TwitterDetailActivity.class);//
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(whotweet));

                // show the activity
                context.startActivity(intent);
            }
        }
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

    /* Within the RecyclerView.Adapter class */

    // Clean all elements of the recycler
    public  void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }


}

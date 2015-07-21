package com.Feebee.dao;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterDAO implements IFeedDAO {

	   public JSONArray fetch_feed(){
	        // gets Twitter instance with default credentials
	        Twitter twitter = new TwitterFactory().getInstance();

            JSONObject json = null;
            JSONArray out = new JSONArray();
            
	        try {
	            ConfigurationBuilder cb = new ConfigurationBuilder();
	            cb.setDebugEnabled(true)
	              .setOAuthConsumerKey("CONSUMER_KEY")
	              .setOAuthConsumerSecret("CONSUMER_SECRET")
	              .setOAuthAccessToken("ACCESS_TOKEN")
	              .setOAuthAccessTokenSecret("ACCESS_TOKEN_SECRET");
	            TwitterFactory tf = new TwitterFactory(cb.build());
	            Twitter twitter1 = tf.getInstance();
	            List<Status> statuses;
	            
	            String user1 = "ZeratoRSC2";
                statuses = twitter1.getUserTimeline(user1);
                try {
		            for (Status status : statuses) {
		                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
		                json = new JSONObject();
		                json.put("author", status.getUser().getScreenName());
		                json.put("text", status.getText());
		                json.put("id", status.getId());
		                json.put("rt", status.isRetweet());
						
		                out.put(json);
		            }
	            } catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            
	            String user2 = "lamortlavraie";
	            statuses = twitter1.getUserTimeline(user2);
                try {
		            for (Status status : statuses) {
		                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
		                json = new JSONObject();
		                json.put("author", status.getUser().getScreenName());
		                json.put("text", status.getText());
		                json.put("id", status.getId());
		                json.put("rt", status.isRetweet());
						
		                out.put(json);
		            }
	            } catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
	        } catch (TwitterException te) {
	            te.printStackTrace();
	            System.out.println("Failed to get timeline: " + te.getMessage());
	            System.exit(-1);
	        }
			return out;
	        
	    }
	

}

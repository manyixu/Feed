package com.Feebee.business;

import org.json.JSONArray;
import org.json.JSONObject;

import com.Feebee.model.ServerException;

public class FeedManager implements IFeedManager {

	public JSONArray fetch_youtube_feed(String username) {
		
		YoutubeManager ytm = new YoutubeManager();
		JSONArray outputJson = null;
		try {
			outputJson = ytm.fetch_feed();
		} catch (ServerException e) {
			System.err.println(e.getMessage());
		};
		
		//TwitterManager twm = new TwitterManager();
		//twm.fetch_feed();

		return outputJson;
	}
	
	public JSONArray fetch_twitter_feed(String username) {
		
		TwitterManager twm = new TwitterManager();
		JSONArray outputJson = twm.fetch_feed();

		return outputJson;
	}

	
}

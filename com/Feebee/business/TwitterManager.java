package com.Feebee.business;

import org.json.JSONArray;

import com.Feebee.dao.TwitterDAO;

public class TwitterManager implements IFeedManager {

	public JSONArray fetch_feed() {

		TwitterDAO dao = new TwitterDAO();

		JSONArray res = dao.fetch_feed();
		
		return res;
	}

}

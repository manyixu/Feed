package com.Feebee.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.Feebee.dao.YoutubeDAO;
import com.Feebee.model.ServerException;
import com.Feebee.model.YoutubeItem;

public class YoutubeManager implements IFeedManager {

	public JSONArray fetch_feed() throws ServerException{
		
		YoutubeDAO dao = new YoutubeDAO();
		
		List<String> channels_ids = new ArrayList<String>();
		channels_ids.add("UCLVm_5eaipV6rKNzbSkwuUA");
		channels_ids.add("UCZ_oIYI9ZNpOfWbpZxWNuRQ");
		channels_ids.add("UC-lHJZR3Gqxm24_Vd_AJ5Yw");
		
		JSONObject response_c = new JSONObject();
		try {
			response_c = dao.fetch_channels(channels_ids);
		} catch (ServerException e) {
			System.err.println(e.getMessage());
			throw new ServerException("ERROR RETRIEVING CHANNELS DATA FROM YOUTUBE");
		}
		
		JSONArray ch_items = null;
		try {
			ch_items = response_c.getJSONArray("items");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<String> playlists_ids = new ArrayList<String>();
		for(int i = 0; i < ch_items.length(); i++){
			try {
				playlists_ids.add(ch_items.getJSONObject(i).
						getJSONObject("contentDetails").
						getJSONObject("relatedPlaylists").
						getString("uploads"));
			} catch (JSONException e) {
				System.err.println(e.getMessage());
				throw new ServerException("ERROR PARSING JSON TO RETRIEVE CHANNELS DATA");
			}
		}
		
		JSONArray response_p = new JSONArray();
		try {
			response_p = dao.fetch_playlists(playlists_ids);
		} catch (ServerException e) {
			System.err.println(e.getMessage());
			throw new ServerException("ERROR RETRIEVING PLAYLISTS DATA FROM YOUTUBE");
		}
		
		List<String> videos_ids = new ArrayList<String>();
		JSONArray pl_items = null;
		for(int i = 0; i < response_p.length(); i++){

			try {
				
				pl_items = response_p.getJSONObject(i).getJSONArray("items");
				for(int j = 0; j < pl_items.length(); j++){

					try {
						videos_ids.add(pl_items.getJSONObject(j).
								getJSONObject("contentDetails").
								getString("videoId"));
					} catch (JSONException e) {
						System.err.println(e.getMessage());
						throw new ServerException("ERROR PARSING JSON TO RETRIEVE CHANNELS DATA");
					}
				}
			} catch (JSONException e) {
				System.err.println(e.getMessage());
				throw new ServerException("ERROR PARSING JSON TO RETRIEVE PLAYLISTS DATA");
			}
				
		}
		
		JSONArray response_v = new JSONArray();
		try {
			response_v = dao.fetch_videos(videos_ids);
		} catch (ServerException e) {
			System.err.println(e.getMessage());
			throw new ServerException("ERROR RETRIEVING VIDEOS DATA FROM YOUTUBE");
		}
		
		List<YoutubeItem> videos = new ArrayList<YoutubeItem>();
		YoutubeItem item = null;
		String ti, a, th, da, du, u;
		Date now = new Date();
		Date vd = null;
		SimpleDateFormat inputP = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'");
		Pattern pattern = Pattern.compile("PT((\\d*)H)?((\\d*)M)?((\\d*)S)?");
		Matcher m = pattern.matcher("PT4H8S");
		/*if(m.find()){
			for(int j = 0; j <= m.groupCount(); j++){
				System.out.println("|"+m.group(j)+"|");
			}
		}*/
		
		
		
		for(int i = 0; i < response_v.length(); i++){
			
			try {
				
				da = response_v.getJSONObject(i)
						.getJSONObject("snippet")
						.getString("publishedAt");
				try {
					vd = inputP.parse(da);
				} catch (ParseException e) {
					System.err.println(e.getMessage());
					throw new ServerException("ERROR PARSING DATE TO RETRIEVE VIDEOS DATA");
				}
				
				if(now.getTime() - vd.getTime() < 604800000){
					
					ti = response_v.getJSONObject(i)
							.getJSONObject("snippet")
							.getString("title");
					a = response_v.getJSONObject(i)
							.getJSONObject("snippet")
							.getString("channelTitle");
					th = response_v.getJSONObject(i)
							.getJSONObject("snippet")
							.getJSONObject("thumbnails")
							.getJSONObject("default")
							.getString("url");
					du = response_v.getJSONObject(i)
							.getJSONObject("contentDetails")
							.getString("duration");
					u = "https://www.youtube.com/watch?v=" + response_v.getJSONObject(i).getString("id");
					
					item = new YoutubeItem();
					item.title = ti;
					item.author = a;
					item.duration = du;
					item.published = vd;
					item.thumbnail = th;
					item.url = u;
					videos.add(item);
				}
				
			} catch (JSONException e) {
				System.err.println(e.getMessage());
				throw new ServerException("ERROR PARSING JSON TO RETRIEVE VIDEOS DATA");
			}
					
		}
		
		if (videos.size() > 0) {
		    Collections.sort(videos, new Comparator<YoutubeItem>() {
		        public int compare(YoutubeItem i1, YoutubeItem i2) {
		        	return (int) (i2.published.getTime() - i1.published.getTime());
				}
		    });
		}

		JSONArray out = new JSONArray();
		JSONObject outItem = null;
		SimpleDateFormat outputP = new SimpleDateFormat("dd-MM-yyyy 'at' HH:mm:ss");
		try {
			for(int i = 0; i < videos.size(); i++){

				outItem = new JSONObject();
				outItem.put("title", videos.get(i).title);
				outItem.put("author", videos.get(i).author);
				outItem.put("duration", videos.get(i).duration);
				outItem.put("thumbnail", videos.get(i).thumbnail);
				outItem.put("url", videos.get(i).url);
				outItem.put("published", outputP.format(videos.get(i).published));
				
				out.put(outItem);
			}
		} catch (JSONException e) {
			System.err.println(e.getMessage());
			throw new ServerException("ERROR FORMING OUTPUT JSON TO RETRIEVE YOUTUBE FEED");
		}

		return out;
	}
}

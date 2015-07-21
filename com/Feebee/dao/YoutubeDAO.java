package com.Feebee.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.Feebee.model.ServerException;



public class YoutubeDAO implements IFeedDAO {

	private String channels_url = "https://www.googleapis.com/youtube/v3/channels";
	private String playlists_url = "https://www.googleapis.com/youtube/v3/playlistItems";
	private String videos_url = "https://www.googleapis.com/youtube/v3/videos";
	
	private String api_key = "API_KEY";
	
	private String user_agent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.89 Safari/537.36";
	
	/**
	 * 
	 * Retrieve channels information in order to get their upload playlists ids
	 * 
	 * @param channels_ids List of channels ids to retrieve videos from
	 * @return JSONObject response from Youtube to be processed
	 * @throws ServerException
	 */
	public JSONObject fetch_channels(List<String> channels_ids) throws ServerException{
		
		String functionName = "YtDAO.fetch_channels()";
		
		if(channels_ids.size() == 0)
			throw new ServerException("BAD USAGE : NO CHANNEL ID PASSED - " + functionName);
		
		// Building query url;
		String url = channels_url + "?"
				+ "part=contentDetails"
				+ "&id=" + channels_ids.get(0);
		for(int i = 1; i < channels_ids.size(); i++){
			url += "," + channels_ids.get(i);
		}
		url += "&key=" + api_key;
		
		// Parsing query url
		URL urlObject = null;
		try {
			urlObject = new URL(url);
		} catch (MalformedURLException e) {
			System.err.println(e.getMessage());
			throw new ServerException("ERROR FORMING URL - " + functionName);
		}
		
		// Building connection object
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) urlObject.openConnection();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			throw new ServerException("ERROR OPENING CONNECTION - " + functionName);
		}
		// Specifying used method
		try {
			connection.setRequestMethod("GET");
		} catch (ProtocolException e) {
			System.err.println(e.getMessage());
			throw new ServerException("ERROR INITIALIZING REQUEST -" + functionName);
		}
		// ??
		connection.setRequestProperty("User_Agent", user_agent);
		
		// Retrieving response code
		// if not 200 (OK), exit function
		int responseCode = -1;
		try {
			responseCode = connection.getResponseCode();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			throw new ServerException("ERROR GETTING RESPONSE CODE - " + functionName);
		}
		
		if(responseCode != 200){
			throw new ServerException("ERROR FETCHING YOUTUBE DATA (ERROR " + responseCode + ") - " + functionName);
		}
		
		// Retrieving input stream and request's response
		BufferedReader in = null;
		try {
			in = new BufferedReader( new InputStreamReader(connection.getInputStream()) );
		} catch (IOException e) {
			System.err.println(e.getMessage());
			throw new ServerException("ERROR GETTING RESPONSE STREAM - " + functionName);
		}

		String inputLine;
		StringBuffer response = new StringBuffer();

		try {
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
			throw new ServerException("ERROR READING RESPONSE - " + functionName);
		}
	
		// Closing connection
		try {
			in.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			throw new ServerException("ERROR CLOSING CONNECTION - " + functionName);
		}

		// Parsing response to form a JSONObject
		JSONObject out = new JSONObject();
		try {
			out = new JSONObject(response.toString());
		} catch (JSONException e) {
			System.err.println(e.getMessage());
			throw new ServerException("ERROR PARSING JSON - " + functionName);
		}

		return out;
	}

	/**
	 * 
	 * @param playlists_ids List of upload playlists to retrieve videos from
	 * @return JSONObject response from Youtube to be processed
	 * @throws ServerException
	 */
	public JSONArray fetch_playlists(List<String> playlists_ids) throws ServerException{
		
		String functionName = "YtDAO.fetch_playlists()";
		
		if(playlists_ids.size() == 0)
			throw new ServerException("BAD USAGE : NO PLAYLIST ID PASSED - " + functionName);
		
		JSONArray out = new JSONArray();
		
		// Declaring variables used for each playlist
		String url = "";
		URL urlObject = null;
		HttpURLConnection connection = null;
		BufferedReader in = null;
		String inputLine = "";
		StringBuffer response = null;
		JSONObject json = null;
		
		// For each id, we retrieve videos ids
		for(String id : playlists_ids){
			
			// Building query url;
			url = playlists_url + "?"
					+ "part=contentDetails"
					+ "&playlistId=" + id
					+ "&key=" + api_key
					+ "&maxResults=10";
			
			// Parsing query url
			try {
				urlObject = new URL(url);
			} catch (MalformedURLException e) {
				System.err.println(e.getMessage());
				throw new ServerException("ERROR FORMING URL - " + functionName);
			}
			
			// Building connection object
			try {
				connection = (HttpURLConnection) urlObject.openConnection();
			} catch (IOException e) {
				System.err.println(e.getMessage());
				throw new ServerException("ERROR OPENING CONNECTION - " + functionName);
			}
			try {
				connection.setRequestMethod("GET");
			} catch (ProtocolException e) {
				System.err.println(e.getMessage());
				throw new ServerException("ERROR INITIALIZING REQUEST -" + functionName);
			}
			connection.setRequestProperty("User_Agent", user_agent);
			
			// Retrieving response code
			// if not 200 (OK), exit function
			int responseCode = -1;
			try {
				responseCode = connection.getResponseCode();
			} catch (IOException e) {
				System.err.println(e.getMessage());
				throw new ServerException("ERROR GETTING RESPONSE CODE - " + functionName);
			}
			
			if(responseCode != 200){
				throw new ServerException("ERROR FETCHING YOUTUBE DATA (ERROR " + responseCode + ") - " + functionName);
			}
			
			// Retrieving input stream and request's response
			try {
				in = new BufferedReader( new InputStreamReader(connection.getInputStream()) );
			} catch (IOException e) {
				System.err.println(e.getMessage());
				throw new ServerException("ERROR GETTING RESPONSE STREAM - " + functionName);
			}

			response = new StringBuffer();

			try {
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
			} catch (IOException e) {
				System.err.println(e.getMessage());
				throw new ServerException("ERROR READING RESPONSE - " + functionName);
			}
		
			// Closing connection
			try {
				in.close();
			} catch (IOException e) {
				System.err.println(e.getMessage());
				throw new ServerException("ERROR CLOSING CONNECTION - " + functionName);
			}
			
			// Parsing response to form a JSONObject
			try {
				json = new JSONObject(response.toString());
			} catch (JSONException e) {
				System.err.println(e.getMessage());
				throw new ServerException("ERROR PARSING JSON - " + functionName);
			}
			
			out.put(json);
			
		}

		return out;
	}
	
	
	/**
	 * 
	 * @param videos_ids List of videos ids to fetch
	 * @return JSONArray data retrived from Youtube to be processed
	 * @throws ServerException
	 */
	public JSONArray fetch_videos(List<String> videos_ids) throws ServerException{
		
		String functionName = "YtDAO.fetch_videos()";
		
		if(videos_ids.size() == 0)
			throw new ServerException("BAD USAGE : NO VIDEO ID PASSED - " + functionName);
		
		JSONArray out = new JSONArray();
		
		// Declaring variables used for each video set
		String url = "";
		URL urlObject = null;
		HttpURLConnection connection = null;
		BufferedReader in = null;
		String inputLine = "";
		StringBuffer response = null;
		JSONObject json = null;
		JSONArray jsona = null;
		
		int iteration = (int) Math.floor(videos_ids.size()/50);
		if(videos_ids.size()%50 > 0) iteration++;
		
		// We iterate over sets of maximum 50 videos ids
		// this is the maximum the Youtube Data API allows
		for(int it = 0; it < iteration; it++){
			
			// Building query url;
			int v = Math.min(50*(it+1), videos_ids.size()); 
			url = videos_url + "?"
					+ "part=snippet,contentDetails"
					+ "&maxResults=50"
					+ "&id=" + videos_ids.get(50*it);
			for(int i = 1; i < v; i++){
				url += "," + videos_ids.get(i);
			}
			url += "&key=" + api_key;
			
			
			
			// Parsing query url
			try {
				urlObject = new URL(url);
			} catch (MalformedURLException e) {
				System.err.println(e.getMessage());
				throw new ServerException("ERROR FORMING URL - " + functionName);
			}
			
			// Building connection object
			try {
				connection = (HttpURLConnection) urlObject.openConnection();
			} catch (IOException e) {
				System.err.println(e.getMessage());
				throw new ServerException("ERROR OPENING CONNECTION - " + functionName);
			}
			try {
				connection.setRequestMethod("GET");
			} catch (ProtocolException e) {
				System.err.println(e.getMessage());
				throw new ServerException("ERROR INITIALIZING REQUEST -" + functionName);
			}
			connection.setRequestProperty("User_Agent", user_agent);
			
			// Retrieving response code
			// if not 200 (OK), exit function
			int responseCode = -1;
			try {
				responseCode = connection.getResponseCode();
			} catch (IOException e) {
				System.err.println(e.getMessage());
				throw new ServerException("ERROR GETTING RESPONSE CODE - " + functionName);
			}
			
			if(responseCode != 200){
				throw new ServerException("ERROR FETCHING YOUTUBE DATA (ERROR " + responseCode + ") - " + functionName);
			}
			
			// Retrieving input stream and request's response
			try {
				in = new BufferedReader( new InputStreamReader(connection.getInputStream()) );
			} catch (IOException e) {
				System.err.println(e.getMessage());
				throw new ServerException("ERROR GETTING RESPONSE STREAM - " + functionName);
			}

			response = new StringBuffer();

			try {
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
			} catch (IOException e) {
				System.err.println(e.getMessage());
				throw new ServerException("ERROR READING RESPONSE - " + functionName);
			}
		
			// Closing connection
			try {
				in.close();
			} catch (IOException e) {
				System.err.println(e.getMessage());
				throw new ServerException("ERROR CLOSING CONNECTION - " + functionName);
			}
			
			// Parsing response to form a JSONObject
			try {
				json = new JSONObject(response.toString());
				
				jsona = json.getJSONArray("items");
				for(int i = 0; i < jsona.length(); i++){
					out.put(jsona.getJSONObject(i));
				}
				
			} catch (JSONException e) {
				System.err.println(e.getMessage());
				throw new ServerException("ERROR PARSING JSON - " + functionName);
			}
			
		}
		
		return out;
		
	}
	
}

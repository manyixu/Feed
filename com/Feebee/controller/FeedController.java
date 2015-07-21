package com.Feebee.controller;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import com.Feebee.business.FeedManager;

@Path("{username}/feed")
public class FeedController {
	
	@GET
	@Path("hello")
	public Response sayHello(@PathParam("username") String username){
		
		String out = "Hello " + username + " !";
		
		return Response.ok(out, MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("youtube")
	public Response fetchYoutubeFeed(@PathParam("username") String username){
		
		FeedManager m = new FeedManager();
		
		JSONArray res = m.fetch_youtube_feed(username);

		String out = res.toString();
		
		return Response.ok(out, MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("twitter")
	public Response fetchTwitterFeed(@PathParam("username") String username){
		
		FeedManager m = new FeedManager();
		
		JSONArray res = m.fetch_twitter_feed(username);

		String out = res.toString();
		
		return Response.ok(out, MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("add_feed")
	public Response addFeed(@PathParam("username") String username){
		
		String out = "add_feed";
		
		return Response.ok(out, MediaType.APPLICATION_JSON).build();
	}
}

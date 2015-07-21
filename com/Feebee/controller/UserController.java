package com.Feebee.controller;

import java.util.HashMap;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.Feebee.business.UserManager;

public class UserController {

	@GET
	@Path("profile")
	public Response fetchProfile(@PathParam("username") String username){
		
		UserManager m = new UserManager();
		String out = m.fetchProfile(username);
		
		return Response.ok(out, MediaType.APPLICATION_JSON).build();
	}
	
	@POST
	@Path("update")
	public Response updateProfile(@PathParam("username") String username,
			@FormParam("info") String name,
			@FormParam("info") String age){
		
		UserManager m = new UserManager();
		HashMap<String, String> infos = new HashMap<String, String>();
		// TODO fill HashMap with fields
		infos.put("name", name);
		infos.put("age", age);
		boolean out = m.updateProfile(username, infos);
		
		return Response.ok(out).build();
	}
}

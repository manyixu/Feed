package com.Feebee.controller;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.Feebee.business.AuthManager;

@Path("/auth/{usernamed}")
public class AuthController {

	@POST
	@Path("auth")
	public Response authenticate(@PathParam("username") String username, @FormParam("pwd") String password){

		AuthManager m = new AuthManager();
		boolean res = m.authenticate(username, password);
		
		return Response.ok(res).build();
	}
	
	public Response updatePassword(@PathParam("username") String username, @FormParam("pwd") String password){
	
		AuthManager m = new AuthManager();
		boolean res = m.updatePassword(username, password);
		
		return Response.ok(res).build();
	}
}

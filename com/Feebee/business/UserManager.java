package com.Feebee.business;

import java.util.HashMap;

import com.Feebee.dao.UserDAO;

public class UserManager {

	public String fetchProfile(String username) {

		UserDAO dao = new UserDAO();
		String res = dao.fetchProfile(username);
		
		// TODO fetch data from database and create json
		
		return res;
	}
	
	public boolean updateProfile(String username, HashMap<String, String> info){
		
		UserDAO dao = new UserDAO();
		boolean res = dao.updateProfile(username, info);
		
		// TODO fetch data from database and create json
		
		return res;
	}

}

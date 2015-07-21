package com.Feebee.dao;

import java.util.HashMap;

public interface IDatabaseDAO {

	public boolean updateProfile(HashMap<String, String> fields);
	
	public boolean changePassword(String newPwd);
	
	public String fetchProfile(String username);
	
	public boolean addFeed(HashMap<String, String> params);
}

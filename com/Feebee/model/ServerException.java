package com.Feebee.model;

public class ServerException extends Exception {
	 
	private static final long serialVersionUID = 5618558449474647011L;
	
	public ServerException() { super(); }
	public ServerException(String message) { super(message); }
	public ServerException(String message, Throwable cause) { super(message, cause); }
	public ServerException(Throwable cause) { super(cause); }
}

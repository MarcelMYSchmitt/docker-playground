package com.schmitt.playground.model;

public class PlayAppMessage {

	private int randomId;
	private String message;
	private String timestamp;

	public int getRandomId() {
		return randomId;
	}

	public void setRandomId(int randomId) {
		this.randomId = randomId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String string) {
		this.timestamp = string;
	}

	@Override
	public String toString() {
		return randomId + ", " + message+ ", " + timestamp + "!";
	}

}

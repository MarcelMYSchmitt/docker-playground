package com.schmitt.playground.model;

public class WatchdogMessage {

	private int id;
	private String message;
	private String timestamp;

	public WatchdogMessage() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
		return id + ", " + message + ", " + timestamp + "!";
	}
}

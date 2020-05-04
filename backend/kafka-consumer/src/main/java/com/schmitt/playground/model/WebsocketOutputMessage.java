package com.schmitt.playground.model;

public class WebsocketOutputMessage {

	private int randomId;
	private String message;
	private String timestamp;

	public WebsocketOutputMessage(final int randomId, final String message, final String timestamp) {
		this.randomId = randomId;
		this.message = message;
		this.timestamp = timestamp;
	}

	public int getRandomId() {
		return randomId;
	}

	public String getMessage() {
		return message;
	}

	public String getTimestamp() {
		return timestamp;
	}
}

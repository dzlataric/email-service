package com.service.email.dto;

public class BasicEmailRequestDTO {

	private String sender;
	private String title;
	private String text;

	public BasicEmailRequestDTO() {
	}

	public BasicEmailRequestDTO(final String sender, final String title, final String text) {
		this.sender = sender;
		this.title = title;
		this.text = text;
	}

	public String getSender() {
		return sender;
	}

	public String getTitle() {
		return title;
	}

	public String getText() {
		return text;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setText(String text) {
		this.text = text;
	}

}

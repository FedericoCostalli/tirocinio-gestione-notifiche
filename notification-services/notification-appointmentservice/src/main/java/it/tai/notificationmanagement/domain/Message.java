package it.tai.notificationmanagement.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Message implements Serializable{

	private static final long serialVersionUID = 1L;
	@JsonProperty("attributes")
	private MessageData data;
	@JsonProperty("id")
	private String id;
	@JsonProperty("type")
	private String type;
}

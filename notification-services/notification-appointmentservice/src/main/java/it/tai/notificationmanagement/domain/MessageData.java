package it.tai.notificationmanagement.domain;

import java.io.Serializable;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MessageData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("appointment-id")
	private Integer appointmentId;
	@JsonProperty("appointment-reference-number")
	private String appointmentReferenceNumber;
	@JsonProperty("channel")
	private String channel;
	@JsonProperty("created-at")
	private Instant createdAt;
	@JsonProperty("customer-full-name")
	private String customerFullName;
	@JsonProperty("customer-id")
	private Integer customerId;
	@JsonProperty("destination")
	private String destination;
	@JsonProperty("status")
	private String status;
	@JsonProperty("title")
	private String title;
	@JsonProperty("type")
	private String type;
}

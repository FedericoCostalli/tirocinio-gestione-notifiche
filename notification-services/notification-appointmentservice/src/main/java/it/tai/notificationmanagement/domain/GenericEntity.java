package it.tai.notificationmanagement.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class GenericEntity implements Serializable{

	private String id;
	
	private String type;
	
	private Map<String, GenericWrapper> relationships;
	
	private Map<String, Object> attributes;
	
	
	
}

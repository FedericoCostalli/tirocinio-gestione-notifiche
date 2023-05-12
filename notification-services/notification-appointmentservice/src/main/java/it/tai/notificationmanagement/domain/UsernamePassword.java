package it.tai.notificationmanagement.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsernamePassword implements Serializable{

	@JsonProperty("email")
	private String email;
	@JsonProperty("password")
	private String password;
}

package it.tai.notificationmanagement.service.notification.whatsapp.support.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextMessage implements Serializable{

	private String content;
	
	private String to;
}

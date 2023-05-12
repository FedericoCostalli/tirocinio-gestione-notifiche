package it.tai.notificationmanagement.service.notification.whatsapp.support.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TextMessageArgument {

	private TextMessage args;
}

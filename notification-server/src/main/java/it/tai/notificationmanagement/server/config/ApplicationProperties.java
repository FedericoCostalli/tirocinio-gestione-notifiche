package it.tai.notificationmanagement.server.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.regions.Region;

@ConfigurationProperties(prefix = "application", ignoreUnknownFields = true)
@Data
public class ApplicationProperties {

	private final FreshaProperties fresha = new FreshaProperties();
	private final DynamoDbProperties dynamodb = new DynamoDbProperties();
	private final GoogleProperties google = new GoogleProperties();
	private final AdminProperties adminProperties = new AdminProperties();
	
	private List<Credential> credentials;
	@Data
	public static class DynamoDbProperties {
		private Region region = Region.EU_WEST_3;
		private String accesskey;
		private String secretkey;

	}
	@Data
	public static class FreshaProperties {
		private String apiUrl = "https://partners-api.fresha.com";
		private String notificationUrl = "https://partners-client-notifications.fresha.com/";
		private String customerUrl = "https://customers-api.fresha.com";
		private String username;
		private String password;
	}

	@Data
	public static class GoogleProperties {
		private String clientSecretFileLocation = "classpath:credentials.json";
	}
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Credential {
		private String username;
		private String password;
	}

	@Data
	public static class AdminProperties{
		private String phonenumber;
		private String apiUrl;
		private String username;
		private String password;
	}

}

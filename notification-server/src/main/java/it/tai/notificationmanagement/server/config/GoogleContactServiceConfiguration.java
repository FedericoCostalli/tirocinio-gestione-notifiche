package it.tai.notificationmanagement.server.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.PeopleServiceScopes;

import it.tai.notificationmanagement.server.config.ApplicationProperties.GoogleProperties;
import it.tai.notificationmanagement.server.support.GoogleCustomDataStoreFactory;
import it.tai.notificationmanagement.service.GoogleContactService;
import lombok.extern.slf4j.Slf4j;
@Configuration
@Slf4j
public class GoogleContactServiceConfiguration {
	
	private static final String APPLICATION_NAME = "Google People API Java Quickstart";
	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	private static final List<String> SCOPES = Arrays.asList(PeopleServiceScopes.CONTACTS);

	
	private final ResourceLoader resourceLoader;
	
	private final GoogleProperties googleProperties;

	public GoogleContactServiceConfiguration(ApplicationProperties applicationProperties, ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
		this.googleProperties = applicationProperties.getGoogle();
	}


	@Bean
	@ConditionalOnProperty(prefix = "application.contact", name = "service", havingValue = "google", matchIfMissing = false)
	public GoogleContactService buildGoogleContactService() throws GeneralSecurityException, IOException {
		log.info("Creating Google Contact Service");
		return new GoogleContactService(buildPeopleService());
	}

	
	private PeopleService buildPeopleService()
			throws GeneralSecurityException, IOException {
		return buildPeopleService(new GoogleCustomDataStoreFactory(
					GoogleContactServiceConfiguration.class.getClassLoader().getResourceAsStream("StoredCredential")));
	}
	
	private PeopleService buildPeopleService(DataStoreFactory dataStore)
			throws GeneralSecurityException, IOException {
		final NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
		return new PeopleService.Builder(transport, JSON_FACTORY, getCredentials(transport, dataStore))
				.setApplicationName(APPLICATION_NAME).build();
	}
	
	private Credential getCredentials(final NetHttpTransport transport, DataStoreFactory dataStore)
			throws IOException {

		var secretFileLocation = googleProperties.getClientSecretFileLocation();
        var resourceFile =  resourceLoader.getResource(secretFileLocation);
		
		if (!resourceFile.exists()) {
			throw new FileNotFoundException("Resource not found: " + secretFileLocation);
		}
		
		var clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
				new InputStreamReader(Objects.requireNonNull(resourceFile.getInputStream())));
		var flow = new GoogleAuthorizationCodeFlow.Builder(transport, JSON_FACTORY,
				clientSecrets, SCOPES).setDataStoreFactory(dataStore).setAccessType("offline").build();
		var receiver = new LocalServerReceiver.Builder().setPort(8080).build();
		return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	}
}

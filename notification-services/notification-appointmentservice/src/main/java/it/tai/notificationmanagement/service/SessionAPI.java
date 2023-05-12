package it.tai.notificationmanagement.service;

import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface SessionAPI {

	@RequestLine("POST /session")
	@Headers("Content-Type: application/vnd.api+json")
	@Body("%7B\"data\":%7B\"type\":\"sign-in-requests\",\"id\":null,\"attributes\":%7B\"email\":\"{email}\",\"password\":\"{password}\"%7D%7D%7D")
	void createSession(@Param("email") String user, @Param("password") String password);
	
}

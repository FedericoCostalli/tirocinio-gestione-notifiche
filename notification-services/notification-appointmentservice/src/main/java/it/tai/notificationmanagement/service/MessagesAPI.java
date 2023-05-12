package it.tai.notificationmanagement.service;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import it.tai.notificationmanagement.domain.MessageWrapper;

public interface MessagesAPI {

	 @RequestLine("GET /messages?page={page}&search_query=")
	 @Headers({
	      "Accept: application/vnd.api+json",
	  })
	 MessageWrapper findAll(@Param("page") Integer page);
}

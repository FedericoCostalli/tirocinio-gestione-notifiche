package it.tai.notificationmanagement.service;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import it.tai.notificationmanagement.domain.GenericWrapper;

public interface CustomerAPI {

	//https://partners-api.fresha.com/customer-search?query=%20&offset=0&limit=100&sort-by=
	 @RequestLine("GET /v2/customer-search?offset={offset}&limit={limit}&query=&genders=&customer-type=&sort-order=asc&sort-by=first-name")
	 @Headers({
	      "Accept: application/vnd.api+json",
	  })
	 GenericWrapper findAll(@Param("offset") Integer offset, @Param("limit") Integer limit);
	 
	 
	 //https://partners-api.fresha.com/customers/46214686?include=customer-payment-method
	 @RequestLine("GET /customers/{id}?include=customer-payment-method")
	 @Headers({
	      "Accept: application/vnd.api+json",
	  })
	 GenericWrapper getById(@Param("id") String id);
}

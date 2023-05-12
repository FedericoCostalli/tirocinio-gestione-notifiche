package it.tai.notificationmanagement.service;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import it.tai.notificationmanagement.domain.GenericWrapper;

public interface BookingAPI {
	
	//https://partners-api.fresha.com/bookings?date-from=2020-05-14&date-to=2020-05-15&location-id=296153&resource-ids=877620%2C877609%2C877610&resource-type=employees
	//@RequestLine("GET /bookings?date-from={date-from}&date-to={date-to}&location-id=296153&resource-ids=877620%2C877609%2C877610&resource-type=employees")
	@RequestLine("GET /bookings?date-from={date-from}&date-to={date-to}&location-id=859896&resource-ids=2182876&resource-type=employees")
	 @Headers({
	      "Accept: application/vnd.api+json",
	  })
	GenericWrapper findAll(@Param("date-from") String from, @Param("date-to") String to);
	
	
	
	
}

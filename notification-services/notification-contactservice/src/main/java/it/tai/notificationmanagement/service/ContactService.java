package it.tai.notificationmanagement.service;

import it.tai.notificationmanagement.domain.Contact;

public interface ContactService {
	  
	   String putContact(Contact contact); 
	  
	   boolean existsContactByPhoneNumber(String phoneNumber);
	  
	   Contact getByPhoneNumber(String phoneNumber);
	   
	   boolean deleteContact(String contactId);
	   
	  
}

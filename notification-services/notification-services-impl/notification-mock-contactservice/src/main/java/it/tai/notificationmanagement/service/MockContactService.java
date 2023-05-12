package it.tai.notificationmanagement.service;

import it.tai.notificationmanagement.domain.Contact;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockContactService implements ContactService {

	@Override
	public String putContact(Contact contact) {
		log.debug("Called putContact with argument {}", contact);
		return null;
	}

	@Override
	public boolean existsContactByPhoneNumber(String phoneNumber) {

		return true;
	}

	@Override
	public Contact getByPhoneNumber(String phoneNumber) {

		return Contact.builder().phoneNumber(phoneNumber).name("Test User").build();
	}

	@Override
	public boolean deleteContact(String contactId) {
		return false;
	}

}

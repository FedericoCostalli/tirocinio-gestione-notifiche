package it.tai.notificationmanagement.service;

import java.io.IOException;

import com.google.api.services.people.v1.model.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.api.services.people.v1.PeopleService;

import it.tai.notificationmanagement.domain.Contact;
import org.powermock.reflect.Whitebox;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GoogleContactServiceTests {

	private ContactService contactService;
   @BeforeAll
	public void initContactService(@Mock PeopleService peopleService, @Mock PeopleService.People people, @Mock PeopleService.People.CreateContact contact) throws Exception {
	   when(peopleService.people()).thenReturn(people);
	   Person person = new Person();
	   person.setResourceName("Test");
	   when(contact.execute()).thenReturn(person);
	   when(people.createContact(any(Person.class))).thenReturn(contact);
	   contactService = new GoogleContactService(peopleService);
	}

	@org.junit.Test(expected = RuntimeException.class)
	public void testInvalidPhoneNumber() {
		var contact = Contact.builder().name("Pippo").phoneNumber("32865965990").build();
		var resourceId = contactService.putContact(contact);

	}

	@Test
	void testPutAccount() {
		var contact = Contact.builder().name("Pippo").phoneNumber("3286596599").build();
		var resourceId = contactService.putContact(contact);
		System.out.println(resourceId);
	}
}




package it.tai.notificationmanagement.service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Name;
import com.google.api.services.people.v1.model.Person;
import com.google.api.services.people.v1.model.PhoneNumber;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberType;

import com.google.i18n.phonenumbers.Phonenumber;
import it.tai.notificationmanagement.domain.Contact;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;


@AllArgsConstructor
public class GoogleContactService implements ContactService {

	private final PeopleService peopleService;

	@Override
	public String putContact(Contact contact) {

		PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
		var phoneNumber = Optional.ofNullable(contact).map(Contact::getPhoneNumber).map(k -> {
			try {
				return phoneNumberUtil.parse(k, "IT");
			} catch (NumberParseException npe) {
				return null;
			}
		}).filter(k -> phoneNumberUtil.isPossibleNumberForType(k, PhoneNumberType.MOBILE))
				.map(number -> phoneNumberUtil.format(number, PhoneNumberFormat.E164))
				.map(k -> new PhoneNumber().setValue(k))
				.orElseThrow(() -> new RuntimeException("Invalid Phone Number !"));
		
		var contactName = Optional.ofNullable(contact).map(Contact::getName).map(String::trim)
				.filter(k -> k.length() > 0).map(k -> new Name().setGivenName(k))
				.orElseThrow(() -> new RuntimeException("Contact name is required!"));

		Person contactToCreate = new Person().setNames(Collections.singletonList(contactName))
				.setPhoneNumbers(Collections.singletonList(phoneNumber));

		try {
			Person createdContact = peopleService.people().createContact(contactToCreate).execute();
			return createdContact.getResourceName();
		} catch (IOException e) {
			throw new RuntimeException("Error Creating Contact " + contact, e);
		}
	}

	@Override
	public boolean existsContactByPhoneNumber(String phoneNumber) {
		PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
		Phonenumber.PhoneNumber current_phone = new Phonenumber.PhoneNumber();
		try {
			current_phone = phoneNumberUtil.parse(phoneNumber, "IT");
		} catch (NumberParseException e) {
			throw new RuntimeException("Invalid Phone Number !");
		}
		String current_phone_string;
		if (phoneNumberUtil.isPossibleNumberForType(current_phone, PhoneNumberType.MOBILE)) {
			current_phone_string = phoneNumberUtil.format(current_phone, PhoneNumberFormat.E164);
		} else {
			throw new RuntimeException("Invalid Phone Number!");
		}

		List<Person> people;
		try {
			ListConnectionsResponse response = peopleService.people().connections().list("people/me")
					.setPersonFields("names,phoneNumbers")
					.execute();
			people = response.getConnections();
		} catch (IOException e) {
			throw new RuntimeException("Error Getting Contacts " + e);
		}
		for (Person person : CollectionUtils.emptyIfNull(people)) {
			for(PhoneNumber phone : CollectionUtils.emptyIfNull(person.getPhoneNumbers())) {
				if(phone.getCanonicalForm().equalsIgnoreCase(current_phone_string))
					return true;
			}
		}
		return false;
	}

	@Override
	public Contact getByPhoneNumber(String phoneNumber) {
		PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
		Phonenumber.PhoneNumber current_phone = new Phonenumber.PhoneNumber();
		try {
			current_phone = phoneNumberUtil.parse(phoneNumber, "IT");
		} catch (NumberParseException e) {
			throw new RuntimeException("Invalid Phone Number !");
		}
		String current_phone_string;
		if (phoneNumberUtil.isPossibleNumberForType(current_phone, PhoneNumberType.MOBILE)) {
			current_phone_string = phoneNumberUtil.format(current_phone, PhoneNumberFormat.E164);
		} else {
			throw new RuntimeException("Invalid Phone Number!");
		}

		List<Person> people;
		try {
			ListConnectionsResponse response = peopleService.people().connections().list("people/me")
					.setPersonFields("names,phoneNumbers")
					.execute();
			people = response.getConnections();
		} catch (IOException e) {
			throw new RuntimeException("Error Getting Contacts " + e);
		}
		String contact_name = null;
		for (Person person : CollectionUtils.emptyIfNull(people)) {
			for(PhoneNumber phone : CollectionUtils.emptyIfNull(person.getPhoneNumbers())) {
				if(phone.getCanonicalForm().equalsIgnoreCase(current_phone_string))
					contact_name = person.getNames().get(0).getDisplayName();
			}
		}
		if(contact_name!=null){
			return Contact.builder().phoneNumber(current_phone_string).name(contact_name).build();
		}
		return null;
	}

	@Override
	public boolean deleteContact(String contactId) {
		boolean result = false;
		try {
			peopleService.people().deleteContact(contactId).execute();
			result = true;
		} catch (IOException e) {
			throw new RuntimeException("Error Deleting Contact! "+ e);
		}
		return result;
	}


}

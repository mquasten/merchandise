package de.mq.merchandise.model.support;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import de.mq.merchandise.contact.support.AddressAO;
import de.mq.merchandise.contact.support.EMailContactAO;
import de.mq.merchandise.contact.support.MessengerContactAO;
import de.mq.merchandise.contact.support.PhoneContactAO;
import de.mq.merchandise.contact.support.PostBoxAO;
import de.mq.merchandise.customer.support.LegalPersonAO;
import de.mq.merchandise.customer.support.NaturalPersonAO;

public final  class Functions {
	
	static final String PERSON_ADDRESS_ADDRESS_KEY = "person_address_address";
	static final String PERSON_ADDRESS_POSTBOX_KEY = "person_address_postbox";
	static final String PERSON_CONTACT_MESSENGER_KEY = "person_contact_messenger";
	static final String PERSON_CONTACT_EMAIL_KEY = "person_contact_email";
	static final String PERSON_CONTACT_TELEFON_KEY = "person_contact_telefon";
	static final  String FOUNDATION_PREFIX_KEY = "view_person_foundation_perfix";
	static final  String TAXID_PREFIX_KEY = "view_person_tax_perfix";
	static final String PERSON_NOT_KNOWN_STRING = "";
	
	
	static final String CONTACT_NOT_KNOWN_STRING = "";
	
	
	public final static String country(final String language, final String country) {
		return new Locale(language, country).getDisplayCountry(new Locale(language));
	}
	
	public final static String language(final String language) {
		return new Locale(language).getDisplayLanguage(new Locale(language));
	}
	
	public final static String shortDate(final String language, final Date date){
		return DateFormat.getDateInstance(DateFormat.SHORT, new Locale(language)).format(date);
	}
	
	/* You've been a very bad el resolver! A very very bad bad el resolver ...  (Beyonce f. Lady Gaga: Telephone)  */
	public final static String person2String(final Object person, final Map<String,String> message, final String language ) {
	
		if (person instanceof NaturalPersonAO) {
			 final NaturalPersonAO naturalPersonAO = (NaturalPersonAO) person;
			 return naturalPersonAO.getFirstName() + " "+  naturalPersonAO.getLastName();
			
		}
		if (person instanceof LegalPersonAO) {
			final LegalPersonAO legalPersonAO = (LegalPersonAO) person;
			return legalPersonAO.getName() +" " + legalPersonAO.getLegalForm() + ", " + message.get(FOUNDATION_PREFIX_KEY) + ": "+ shortDate(language, legalPersonAO.getFoundationDate()) + ", " + message.get(TAXID_PREFIX_KEY) + ": " + legalPersonAO.getTaxId();
		}
		return PERSON_NOT_KNOWN_STRING;
	}
	
	
	public final static boolean isLegalPerson(final Object person) {
		if (person instanceof LegalPersonAO) {
			return true;
		}
		
		return false;
	}
	
	
	public final static boolean isAddress(final Object address) {
		if( address instanceof AddressAO){
			return true;
		}
		return false;
	}
	
	public final static boolean isPostBox(final Object address) {
		if( address instanceof PostBoxAO){
			return true;
		}
		return false;
	}
	
	
	public final static boolean isPhoneContact(final Object contact) {
		if( contact instanceof PhoneContactAO){
			return true;
		}
		return false;
	}
	
	public final static boolean isEMailContact(final Object contact) {
		if( contact instanceof EMailContactAO){
			return true;
		}
		return false;
	}
	
	public final static boolean isMessengerContact(final Object contact) {
		
		if( contact instanceof MessengerContactAO){
			return true;
		}
		return false;
	}
	
	public final static String translate( final Object contact, final Map<String,String> message) {
		
		if (contact instanceof PhoneContactAO) {
			return message.get(PERSON_CONTACT_TELEFON_KEY);
		}
		if (contact instanceof EMailContactAO) {
			return message.get(PERSON_CONTACT_EMAIL_KEY);
		}
		if( contact instanceof MessengerContactAO){
		    return message.get(PERSON_CONTACT_MESSENGER_KEY);
		}
		if(contact instanceof PostBoxAO){
			return message.get(PERSON_ADDRESS_POSTBOX_KEY);
		}
		if( contact instanceof AddressAO){
			return message.get(PERSON_ADDRESS_ADDRESS_KEY);
		}
		return CONTACT_NOT_KNOWN_STRING; 
	}
	

}

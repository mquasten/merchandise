package de.mq.merchandise.model.support;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.contact.support.AddressAO;
import de.mq.merchandise.contact.support.EMailContactAO;
import de.mq.merchandise.contact.support.MessengerContactAO;
import de.mq.merchandise.contact.support.PhoneContactAO;
import de.mq.merchandise.contact.support.PostBoxAO;
import de.mq.merchandise.customer.support.LegalPersonAO;
import de.mq.merchandise.customer.support.NaturalPersonAO;
import de.mq.merchandise.model.PersonTestConstants;


public class FunctionsTest {
	
	private static final String TAX_PREFIX = "Steuernummer";
	private static final String FOUNDATION_PREFIX = "gegründet";
	private static final String TAX_ID = "08/15";
	private static final String LEGAL_FORM = "AG";
	private static final Date FOUNDATION_DATE = new GregorianCalendar(1968, 4, 28).getTime();
	private static final String LAST_NAME = "Minogue";
	private static final String FIRST_NAME = "Kylie";

	@Test
	public final void country() {
		Assert.assertEquals(Locale.GERMANY.getDisplayCountry(Locale.GERMAN), Functions.country("de", "DE"));
		Assert.assertEquals(Locale.GERMANY.getDisplayCountry(Locale.ENGLISH), Functions.country("en", "DE"));
	}
	
	@Test
	public final void language() {
		Assert.assertEquals(Locale.GERMAN.getDisplayLanguage(Locale.GERMAN), Functions.language(Locale.GERMAN.getLanguage()));
		Assert.assertEquals(Locale.ENGLISH.getDisplayLanguage(Locale.ENGLISH), Functions.language(Locale.ENGLISH.getLanguage()));
	}
	
	@Test
	public final void coverageOnly() {
		new Functions();
	}
	
	@Test
	public final void date() {
		
		Assert.assertEquals("28.05.68", Functions.shortDate("de", PersonTestConstants.FOUNDATION_DATE));

		Assert.assertEquals("5/28/68", Functions.shortDate("en", PersonTestConstants.FOUNDATION_DATE));
	}
	
	@Test
	public final void isLegalPerson() {
		final LegalPersonAO legalPerson  = Mockito.mock(LegalPersonAO.class);
		final NaturalPersonAO naturalPersonAO = Mockito.mock(NaturalPersonAO.class);
		Assert.assertTrue(Functions.isLegalPerson(legalPerson));
		Assert.assertFalse(Functions.isLegalPerson(naturalPersonAO));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public final void naturalPerson2String() {
		final NaturalPersonAO naturalPersonAO = Mockito.mock(NaturalPersonAO.class);
		Mockito.when(naturalPersonAO.getFirstName()).thenReturn(FIRST_NAME);
		Mockito.when(naturalPersonAO.getLastName()).thenReturn(LAST_NAME);
		Assert.assertEquals(FIRST_NAME + " " + LAST_NAME, Functions.person2String(naturalPersonAO, Mockito.mock(Map.class), "de"));
		
	}
	
	@Test
	public final void legalPerson() {
		final LegalPersonAO legalPersonAO = Mockito.mock(LegalPersonAO.class);
		Mockito.when(legalPersonAO.getFoundationDate()).thenReturn(FOUNDATION_DATE);
		Mockito.when(legalPersonAO.getName()).thenReturn(LAST_NAME);
		Mockito.when(legalPersonAO.getLegalForm()).thenReturn(LEGAL_FORM);
		Mockito.when(legalPersonAO.getTaxId()).thenReturn(TAX_ID);
		final Map<String,String> message = new HashMap<>();
		message.put(Functions.FOUNDATION_PREFIX_KEY, FOUNDATION_PREFIX);
		message.put(Functions.TAXID_PREFIX_KEY, TAX_PREFIX);
		final String result = Functions.person2String(legalPersonAO, message, "de");
		Assert.assertEquals("Minogue AG, gegründet: 28.05.68, Steuernummer: 08/15", result);
		
	}
	
	@Test
	public final void personNoPerson(){
		final Map<String,String> message = new HashMap<>();
		Assert.assertEquals(Functions.PERSON_NOT_KNOWN_STRING, Functions.person2String(null, message, "de"));
	}
	
	@Test
	public final void isAddress() {
		Assert.assertTrue(Functions.isAddress(Mockito.mock(AddressAO.class)));
		Assert.assertFalse(Functions.isAddress(Mockito.mock(PostBoxAO.class)));
	}
	
	@Test
	public final void isPostBox() {
		Assert.assertTrue(Functions.isPostBox(Mockito.mock(PostBoxAO.class)));
		Assert.assertFalse(Functions.isPostBox(Mockito.mock(AddressAO.class)));
	}
	
	@Test
	public final void isPhoneContact() {
		Assert.assertTrue(Functions.isPhoneContact(Mockito.mock(PhoneContactAO.class)));
		Assert.assertFalse(Functions.isPhoneContact(Mockito.mock(AddressAO.class)));
	}
	
	@Test
	public final void isEMailContact() {
		Assert.assertTrue(Functions.isEMailContact(Mockito.mock(EMailContactAO.class)));
		Assert.assertFalse(Functions.isEMailContact(Mockito.mock(AddressAO.class)));
	}
	
	@Test
	public final void isMessengerContact() {
		Assert.assertTrue(Functions.isMessengerContact(Mockito.mock(MessengerContactAO.class)));
		Assert.assertFalse(Functions.isMessengerContact(Mockito.mock(AddressAO.class)));
	}
	
	@Test
	public final void translate() {
		final Map<String,String> messages = new HashMap<>();
		messages.put(Functions.PERSON_CONTACT_TELEFON_KEY, "Telefon: Beyonce f. Lady Gaga");
		messages.put(Functions.PERSON_CONTACT_EMAIL_KEY, "E-Mail-Address");
		messages.put(Functions.PERSON_CONTACT_MESSENGER_KEY, "Messenger-Account");
		messages.put(Functions.PERSON_ADDRESS_ADDRESS_KEY, "Addresse");
		messages.put(Functions.PERSON_ADDRESS_POSTBOX_KEY, "Postfach");
		Assert.assertEquals(messages.get(Functions.PERSON_CONTACT_TELEFON_KEY), Functions.translate(Mockito.mock(PhoneContactAO.class), messages));
		Assert.assertEquals(messages.get(Functions.PERSON_CONTACT_EMAIL_KEY), Functions.translate(Mockito.mock(EMailContactAO.class), messages));
		Assert.assertEquals(messages.get(Functions.PERSON_CONTACT_MESSENGER_KEY), Functions.translate(Mockito.mock(MessengerContactAO.class), messages));
		Assert.assertEquals(messages.get(Functions.PERSON_ADDRESS_ADDRESS_KEY), Functions.translate(Mockito.mock(AddressAO.class) , messages));
		Assert.assertEquals(messages.get(Functions.PERSON_ADDRESS_POSTBOX_KEY), Functions.translate(Mockito.mock(PostBoxAO.class) , messages));
		
		Assert.assertEquals(Functions.CONTACT_NOT_KNOWN_STRING, Functions.translate(new String() , messages));
	}
	

	@Test
	public final void first() {
		final Map<String, Object> values=new HashMap<>();
		values.put(FIRST_NAME, FIRST_NAME);
		values.put(LAST_NAME, LAST_NAME);
		Assert.assertEquals(FIRST_NAME, Functions.firstKey(values));
	}
	
	@Test
	public final void firstEmpthy() {
		
		
		Assert.assertNull(Functions.firstKey(new HashMap<String,String>()));
	}
	
}

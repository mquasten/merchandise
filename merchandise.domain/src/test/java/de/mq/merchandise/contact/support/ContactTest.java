package de.mq.merchandise.contact.support;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.contact.Contact;
import de.mq.merchandise.contact.InstantMessenger;
import de.mq.merchandise.contact.support.AbstractContact;
import de.mq.merchandise.contact.support.EMailContactImpl;
import de.mq.merchandise.contact.support.InstantMessengerContactImpl;
import de.mq.merchandise.contact.support.PhoneContactImpl;

public class ContactTest {
	
	private static final String NUMBER = "123456";
	private static final String AREA_CODE = "211";
	private static final String INTERNATIONAL_AREA_CODE = "49";
	private static final String INSTANT_MESSENGER_ACCOUNT = "hotKylie";
	private static final String MAIL_ACCOUNT = "kylie@fever.com";
	private static final String ACCOUNT = "test";
	private static final long ID = 19680528L;

	@Test
	public final void id() {
		final Contact contact = new ContactMock(ID, ACCOUNT, false);
		Assert.assertEquals(ID, contact.id());
		Assert.assertEquals(ACCOUNT, contact.contact());
	}
	
	@Test
	public final void defaultConstructor() {
		final ContactMock contact = new ContactMock();
		Assert.assertNull(contact.account);
		Assert.assertFalse(contact.isLogin());
	}
	
	@Test
	public final void isLoginNoLoginContact() {
		final AbstractContact contact = new ContactMock(ID, ACCOUNT, false);
		contact.onStore();
		
		Assert.assertNull( ReflectionTestUtils.getField(contact, "login"));
	}
	
	@Test
	public final void isLogin() {
		final AbstractContact contact = new ContactMock(ID, ACCOUNT, true);
		contact.onStore();
		
		Assert.assertEquals(ACCOUNT, ReflectionTestUtils.getField(contact, "login"));
	}
	
	@Test
	public final void onLoad() {
		final AbstractContact contact = new ContactMock(ID, ACCOUNT, true);
		ReflectionTestUtils.setField(contact, "login", null);
		contact.onLoad();
		Assert.assertFalse(contact.isLogin());
		ReflectionTestUtils.setField(contact, "login", MAIL_ACCOUNT);
		contact.onLoad();
		
		Assert.assertTrue(contact.isLogin());
		
	}
	
	
	
	
	@Test
	public final void mailContact() {
		final Contact contact = new EMailContactImpl(MAIL_ACCOUNT,false);
		Assert.assertEquals(MAIL_ACCOUNT, contact.contact());
	}
	
	@Test
	public final void instantMessengerTypes() {
		Assert.assertEquals(8, InstantMessenger.values().length);
		
	}
	
	@Test
	public final void instantMessengerContact() {
		final Contact contact = new InstantMessengerContactImpl(InstantMessenger.Skype, INSTANT_MESSENGER_ACCOUNT,false);
		Assert.assertEquals(InstantMessenger.Skype + ": " + INSTANT_MESSENGER_ACCOUNT  , contact.contact());
		
	}
	
	@Test
	public final void phoneContact() {
		final Contact contact = new PhoneContactImpl(INTERNATIONAL_AREA_CODE,AREA_CODE , NUMBER, false); 
		Assert.assertEquals(INTERNATIONAL_AREA_CODE+AREA_CODE+NUMBER, contact.contact());
	}
	
	
	@Test
	public final void eMailHash(){
		final Contact contact = new EMailContactImpl(MAIL_ACCOUNT,false);
		Assert.assertEquals(MAIL_ACCOUNT.hashCode(), contact.hashCode());
	}
	
	@Test
	public final void eMailHashInvalid(){
		Assert.assertTrue(new EMailContactImpl(null,false).hashCode() != new EMailContactImpl(null,false).hashCode());
	}
	
	@Test
	public final void eMailEquals() {
		Assert.assertFalse(new EMailContactImpl(null,false).equals(new EMailContactImpl(null,false)));
		Assert.assertTrue(new EMailContactImpl(MAIL_ACCOUNT,false).equals(new EMailContactImpl(MAIL_ACCOUNT,false)));
		Assert.assertFalse(new EMailContactImpl(MAIL_ACCOUNT,false).equals(new EMailContactImpl(null,false)));
		Assert.assertFalse(new EMailContactImpl(null,false).equals(new EMailContactImpl(MAIL_ACCOUNT,false)));
		Assert.assertFalse(new EMailContactImpl(MAIL_ACCOUNT,false).equals("kylie is hot"));
	}
	
	@Test
	public final void eMailString() {
		Assert.assertEquals("account="+ MAIL_ACCOUNT, new EMailContactImpl(MAIL_ACCOUNT,false).toString());
		Assert.assertTrue(new EMailContactImpl(null,false).toString().startsWith(EMailContactImpl.class.getName()+"@"));
	}
	
	@Test
	public final void instandMessengerEquals() {
		final Contact instantMessengerContact = new InstantMessengerContactImpl(null, null,false);
		Assert.assertFalse(instantMessengerContact.equals( new InstantMessengerContactImpl(null, null,false)));
		Assert.assertTrue(instantMessengerContact.equals(instantMessengerContact));
		Assert.assertTrue(new InstantMessengerContactImpl(InstantMessenger.SIMPLE, INSTANT_MESSENGER_ACCOUNT,false).equals( new InstantMessengerContactImpl(InstantMessenger.SIMPLE, INSTANT_MESSENGER_ACCOUNT,false)));
		Assert.assertFalse(new InstantMessengerContactImpl(InstantMessenger.SIMPLE, INSTANT_MESSENGER_ACCOUNT,false).equals( new InstantMessengerContactImpl(InstantMessenger.SIMPLE, "dontLetMeGetMe",false)));
	}
	
	@Test
	public final void instandMessengerHasCode() {
		final Contact instantMessengerContact = new InstantMessengerContactImpl(null, null,false);
		Assert.assertEquals(InstantMessenger.SIMPLE.hashCode() + INSTANT_MESSENGER_ACCOUNT.hashCode(), new InstantMessengerContactImpl(InstantMessenger.SIMPLE, INSTANT_MESSENGER_ACCOUNT,false).hashCode());
	    Assert.assertEquals(System.identityHashCode(instantMessengerContact), instantMessengerContact.hashCode());
	}
	
	@Test
	public final void phoneEquals() {
		final Contact contact = new PhoneContactImpl(null, null, null,false);
		Assert.assertTrue(contact.equals(contact));
		Assert.assertFalse(contact.equals(new PhoneContactImpl(null, null, null,false)));
		Assert.assertFalse(new PhoneContactImpl(PhoneContactImpl.AREA_CODE_DE, AREA_CODE, NUMBER,false).equals(new PhoneContactImpl(PhoneContactImpl.AREA_CODE_DE,"000", NUMBER,false)));
		Assert.assertTrue(new PhoneContactImpl(PhoneContactImpl.AREA_CODE_DE, AREA_CODE, NUMBER,false).equals(new PhoneContactImpl(PhoneContactImpl.AREA_CODE_DE, AREA_CODE, NUMBER,false)));
	}
	
	
	@Test
	public final void phoneHasCode() {
		final Contact contact = new PhoneContactImpl(null, null, null,false);
		Assert.assertEquals(System.identityHashCode(contact), contact.hashCode());
		Assert.assertEquals(PhoneContactImpl.AREA_CODE_DE.hashCode()+ AREA_CODE.hashCode()+NUMBER.hashCode(), new PhoneContactImpl(PhoneContactImpl.AREA_CODE_DE, AREA_CODE, NUMBER, false).hashCode());
	}
	
	
	@Test
	public final void hasId() {
		Assert.assertFalse(new PhoneContactImpl("01","200", "12345678", false).hasId());
		Assert.assertTrue(new ContactMock(ID, "LadyGagaAndBeyonce@telephone.us", false).hasId());
	}
	
	class ContactMock extends AbstractContact {

		private static final long serialVersionUID = 1L;


		ContactMock() {
			super();
		}
		
		ContactMock(final Long id, final String account, final boolean  isLogin) {
			super(account,isLogin);
			this.id=id;
			
		}
		
		
		@Override
		public String contactInfo() {
			return account;
		}
		
	}

}

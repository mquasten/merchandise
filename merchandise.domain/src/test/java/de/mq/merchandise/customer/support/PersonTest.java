package de.mq.merchandise.customer.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.DigestUtils;

import de.mq.merchandise.contact.Address;
import de.mq.merchandise.contact.Contact;
import de.mq.merchandise.contact.LoginContact;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.LegalForm;
import de.mq.merchandise.customer.LegalPerson;
import de.mq.merchandise.customer.Nativity;
import de.mq.merchandise.customer.NaturalPerson;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.customer.PersonRole;
import de.mq.merchandise.customer.State;
import de.mq.merchandise.customer.TradeRegister;
import de.mq.merchandise.customer.support.AbstractPerson;
import de.mq.merchandise.customer.support.LegalPersonImpl;
import de.mq.merchandise.customer.support.NaturalPersonImpl;
import de.mq.merchandise.customer.support.StateImpl;
import de.mq.merchandise.customer.support.TradeRegisterImpl;
import de.mq.merchandise.customer.support.UserRelation;

public class PersonTest {
	private static final String TAXID = "4711";
	private static final String FIRSTNAME = "Kylie";
	private static final long ID = 19680528L;
	private static final String NAME = "Minogue";
	private static final Date DATE =  new GregorianCalendar(1968, 04, 28).getTime();
	private static final Nativity NATIVITY = Mockito.mock(Nativity.class);
	private static final String PASSWORD = "kinkyKylie";
			




	@Test
	public final void id() {
		final Person person = new PersonMock(NAME);
		ReflectionTestUtils.setField(person, "id", 19680528L);
		Assert.assertEquals(ID, person.id());
		Assert.assertEquals(NAME, person.name());
	}
	
	@Test(expected=IllegalStateException.class)
	public final void idNull() {
		new PersonMock(NAME).id();
	}
	
	@Test()
	public final void createInvalid() {
		final Person person = new PersonMock(null);
		Assert.assertNull(person.name());
	}
	
	@Test
	public final void defaultConstructor() {
		Assert.assertNotNull(new PersonMock());
	}
	
	
	class PersonMock extends AbstractPerson {
		
		private static final long serialVersionUID = 1L;

		PersonMock(final String name) {
			super(name, Locale.GERMAN);
			
		}
		
		
		PersonMock(final String name, Locale locale) {
			super(name, locale);
			
		}
		
		PersonMock() {
			super();
		}
		
	}
	
	@Test
	public final void createNaturalPerson() {
		final NaturalPerson person = new NaturalPersonImpl(FIRSTNAME, NAME, NATIVITY);
		Assert.assertEquals(FIRSTNAME, person.firstname());
		Assert.assertEquals(NAME, person.name());
		Assert.assertEquals(NATIVITY, person.nativity());
		
	}
	
	@Test
	public final void createInvalidNaturalPerson() throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		final NaturalPerson person = newInvalidNaturalPerson();
		Assert.assertNull(person.name());
		Assert.assertNull(person.firstname());
		Assert.assertNull(person.nativity());
		
	}

	private NaturalPerson newInvalidNaturalPerson()  {
		
		try {
			final Constructor<? extends NaturalPerson> constructor =  NaturalPersonImpl.class.getDeclaredConstructor();
			constructor.setAccessible(true);
			return constructor.newInstance();
		} catch (final Exception ex) {
			throw new IllegalStateException(ex);
		} 
	}
	
	@Test
	public final void createLegalPerson() {
		final TradeRegister tradeRegister = Mockito.mock(TradeRegisterImpl.class);
		final LegalPerson person = new LegalPersonImpl(NAME, TAXID,tradeRegister, LegalForm.AG, DATE );
		Assert.assertEquals(NAME, person.name());
		Assert.assertEquals(TAXID, person.taxId());
		Assert.assertEquals(tradeRegister, person.tradeRegister());
		Assert.assertEquals(LegalForm.AG, person.legalForm());
		Assert.assertEquals(DATE, person.foundationDate());
	}
	
	@Test
	public final void createInvalidLegalPerson() {
		final LegalPerson person = newInvalidLegalPerson();
		Assert.assertNull(person.name());
		Assert.assertNull(person.taxId());
		Assert.assertNull(person.tradeRegister());
		Assert.assertNull(person.legalForm());
		Assert.assertNull(person.foundationDate());
		
	}
	
	
	@Test
	public final void assignContact() {
		final Person person = new PersonMock(NAME);
		final LoginContact contact = Mockito.mock(LoginContact.class);
		person.assign(contact);
		@SuppressWarnings("unchecked")
		final Set<Contact> contacts = (Set<Contact>) ReflectionTestUtils.getField(person, "contacts");
		Assert.assertEquals(1, contacts.size());
		Assert.assertEquals(contact, contacts.iterator().next());
	}
	
	@Test
	public final void contacts() {
		final Contact contact = Mockito.mock(Contact.class);
		final Person person = new PersonMock(NAME);
		@SuppressWarnings("unchecked")
		final Set<Contact> contacts = (Set<Contact>) ReflectionTestUtils.getField(person, "contacts");
		contacts.add(contact);
		Assert.assertEquals(1, person.contacts().size());
		Assert.assertEquals(contact, person.contacts().iterator().next());
	}
	
	@Test
	public final void removeContact() {
		final Person person = new PersonMock(NAME);
		final LoginContact contact = Mockito.mock(LoginContact.class);
		person.assign(contact);
		Assert.assertFalse(person.contacts().isEmpty());
		person.remove(contact);
		Assert.assertTrue(person.contacts().isEmpty());
	}
	
	@Test
	public final void addRole() {
		final Person person = new PersonMock(NAME);
		Assert.assertTrue(person.roles().isEmpty());
		person.assign(PersonRole.Catalogs);
		Assert.assertFalse(person.roles().isEmpty());
		Assert.assertEquals(PersonRole.Catalogs, person.roles().iterator().next());
	}
	
	@Test
	public final void removeRole() {
		final Person person = new PersonMock(NAME);
		person.assign(PersonRole.Catalogs);
		Assert.assertFalse(person.roles().isEmpty());
		person.remove(PersonRole.Catalogs);
		Assert.assertTrue(person.roles().isEmpty());
	}
	
	@Test
	public final void hasRole() {
		final Person person = new PersonMock(NAME);
		Assert.assertFalse(person.hasRole(PersonRole.Catalogs));
		person.assign(PersonRole.Catalogs);
		Assert.assertTrue(person.hasRole(PersonRole.Catalogs));
	}
	
	@Test
	public final void legalPersonEquals() {
		final LegalPerson person = newInvalidLegalPerson();
		Assert.assertTrue(person.equals(person));
		final TradeRegister tradeRegister = Mockito.mock(TradeRegisterImpl.class);
		Assert.assertTrue(new LegalPersonImpl("Kylie", null, tradeRegister, null, null).equals(new LegalPersonImpl("Kylie", null, tradeRegister, null, null)));
		
		Assert.assertFalse(new LegalPersonImpl("Kylie", null, tradeRegister, null, null).equals(new LegalPersonImpl(null, null, tradeRegister, null, null)));
		
		Assert.assertFalse(new LegalPersonImpl("Kylie", null, tradeRegister, null, null).equals(new PersonMock("Kylie")));
	}
	
	@Test
	public final void assignAddress() {
		final Person person = new PersonMock(NAME);
		final Address address = Mockito.mock(Address.class);
		Assert.assertTrue(person.contacts().isEmpty());
		person.assign(address);
		Assert.assertFalse(person.contacts().isEmpty());
		Assert.assertEquals(address, person.contacts().iterator().next());
		
	}
	
	@Test
	public final void removeAddress() {
		final Person person = new PersonMock(NAME);
		final Address address = Mockito.mock(Address.class);
		person.assign(address);
		Assert.assertFalse(person.contacts().isEmpty());
		person.remove(address);
		Assert.assertTrue(person.contacts().isEmpty());
	}
	
	
	
	@Test
	public final void state() {
		final State state = Mockito.mock(StateImpl.class);
		final Person person = new PersonMock(NAME);
		ReflectionTestUtils.setField(person, "state", state);
		Assert.assertEquals(state, person.state());
	}
	
	@Test
	public final void customer() {
		
		final Customer customer = Mockito.mock(Customer.class);
		final Person person = new PersonMock(NAME);
		State inActiveState = Mockito.mock(State.class);
		final State  state = Mockito.mock(State.class);
		Mockito.when(state.isActive()).thenReturn(true);
		
		final UserRelation activeUserRelation = Mockito.mock(UserRelation.class);
		Mockito.when(activeUserRelation.customer()).thenReturn(customer);
		Mockito.when(activeUserRelation.person()).thenReturn(person);
		Mockito.when(activeUserRelation.state()).thenReturn(state);
				
		
		final UserRelation inActiveUserRelation = Mockito.mock(UserRelation.class);
		Mockito.when(inActiveUserRelation.customer()).thenReturn(customer);
		Mockito.when(inActiveUserRelation.person()).thenReturn(person);
		Mockito.when(inActiveUserRelation.state()).thenReturn(inActiveState);
		
		final Set<UserRelation> userRelations = new HashSet<>();
		userRelations.add(activeUserRelation);
		userRelations.add(inActiveUserRelation);
		ReflectionTestUtils.setField(person, "userRelations", userRelations);
		
		Assert.assertEquals(1, person.customers().size());
		Assert.assertEquals(activeUserRelation.customer(), person.customers().iterator().next());
		
	}
	
	
	@Test
	public final void legalPersonHashCode() {
		final LegalPerson person = newInvalidLegalPerson();
		Assert.assertEquals(System.identityHashCode(person), person.hashCode());
		TradeRegister tradeRegister = Mockito.mock(TradeRegisterImpl.class);
		Assert.assertEquals(NAME.hashCode()+ tradeRegister.hashCode(), new LegalPersonImpl(NAME, TAXID, tradeRegister, LegalForm.AG, DATE ).hashCode());
		
	}
	
	@Test
	public final void naturalPersonEquals() {
		final Person naturalPerson = newInvalidNaturalPerson();
		Assert.assertTrue(naturalPerson.equals(naturalPerson));
		Assert.assertFalse(naturalPerson.equals(new NaturalPersonImpl(null, null, null)));
		Assert.assertTrue(new NaturalPersonImpl(FIRSTNAME, NAME, NATIVITY).equals(new NaturalPersonImpl(FIRSTNAME, NAME, NATIVITY)));
		Assert.assertFalse(new NaturalPersonImpl(FIRSTNAME, NAME, NATIVITY).equals(new NaturalPersonImpl("dontLetMeGetMe", NAME, NATIVITY)));
	}
	
	@Test
	public final void naturalPersonHash() {
		final NaturalPerson person = newInvalidNaturalPerson();
		Assert.assertEquals(System.identityHashCode(person), person.hashCode());
		Assert.assertEquals(FIRSTNAME.hashCode()+ NAME.hashCode()+NATIVITY.hashCode(), new NaturalPersonImpl(FIRSTNAME, NAME, NATIVITY).hashCode());
		
	}
	
	@Test
	public final void locale(){
		final Person person = new PersonMock(NAME, Locale.US);
		Assert.assertEquals(Locale.US, person.locale());
		
	}
	
	@Test
	public final void hasId() {
		Assert.assertFalse(newInvalidLegalPerson().hasId());
		final Person person = newInvalidLegalPerson();
		ReflectionTestUtils.setField(person, "id", 19680528L);
		
		Assert.assertTrue(person.hasId());
		
	}
	

	private LegalPerson newInvalidLegalPerson()  {
		
		try {
			final Constructor<? extends LegalPerson> constructor = LegalPersonImpl.class.getDeclaredConstructor();
			constructor.setAccessible(true);
			return constructor.newInstance();
		} catch (final Exception ex) {
			throw new IllegalStateException(ex);
		} 
	}
	
	
	@Test
	public final void digest() {
		final NaturalPerson person = new NaturalPersonImpl(FIRSTNAME, NAME, NATIVITY);
		final Digest digest = Mockito.mock(Digest.class);
		ReflectionTestUtils.setField(person, "digest", digest);
		Assert.assertEquals(digest, person.digest());
	}
	
	@Test
	public final void digestEntityCallback() {
		final NaturalPerson person = new NaturalPersonImpl(FIRSTNAME, NAME, NATIVITY);
		ReflectionTestUtils.setField(person.digest(), "digest", PASSWORD);
		Assert.assertEquals(PASSWORD, ReflectionTestUtils.getField(person.digest(), "digest"));
		((AbstractPerson)person).digestPassword();
		Assert.assertEquals(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()).toUpperCase(), ReflectionTestUtils.getField(person.digest(), "digest"));
	}
}

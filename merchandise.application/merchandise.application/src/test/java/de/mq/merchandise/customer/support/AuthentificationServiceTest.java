package de.mq.merchandise.customer.support;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.jaas.JaasAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthoritiesContainerImpl;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerRole;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.customer.PersonRole;
import de.mq.merchandise.customer.State;
import de.mq.merchandise.util.StringCrypter;

public class AuthentificationServiceTest {
	
		private static final String PERSON_NAME = "Kylie Minogue";
		private static final String CRYPTED_PASSWORD = "xxxxxxx";
		private static final String PASSWORD = "kinkyKylie";
		private static final long CUSTOMER_ID = 4711L;
		private static final long USER_ID = 19680528L;
		private final SecurityContextFactory securityContextFactory = Mockito.mock(SecurityContextFactory.class);
		private final SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		private final CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);
		private final StringCrypter stringCrypter = Mockito.mock(StringCrypter.class);
		private final Authentication authentication = Mockito.mock(Authentication.class);
		private final AuthentificationService authentificationService = new AuthentificationServiceImpl(customerRepository, securityContextFactory, stringCrypter);
		private final Customer customer = Mockito.mock(Customer.class);
		private final Person person = Mockito.mock(Person.class);
		private final List<Person> persons = new ArrayList<>();
		private final Digest digest = Mockito.mock(Digest.class);
		private final State state = Mockito.mock(State.class);
		private final Set<PersonRole> personRoles = new HashSet<>();
		private final List<CustomerRole> customerRoles = new ArrayList<>();
		final String key  = String.format("%s:%s:%s" ,  USER_ID, CUSTOMER_ID, System.currentTimeMillis()/1000/60);
		@Before
		public final void setup() {
			 
			 Mockito.when(securityContextFactory.securityContext()).thenReturn(securityContext);
			 Mockito.when(stringCrypter.encrypt(String.format(PASSWORD, USER_ID, CUSTOMER_ID), key)).thenReturn(CRYPTED_PASSWORD);
			 Mockito.when(authentication.getPrincipal()).thenReturn(String.format("%s:%s", USER_ID, CUSTOMER_ID));
			 Mockito.when(authentication.getCredentials()).thenReturn(CRYPTED_PASSWORD);
			 Mockito.when(customerRepository.forId(CUSTOMER_ID)).thenReturn(customer);
			 persons.add(Mockito.mock(Person.class));
			 persons.add(person);
			
			 Mockito.when(person.id()).thenReturn(USER_ID);
			 Mockito.when(person.name()).thenReturn(PERSON_NAME);
			 Mockito.when(customer.activePersons()).thenReturn(persons);
			 Mockito.when(stringCrypter.decrypt(CRYPTED_PASSWORD,key)).thenReturn(PASSWORD);
			 
			 Mockito.when(digest.check(PASSWORD)).thenReturn(true);
			 Mockito.when(person.digest()).thenReturn(digest);
			 Mockito.when(state.isActive()).thenReturn(true);
			 Mockito.when(person.state()).thenReturn(state);
			 Mockito.when(customer.state()).thenReturn(state);
			 personRoles.add(PersonRole.CreateAccount);
			 customerRoles.add(CustomerRole.Bids);
			 Mockito.when(person.roles()).thenReturn(personRoles);
			 Mockito.when(customer.roles(person)).thenReturn(customerRoles);
		}
		 
		 
		 @Test
		 public final void createSecurityToken() {
			
			 authentificationService.createSecurityToken(USER_ID, CUSTOMER_ID, PASSWORD);
			 final ArgumentCaptor<Authentication> argumentCaptor = ArgumentCaptor.forClass(Authentication.class);
			 Mockito.verify(securityContext).setAuthentication(argumentCaptor.capture());
			 Assert.assertEquals(CRYPTED_PASSWORD, argumentCaptor.getValue().getCredentials());
			 Assert.assertEquals(String.format("%s:%s", USER_ID, CUSTOMER_ID), argumentCaptor.getValue().getPrincipal());
			 Assert.assertFalse(argumentCaptor.getValue().isAuthenticated());
			 Assert.assertEquals(securityContext, securityContextFactory.securityContext());
			 
		 }
		 
		 @Test
		 public final void supports() {
			 Assert.assertTrue(authentificationService.supports(UsernamePasswordAuthenticationToken.class));
			 Assert.assertFalse(authentificationService.supports(JaasAuthenticationToken.class));
		 }
	
		 @Test
		 public final void authenticate() {
			 checkSucess((PersonCustomerAuthentification) authentificationService.authenticate(authentication));
		 }


		private void checkSucess(final PersonCustomerAuthentification result) {
			Assert.assertEquals(PERSON_NAME, result.getName());
			 Assert.assertEquals(person, result.getPrincipal());
			 Assert.assertEquals(customer, result.getDetails());
			 Assert.assertTrue(result.isAuthenticated());
			 Assert.assertEquals(customerRoles.size()+ personRoles.size(), result.getAuthorities().size());
			 
			 for(final GrantedAuthority grantedAuthority : result.getAuthorities() ){
				 Assert.assertTrue(grantedAuthority.getAuthority().equals(CustomerRole.Bids.name())||grantedAuthority.getAuthority().equals(PersonRole.CreateAccount.name()));
			 }
		}
		 @Test(expected=AuthenticationCredentialsNotFoundException.class)
		 public final void authentificationNull() {
			 authentificationService.authenticate(null);
		 }
		 
		 @Test(expected=UsernameNotFoundException.class)
		 public final void authentificationCustomerNotFound() {
			 Mockito.when(customerRepository.forId(CUSTOMER_ID)).thenReturn(null);
			 authentificationService.authenticate(authentication);
		 }
		 
		 @Test(expected=BadCredentialsException.class)
		 public final void authentificationWrongPassword() {
			 Mockito.when(digest.check(PASSWORD)).thenReturn(false);
			 authentificationService.authenticate(authentication);
		 }

		 
		 @Test(expected=UsernameNotFoundException.class)
		 public final void authentificationPersonNotFoundOnCustomer() {
			 Mockito.when(customer.activePersons()).thenReturn(new ArrayList<Person>());
			 authentificationService.authenticate(authentication);
		 }
		 
		 @Test(expected=BadCredentialsException.class)
		 public final void authentificationMissingId() {
			 Mockito.when(authentication.getPrincipal()).thenReturn(String.valueOf(USER_ID));
			 authentificationService.authenticate(authentication);
		 }
		 
		 @Test
		 public final void authentificationDecryptLastMinute() {
			 Mockito.when(stringCrypter.decrypt(CRYPTED_PASSWORD, key)).thenThrow(new IllegalArgumentException());
			 Mockito.when(stringCrypter.decrypt(CRYPTED_PASSWORD,  String.format("%s:%s:%s" ,  USER_ID, CUSTOMER_ID, System.currentTimeMillis()/1000/60 -1 ))).thenReturn(PASSWORD);
			 checkSucess((PersonCustomerAuthentification) authentificationService.authenticate(authentication));
		 }
}

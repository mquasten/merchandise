package de.mq.merchandise.customer.support;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerRole;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.customer.PersonRole;
import de.mq.merchandise.util.StringCrypter;

public class AuthentificationServiceImpl {
	
	private final CustomerRepository customerRepository;
	
	private final SecurityContextFactory securityContextFactory;
	
	private final StringCrypter stringCrypter;
	
	@Autowired
	public AuthentificationServiceImpl(final CustomerRepository customerRepository, final SecurityContextFactory securityContextFactory , final  StringCrypter stringCrypter) {
		this.customerRepository=customerRepository;
		this.securityContextFactory=securityContextFactory;
		this.stringCrypter=stringCrypter;
	}
	
	public final void createSecurityToken(final long userId, final long customerId, final String credentials) {
		securityContextFactory.securityContext().setAuthentication(new UsernamePasswordAuthenticationToken(stringCrypter.concat(":", userId, customerId), stringCrypter.encrypt(credentials, stringCrypter.concatWithCurrentTimeAsFactorFromSeconds(60, ":", userId, customerId))));
	}
	
	@Transactional
	public final  void setAuthenticated() {
		
		final Long personId, customerId;
		final Authentication authentication = securityContextFactory.securityContext().getAuthentication();
		if ( authentication == null){
			throw new AuthenticationServiceException("Missing Authentication in SecurityContext");
		}
		try(final Scanner scanner = new Scanner((String) authentication.getPrincipal())) {
			 scanner.useDelimiter(":");	
			 idExistsGuard(scanner);
			 personId=scanner.nextLong();
			 idExistsGuard(scanner);
			 customerId=scanner.nextLong();
	    } 
		final String password = decrypt(personId, customerId, (String) authentication.getCredentials(), System.currentTimeMillis()/1000/60 ); 
		final Customer customer = customerRepository.forId(customerId);
		if (customer == null){
			throw new AuthenticationServiceException("Customer not found for id, given in token");
		}
		if ( ! customer.state().isActive()){
			throw new AuthenticationServiceException("Customer not active");
		}
		final Person person = personFromCustomer(personId, customer);
		if(! person.state().isActive()){
			throw new AuthenticationServiceException("Person not active");
		}
		if( ! person.digest().check(password) ) {
			throw new AuthenticationServiceException("Wrong password");
		}
		
		final Set<GrantedAuthority> authorities = new HashSet<>();
		for(final PersonRole role : person.roles()){
			authorities.add(new SimpleGrantedAuthority(role.name()));
		}
		
		for(final CustomerRole role : customer.roles(person)) {
			authorities.add(new SimpleGrantedAuthority(role.name()));
		}
		securityContextFactory.securityContext().setAuthentication(new PreAuthenticatedAuthenticationToken(authentication.getPrincipal() , "LadyGagaMaleOrFemaleOrWhatEver->BornThisWay" , authorities));
	}

	private Person  personFromCustomer(final Long personId, final Customer customer) {
		for(final Person person : customer.activePersons()){
			if(person.id() == personId){
				return person;
			}
		}
		throw new AuthenticationServiceException("Person is not user for this customer");
	}

	private void idExistsGuard(final Scanner scanner) {
		if (! scanner.hasNextLong() ) {
			 throw new  AuthenticationServiceException("AuthenticationToken is invalid");
		 }
	}

	private String  decrypt(final long userId, final long customerId, final String credentials, long time ) {
		try {
		    return stringCrypter.decrypt(credentials, stringCrypter.concat(":", userId, customerId, time));
		} catch (IllegalArgumentException ex) {
			 return stringCrypter.decrypt(credentials, stringCrypter.concat(":", userId, customerId, time-1));
		}
	}
	

}

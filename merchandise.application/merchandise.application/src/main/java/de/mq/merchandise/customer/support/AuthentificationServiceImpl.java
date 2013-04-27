package de.mq.merchandise.customer.support;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.util.StringCrypter;

public class AuthentificationServiceImpl implements AuthentificationService {
	
	private static final String DELIMITER = ":";

	
	private final CustomerRepository customerRepository;
	
	private final SecurityContextFactory securityContextFactory;
	
	private final StringCrypter stringCrypter;
	
	@Autowired
	public AuthentificationServiceImpl(final CustomerRepository customerRepository, final SecurityContextFactory securityContextFactory , final  StringCrypter stringCrypter) {
		this.customerRepository=customerRepository;
		this.securityContextFactory=securityContextFactory;
		this.stringCrypter=stringCrypter;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.customer.support.AuthentificationService#createSecurityToken(long, long, java.lang.String)
	 */
	@Override
	public  void createSecurityToken(final long userId, final long customerId, final String credentials) {
		securityContextFactory.securityContext().setAuthentication(new UsernamePasswordAuthenticationToken(concat(DELIMITER, userId, customerId), stringCrypter.encrypt(credentials, concatWithCurrentTimeAsFactorFromSeconds(60, DELIMITER, userId, customerId))));
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.customer.support.AuthentificationService#authenticate(org.springframework.security.core.Authentication)
	 */
	@Override
	@Transactional
	public   Authentication authenticate(final Authentication authentication) {
		
		final Long personId, customerId;
		if ( authentication == null){
			throw new AuthenticationCredentialsNotFoundException("Missing Authentication in SecurityContext");
		}
		try(final Scanner scanner = new Scanner((String) authentication.getPrincipal())) {
			 scanner.useDelimiter(DELIMITER);	
			 idExistsGuard(scanner);
			 personId=scanner.nextLong();
			 idExistsGuard(scanner);
			 customerId=scanner.nextLong();
	    } 
		final String password = decrypt(personId, customerId, (String) authentication.getCredentials(), System.currentTimeMillis()/1000/60 ); 
		final Customer customer = customerRepository.forId(customerId);
		if (customer == null){
			throw new UsernameNotFoundException("Customer not found for id, given in token");
		}
		
		final Person person = personFromCustomer(personId, customer);
		if( ! person.digest().check(password) ) {
			throw new BadCredentialsException("Wrong password");
		}
		return new PersonCustomerAuthentificationImpl(person, customer);
	}

	private Person  personFromCustomer(final long personId, final Customer customer) {
		for(final Person person : customer.activePersons()){
			if(person.id() == personId){
				return person;
			}
		}
		throw new UsernameNotFoundException("Person is not user for this customer");
	}

	private void idExistsGuard(final Scanner scanner) {
		if (! scanner.hasNextLong() ) {
			 throw new BadCredentialsException("AuthenticationToken is invalid");
		 }
	}

	private String  decrypt(final long userId, final long customerId, final String credentials, long time ) {
		try {
		    return stringCrypter.decrypt(credentials, concat(DELIMITER, userId, customerId, time));
		} catch (IllegalArgumentException ex) {
			 return stringCrypter.decrypt(credentials, concat(DELIMITER, userId, customerId, time-1));
		}
	}

	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.customer.support.AuthentificationService#supports(java.lang.Class)
	 */
	
	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(UsernamePasswordAuthenticationToken.class);
	}
	
	
	
	String concatWithCurrentTimeAsFactorFromSeconds(final long factor, final String del,  final long ... ids) {
		final StringBuffer buffer = new StringBuffer();
		for(final long key : ids){
			buffer.append(key);
			buffer.append(del);
		}
		buffer.append(System.currentTimeMillis() / 1000 / factor);
		return buffer.toString();
	}
	
	
	String concat(final String del,  final long ... ids) {
		final StringBuffer buffer = new StringBuffer();
		for(final long key : ids){
			if(buffer.length() >0 ){
				buffer.append(del);
			}
			buffer.append(key);
		}
		return buffer.toString();
	}
	

}

package de.mq.merchandise.customer.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;


import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerRole;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.customer.PersonRole;

public class SimpleAuthenticationProviderImpl  implements AuthenticationProvider {

	@Autowired
	private CustomerRepository customerRepository;
	
	
	@Override
	@Transactional
	public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
		@SuppressWarnings("unchecked")
		final Entry<Customer,Person>  entry = (Entry<Customer, Person>) authentication.getDetails(); 
		Collection<GrantedAuthority> roles = new ArrayList<>();
		final Customer customer = customerRepository.forId(entry.getKey().id());
		
		final Person person = personFromCustomer(entry.getValue().id(), customer);
		for(final PersonRole role : person.roles()){
			roles.add(new SimpleGrantedAuthority(role.name()));
		}
		for(final CustomerRole role : customer.roles(person)){
			roles.add(new SimpleGrantedAuthority(role.name()));
		}
		final AbstractAuthenticationToken  result = new  UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), roles);
		result.setDetails(entry);
		return result;
		
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
	
	private Person  personFromCustomer(final long personId, final Customer customer)  {
		for(final Person person : customer.activePersons()){
			if(person.id() == personId) {
				System.out.println();
				return person;
			}
		}
		throw new AuthenticationServiceException("Person is not user for this customer");
	}



}

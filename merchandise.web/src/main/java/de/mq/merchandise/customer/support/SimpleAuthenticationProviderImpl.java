package de.mq.merchandise.customer.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
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
		for(final PersonRole role : entry.getValue().roles()){
			roles.add(new SimpleGrantedAuthority(role.name()));
		}
		for(final CustomerRole role : entry.getKey().roles(entry.getValue())){
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



}

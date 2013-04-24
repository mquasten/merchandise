package de.mq.merchandise.customer.support;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerRole;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.customer.PersonRole;
import de.mq.merchandise.customer.State;

class PersonCustomerAuthentificationImpl implements PersonCustomerAuthentification {

	private final Person person;

	private Customer customer;

	private static final long serialVersionUID = 1L;

	PersonCustomerAuthentificationImpl(final Person person, final Customer customer) {
		isActiveGuard(person.state(), "Person" );
		isActiveGuard(customer.state(), "Customer" );
		this.person = person;
		this.customer = customer;
	}

    private void isActiveGuard(final State state, String object	) {
    	if ( ! state.isActive()){
    		throw new AuthenticationServiceException( object + " not active");
    	}
    }
    
    
	@Override
	public String getName() {
		return person.name();
	}

	@Override
	public final Collection<? extends GrantedAuthority> getAuthorities() {
		final Set<GrantedAuthority> authorities = new HashSet<>();
		for (final PersonRole role : person.roles()) {
			authorities.add(new SimpleGrantedAuthority(role.name()));
		}
		for (final CustomerRole role : customer.roles(person)) {
			authorities.add(new SimpleGrantedAuthority(role.name()));
		}
		return authorities;
	}

	@Override
	public final Object getCredentials() {
		throw new UnsupportedOperationException();
	}

	@Override
	public final Customer getDetails() {
		return customer;
	}

	@Override
	public final Person getPrincipal() {
		return person;
	}

	@Override
	public final boolean isAuthenticated() {
		return true;
	}

	@Override
	public final void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		throw new UnsupportedOperationException();

	}

	@Override
	public final void setDetails(final Customer customer) {
		if (!customer.hasUser(person)) {
			throw new SecurityException("Person is not user from customer");
		}
		isActiveGuard(customer.state(), "Customer" );
		this.customer=customer;
	}

	@Override
	public final Collection<Customer> getCustomers() {
		return person.customers();

	}

}

package de.mq.merchandise.customer.support;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.Person;


@Repository
@Profile("db")
class CustomerRepositoryImpl implements CustomerRepository {
	
	@PersistenceContext
	private  EntityManager entityManager;
	
	
	public CustomerRepositoryImpl(){
		
	}
	
	CustomerRepositoryImpl(final EntityManager entityManager){
		this.entityManager=entityManager;
	}
	
	public final Customer store(final Customer customer) {
	    return this.entityManager.merge(customer);
	   
	}
	
	
	public final Customer forId(final Long id) {
		return entityManager.find(CustomerImpl.class,id);
	}
	
	public final Collection<Entry<Customer,Person>> forLogin(final String login) {
		final List<Entry<Customer,Person>> result = new ArrayList<>();
		final List<Person> persons = entityManager.createNamedQuery(CustomerRepository.PERSON_FOR_LOGIN,Person.class).setParameter("login", login).getResultList();
		
		DataAccessUtils.requiredSingleResult(persons);
		for(final Customer customer : entityManager.createNamedQuery(CustomerRepository.CUSTOMER_FOR_PERSON, Customer.class).setParameter("personId", persons.get(0).id()).getResultList()) {
			result.add(new AbstractMap.SimpleImmutableEntry<>(customer,persons.get(0)));
		}
		
		emptyResultGuard(result);
		return Collections.unmodifiableList(result);
		
	}

	private void emptyResultGuard(final List<Entry<Customer, Person>> result) {
		if( result.size() < 1){
			throw new EmptyResultDataAccessException(1);
		}
	}

}

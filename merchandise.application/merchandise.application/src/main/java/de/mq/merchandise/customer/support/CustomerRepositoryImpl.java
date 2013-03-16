package de.mq.merchandise.customer.support;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.customer.support.CustomerRepository;


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

}

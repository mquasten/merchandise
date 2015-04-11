package de.mq.merchandise.customer.support;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import de.mq.merchandise.customer.Customer;
@Repository
class CustomerRepositoryImpl implements CustomerRepository {
	
	
   @PersistenceContext
	private EntityManager entityManager;
	

	/* (non-Javadoc)
	 * @see de.mq.merchandise.customer.support.CustomerRepository#customerById(java.util.Optional)
	 */
	@Override
	public  Customer customerById(final Optional<Long> id) {
		Assert.isTrue(id.isPresent(), "Id must be present");
		System.out.println(entityManager);
		final TypedQuery<Customer> query= entityManager.createNamedQuery(CUSTOMER_BY_ID_QUERY, Customer.class);
		query.setParameter(ID_PARAMETER, id.get());
	
		return query.getSingleResult();
		
	}

}

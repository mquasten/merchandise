package de.mq.merchandise.customer.support;

import java.util.Optional;








import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.customer.Customer;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/repositories.xml"})
@Ignore
public class CustomerRepositoryIntegrationTest {
	
	private static final long CUSTOMER_ID = 1L;
	private static final String KIND = "Kind";
	private static final String QUALITY = "Quality";
	private static final long TEST_CUSTOMER_ID = 1L;
	@Autowired
	private CustomerRepository customerRepositoryImpl;
	
	@Test
	@Transactional(propagation=Propagation.REQUIRED)
	public final void readCustomer() {
		final Customer customer = customerRepositoryImpl.customerById(Optional.of(CUSTOMER_ID));
		Assert.assertEquals(TEST_CUSTOMER_ID, (long) customer.id().get());
		Assert.assertEquals(2, customer.conditionTypes().size());
		Assert.assertTrue(customer.conditionTypes().contains(QUALITY));
		Assert.assertTrue(customer.conditionTypes().contains(KIND));
	}

}

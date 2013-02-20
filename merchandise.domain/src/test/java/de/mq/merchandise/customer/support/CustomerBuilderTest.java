package de.mq.merchandise.customer.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerBuilder;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.customer.support.CustomerBuilderImpl;

public class CustomerBuilderTest {
	
	private static final long ID = 19680528L;
	

	@Test
	public final void withId() {
		final CustomerBuilder customerBuilder =  new CustomerBuilderImpl();
		Assert.assertEquals(customerBuilder, customerBuilder.withId(ID));
		Assert.assertEquals(ID, ReflectionTestUtils.getField(customerBuilder, "id"));
	}
	
	@Test
	public final void withPerson() {
		final Person person = Mockito.mock(Person.class);
		final CustomerBuilder customerBuilder =  new CustomerBuilderImpl();
		Assert.assertEquals(customerBuilder, customerBuilder.withPerson(person));
		Assert.assertEquals(person, ReflectionTestUtils.getField(customerBuilder, "person"));
	}
	
	@Test
	public final void build() {
		final Person person = Mockito.mock(Person.class);
		final Customer customer =  new CustomerBuilderImpl().withId(ID).withPerson(person).build();
		Assert.assertEquals(person, customer.person());
		Assert.assertEquals(ID, customer.id());
	}

}

package de.mq.merchandise.customer.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.test.util.ReflectionTestUtils;







import junit.framework.Assert;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.Subject;

public class CustomerTest {
	
	private static final String OTHER_NAME = "Dannii Minogue";
	private static final String NAME_FIELD = "name";
	private static final String CONDITION_TYPE_QUALITY = "quality";
	private static final String NAME = "Kylie Minogue";

	@Test
	public final void create() {
		final Customer customer = new CustomerImpl(NAME);
		Assert.assertEquals(NAME, customer.name());
	}
	
	@Test
	public final void subjects() {
		final Customer customer = new CustomerImpl(NAME);
		final Collection<Subject> subjects = new ArrayList<>();
		final Subject subject = Mockito.mock(Subject.class);
		subjects.add(subject);
		ReflectionTestUtils.setField(customer, "subjects", subjects);
		
		final List<Subject> results = customer.subjects();
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(subject, results.stream().findFirst().get());
	}
	
	@Test
	public final void conditionTypes() {
		final Customer customer = new CustomerImpl(NAME);
		
		Assert.assertEquals(0, customer.conditionTypes().size());
		customer.assignConditionType(CONDITION_TYPE_QUALITY);
		Assert.assertEquals(1, customer.conditionTypes().size());
		Assert.assertEquals(CONDITION_TYPE_QUALITY, customer.conditionTypes().stream().findFirst().get());
	
	}
	
	
	@Test
	public final void  removeConditionType() {
		final Customer customer = new CustomerImpl(NAME);
		customer.assignConditionType(CONDITION_TYPE_QUALITY);
		Assert.assertEquals(1, customer.conditionTypes().size());
		customer.removeConditionType(CONDITION_TYPE_QUALITY);
		
		Assert.assertEquals(0, customer.conditionTypes().size());
		
	}
	
	@Test
	public final void  hash() {
		final Customer customer = new CustomerImpl(NAME);
		Assert.assertEquals(NAME.hashCode(), customer.hashCode());
		ReflectionTestUtils.setField(customer, NAME_FIELD, null);
		Assert.assertEquals(System.identityHashCode(customer), customer.hashCode());
	}
	
	@Test
	public final void equals() {
		final Customer customer = new CustomerImpl(NAME);
		final Customer otherCustomer = new CustomerImpl(NAME);
		ReflectionTestUtils.setField(otherCustomer, NAME_FIELD, null);
		Assert.assertTrue(customer.equals(new CustomerImpl(NAME)));
		Assert.assertFalse(customer.equals(new CustomerImpl(OTHER_NAME)));
		Assert.assertFalse(otherCustomer.equals(customer));
		Assert.assertFalse(customer.equals(otherCustomer));
		Assert.assertFalse(customer.equals(NAME));
	}
	@Test
	public final void defaultConstructor() {
		Assert.assertTrue(BeanUtils.instantiateClass(CustomerImpl.class) instanceof Customer);
	}

}

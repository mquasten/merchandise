package de.mq.merchandise.customer.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.customer.Customer;

public class ConditionTypeTest {
	
	private static final String NAME_FIELD = "name";
	private static final String CUSTOMER_FIELD = "customer";
	private static final String NAME_QUALITY = "quality";
	private Customer customer = Mockito.mock(Customer.class);
	
	@Test
	public final void create() {
		final ConditionTypeImpl conditionType = new ConditionTypeImpl(customer, NAME_QUALITY);
		Assert.assertEquals(customer, conditionType.customer());
		Assert.assertEquals(NAME_QUALITY, conditionType.name());
	}
	
	@Test
	public final void hash() {
		final ConditionTypeImpl conditionType = new ConditionTypeImpl(customer, NAME_QUALITY);
		
		Assert.assertEquals(customer.hashCode() + NAME_QUALITY.hashCode(), conditionType.hashCode());
		ReflectionTestUtils.setField(conditionType, CUSTOMER_FIELD , null);
		Assert.assertEquals(System.identityHashCode(conditionType), conditionType.hashCode());
		ReflectionTestUtils.setField(conditionType, CUSTOMER_FIELD , customer);
		ReflectionTestUtils.setField(conditionType, NAME_FIELD , null);
		Assert.assertEquals(System.identityHashCode(conditionType), conditionType.hashCode());
	}
	
	@Test
	public final void equals() {
		final ConditionTypeImpl conditionType = new ConditionTypeImpl(customer, NAME_QUALITY);
		Assert.assertFalse(conditionType.equals(NAME_QUALITY));
		Assert.assertFalse(conditionType.equals(new ConditionTypeImpl(Mockito.mock(Customer.class), NAME_QUALITY)));
		Assert.assertFalse(conditionType.equals(new ConditionTypeImpl( customer, "other")));
		Assert.assertTrue(conditionType.equals(new ConditionTypeImpl( customer, NAME_QUALITY)));
		ReflectionTestUtils.setField(conditionType, NAME_FIELD, null);
		Assert.assertFalse(conditionType.equals(new ConditionTypeImpl(customer, NAME_QUALITY)));
		Assert.assertFalse(new ConditionTypeImpl(customer, NAME_QUALITY).equals(conditionType));
	}
	@Test
	public final void defaultConstructoer() {
		Assert.assertTrue(BeanUtils.instantiateClass(ConditionTypeImpl.class) instanceof ConditionTypeImpl);
		
	}

}

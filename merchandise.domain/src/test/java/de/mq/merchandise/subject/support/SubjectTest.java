package de.mq.merchandise.subject.support;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;

public class SubjectTest {
	
	private static final String NAME_FIELD = "name";

	private static final String CUSTOMER_FIELD = "customer";

	private static final String CONDITIONS_FIELD = "conditions";

	private static final String DATA_TYPE_FIELD = "dataType";

	private static final String CONDITION_TYPE_FIELD = "conditionType";

	private static final String CONDITION_TYPE = "date";

	private final Customer customer = Mockito.mock(Customer.class);
	
	private final Condition<?> condition = Mockito.mock(Condition.class); 
	
	private final String NAME = "Pets4You"; 
	
	private final String DESCRIPTION = "Escortservice ..." ;
	@Test
	public final void create() {
		final Subject subject = new SubjectImpl(customer, NAME, DESCRIPTION); 
		
		Assert.assertEquals(NAME, subject.name());
		Assert.assertEquals(DESCRIPTION, subject.description());
		Assert.assertEquals(customer, subject.customer());
	}
	
	@Test
	public final void createName() {
		final Subject subject = new SubjectImpl(customer, NAME); 
		
		Assert.assertEquals(NAME, subject.name());
		Assert.assertNull(subject.description());
		Assert.assertEquals(customer, subject.customer());
	}
	
	@Test
	public final void assignCondition() {
		final Subject subject = new SubjectImpl(customer, NAME); 
		subject.add(CONDITION_TYPE, ConditionDataType.String);
		Assert.assertEquals(1, subject.conditions().size());
		final Condition<?> condition = subject.conditions().stream().findFirst().get();
		Assert.assertEquals(CONDITION_TYPE, ReflectionTestUtils.getField(condition, CONDITION_TYPE_FIELD));
		Assert.assertEquals(ConditionDataType.String, ReflectionTestUtils.getField(condition, DATA_TYPE_FIELD));
		
		subject.add(CONDITION_TYPE, ConditionDataType.String);
		Assert.assertEquals(1, subject.conditions().size());
		Assert.assertEquals(condition, subject.conditions().stream().findFirst().get());
	}
	
	
	@Test
	public final void condition() {
		final Subject subject = new SubjectImpl(customer, NAME); 
		
	
		final Map<String, Condition<?>> conditions = conditions(subject);
		conditions.put(CONDITION_TYPE, condition);
		Assert.assertEquals(condition, subject.condition(CONDITION_TYPE));
	}

	@SuppressWarnings("unchecked")
	private Map<String, Condition<?>> conditions(final Subject subject) {
		return (Map<String, Condition<?>>) ReflectionTestUtils.getField(subject, CONDITIONS_FIELD);
	}
	
	@Test(expected=InvalidDataAccessApiUsageException.class)
	public final void  conditionNotAware() {
		final Subject subject = new SubjectImpl(customer, NAME); 
		subject.condition(CONDITION_TYPE);
	}
	
	@Test
	public final void remove() {
		final Subject subject = new SubjectImpl(customer, NAME); 
		conditions(subject).put(CONDITION_TYPE, condition);
		Assert.assertEquals(1, subject.conditions().size());
		subject.remove(CONDITION_TYPE);
		Assert.assertTrue(subject.conditions().isEmpty());
	}
	
	@Test
	public final void  conditionType() {
		final Subject subject = new SubjectImpl(customer, NAME); 
		conditions(subject).put(CONDITION_TYPE, condition);
		final Collection<String> results = subject.conditionTypes();
		Assert.assertEquals(1, results.size());
		
		Assert.assertTrue(results.stream().findFirst().isPresent());
		Assert.assertEquals(CONDITION_TYPE, results.stream().findFirst().get());
	}
	
	@Test
	public final void hash() {
		final Subject subject = new SubjectImpl(customer, NAME); 
		
		Assert.assertEquals(customer.hashCode() + NAME.hashCode(), subject.hashCode());
		ReflectionTestUtils.setField(subject, CUSTOMER_FIELD, null);
	
		Assert.assertEquals(System.identityHashCode(subject), subject.hashCode());
		ReflectionTestUtils.setField(subject, CUSTOMER_FIELD, customer);
		
		ReflectionTestUtils.setField(subject, NAME_FIELD, null);
		Assert.assertEquals(System.identityHashCode(subject), subject.hashCode());
		
		
	}
	
	@Test
	public final void equals() {
		final Subject subject = new SubjectImpl(customer, NAME); 
		
		Assert.assertTrue(subject.equals(new SubjectImpl(customer, NAME)));
		Assert.assertFalse(subject.equals(new SubjectImpl(customer, "other")));
		Assert.assertFalse(subject.equals(new SubjectImpl(Mockito.mock(Customer.class), NAME)));
		Assert.assertFalse(subject.equals(NAME));
		ReflectionTestUtils.setField(subject, NAME_FIELD, null);
		Assert.assertFalse(subject.equals(new SubjectImpl(customer, NAME)));
		Assert.assertTrue(subject.equals(subject));
	}
	
	@Test
	public final void defaultConstructor() {
		Assert.assertTrue(BeanUtils.instantiateClass(SubjectImpl.class) instanceof Subject);
	}
	
	@Test
	public final void created() {
		final Subject subject = new SubjectImpl(customer, NAME); 
		Assert.assertNotNull(subject.created());
		Date date = new Date();
		Assert.assertEquals( subject.created().getTime() ,  date.getTime() );
		
		
	}

}

package de.mq.merchandise.subject.support;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.SubjectModel.EventType;
import de.mq.merchandise.support.Mapper;
import de.mq.merchandise.util.Observer;

public class SubjectModelTest {

	private static final String CONDITION_TYPE_FIELD = "conditionTypes";
	private static final String CONDITION_TYPE_QUALITY = "Quality";
	private static final String DATYTYPE_FIELD = "dataTypes";
	private static final String CONDITION_FIELD = "condition";
	private static final long ID = 19680528L;
	private static final String SUBJECT_FIELD = "subject";
	private static final String CUSTOMER_FIELD = "customer";
	private static final String ID_FIELD = "id";
	private static final long CUSTOMER_ID = 19680528L;

	private SubjectEventFascade subjectEventFascade = Mockito.mock(SubjectEventFascade.class);
	
	private final Subject subject = Mockito.mock(Subject.class);
	@SuppressWarnings("unchecked")
	private final Mapper<Customer, Subject> customerIntoSubjectMapper = Mockito.mock(Mapper.class);
	
	@SuppressWarnings("unchecked")
	final Observer<SubjectModel.EventType> observer = Mockito.mock(Observer.class);

	private final SubjectModel subjectModel = new SubjectModelImpl(subjectEventFascade, customerIntoSubjectMapper);

	@Test
	public final void create() {

		Assert.assertTrue(subjectModel.getSearchCriteria() instanceof SubjectImpl);
		Assert.assertNull(subjectModel.getSearchCriteria().customer());

	}

	@Test
	public final void setSerachCriteria() {
		final Customer customer = Mockito.mock(Customer.class);

		subjectModel.register(observer, SubjectModel.EventType.SearchCriteriaChanged);
		ReflectionTestUtils.setField(subjectModel, CUSTOMER_FIELD, customer);

		final Subject subject = BeanUtils.instantiateClass(SubjectImpl.class);

		subjectModel.setSerachCriteria(subject);

		Assert.assertEquals(customer, subjectModel.getSearchCriteria().customer());
		Assert.assertEquals(subject, subjectModel.getSearchCriteria());
		Mockito.verify(observer).process(SubjectModel.EventType.SearchCriteriaChanged);

	}

	@Test
	public final void setCustomer() {

		subjectModel.register(observer, SubjectModel.EventType.SearchCriteriaChanged);
		final Subject subject = BeanUtils.instantiateClass(SubjectImpl.class);
		ReflectionTestUtils.setField(subjectModel, "searchCriteria", subject);
		final Customer customer = Mockito.mock(Customer.class);
		Mockito.when(customer.id()).thenReturn(Optional.of(CUSTOMER_ID));

		subjectModel.setCustomer(customer);

		Assert.assertEquals(customer, subjectModel.getSearchCriteria().customer());
		Mockito.verify(observer).process(SubjectModel.EventType.SearchCriteriaChanged);
	}

	@Test
	public final void setCustomerNotChanged() {

		Customer existingCustomer = (Customer) ReflectionTestUtils.getField(subjectModel, CUSTOMER_FIELD);
		Field idField = ReflectionUtils.findField(CustomerImpl.class, ID_FIELD);
		idField.setAccessible(true);
		ReflectionUtils.setField(idField, existingCustomer, CUSTOMER_ID);
		subjectModel.register(observer, SubjectModel.EventType.SearchCriteriaChanged);
		final Customer customer = Mockito.mock(Customer.class);

		subjectModel.setCustomer(customer);

		Assert.assertFalse(customer.equals(ReflectionTestUtils.getField(subjectModel, CUSTOMER_FIELD)));
		Mockito.verify(observer, Mockito.times(0)).process(SubjectModel.EventType.SearchCriteriaChanged);

	}

	@Test
	public final void setSubjectId() {

		subjectModel.register(observer, SubjectModel.EventType.SubjectChanged);

		Mockito.when(subjectEventFascade.subjectChanged(ID)).thenReturn(subject);
		subjectModel.setSubjectId(19680528L);

		Assert.assertEquals(subject, ReflectionTestUtils.getField(subjectModel, SUBJECT_FIELD));
		Mockito.verify(observer).process(Mockito.any(SubjectModel.EventType.class));
	}

	@Test
	public final void setSubjectIdNull() {

		subjectModel.register(observer, SubjectModel.EventType.SubjectChanged);
		subjectModel.setSubjectId(null);
		Mockito.verify(observer).process(Mockito.any(SubjectModel.EventType.class));
		final Subject newSubject = (Subject) ReflectionTestUtils.getField(subjectModel, SUBJECT_FIELD);

		Assert.assertFalse(newSubject.id().isPresent());
		Assert.assertNotSame(subject, newSubject);
		Assert.assertTrue(newSubject instanceof SubjectImpl);

	}

	@Test
	public final void events() {
		Arrays.asList(SubjectModel.EventType.values()).forEach(col -> Assert.assertEquals(col, SubjectModel.EventType.valueOf(col.name())));
		Assert.assertEquals(9, SubjectModel.EventType.values().length);
	}

	@Test
	public final void getSubject() {
		Assert.assertEquals(Optional.of(ReflectionTestUtils.getField(subjectModel, SUBJECT_FIELD)), subjectModel.getSubject());
		ReflectionTestUtils.setField(subjectModel, SUBJECT_FIELD, subject);
		Assert.assertEquals(Optional.of(subject), subjectModel.getSubject());

	}
	
	@Test
	public final void setConditionId() {
		final Condition condition = Mockito.mock(Condition.class);
		Mockito.when(condition.conditionDataType()).thenReturn(ConditionDataType.IntegralNumber);
		Mockito.when(condition.conditionType()).thenReturn(CONDITION_TYPE_QUALITY);
		
		Mockito.when(subjectEventFascade.conditionChanged(ID)).thenReturn(condition);
	
		@SuppressWarnings("unchecked")
		final Collection<ConditionDataType> dataTypes = (Collection<ConditionDataType>) ReflectionTestUtils.getField(subjectModel, DATYTYPE_FIELD);
		dataTypes.clear();
		
		@SuppressWarnings("unchecked")
		final Collection<String> conditionTypes = (Collection<String>) ReflectionTestUtils.getField(subjectModel, CONDITION_TYPE_FIELD);
		conditionTypes.clear();
		
		@SuppressWarnings("unchecked")
		final Observer<SubjectModel.EventType> observer = Mockito.mock(Observer.class);
		subjectModel.register(observer, EventType.ConditionChanged);
		
		
		subjectModel.setConditionId(ID);
		final Optional<ConditionDataType> dataType = dataTypes.stream().findFirst();
		Assert.assertTrue(dataType.isPresent());
		Assert.assertEquals(ConditionDataType.IntegralNumber, dataType.get());
		
		final Optional<String> conditionType = conditionTypes.stream().findFirst();
		Assert.assertTrue(conditionType.isPresent());
		Assert.assertEquals(CONDITION_TYPE_QUALITY, conditionType.get());
		
		final Condition result = (Condition) ReflectionTestUtils.getField(subjectModel, CONDITION_FIELD);
		Assert.assertEquals(condition, result);
		
		
		Mockito.verify(observer).process(Mockito.any(EventType.class));
		
		
	}
	
	@Test
	public final void setConditionIdNull() {
		
		
		subjectModel.register(observer, EventType.ConditionChanged);
		subjectModel.setConditionId(null);
		
		final Condition result = (Condition) ReflectionTestUtils.getField(subjectModel, CONDITION_FIELD);
		
		
		Assert.assertTrue(result instanceof ConditionImpl);
			
		Mockito.verify(observer).process(Mockito.any(EventType.class));
	}
	
	@Test
	public final void saveSubject() {
		final Subject subject = Mockito.mock(Subject.class);
		Mockito.when(this.subject.id()).thenReturn(Optional.of(ID));
		
		ReflectionTestUtils.setField(subjectModel, SUBJECT_FIELD, this.subject);
		subjectModel.register(observer, EventType.SubjectChanged);
		subjectModel.save(subject);
		
		Mockito.verify(customerIntoSubjectMapper).mapInto((Customer) ReflectionTestUtils.getField(subjectModel,CUSTOMER_FIELD), subject);
		
		Mockito.verify(subjectEventFascade).save(ID, subject);
		
		
		Mockito.verify(observer).process(Mockito.any(EventType.class));
		
		final Subject result = (Subject) ReflectionTestUtils.getField(subjectModel, SUBJECT_FIELD);
		
		
		Assert.assertTrue(result instanceof SubjectImpl);
	}
	
	@Test
	public final void saveCondition() {
		
		final Subject result = Mockito.mock(Subject.class);
		Mockito.when(this.subject.id()).thenReturn(Optional.of(ID));
		
		ReflectionTestUtils.setField(subjectModel, SUBJECT_FIELD, this.subject);
		final Condition condition = Mockito.mock(Condition.class);
		
		Mockito.when(subjectEventFascade.save(condition, subject.id().get())).thenReturn(result);
		subjectModel.register(observer, EventType.ConditionChanged);
		subjectModel.save(condition);
		
		Assert.assertEquals(result, ReflectionTestUtils.getField(subjectModel, SUBJECT_FIELD));
		
		Assert.assertTrue((Condition) ReflectionTestUtils.getField(subjectModel, CONDITION_FIELD) instanceof ConditionImpl);
		
		
		Mockito.verify(observer).process(Mockito.any(EventType.class));
	}
	
	@Test
	public final void getCondition() {
		final Condition condition = Mockito.mock(Condition.class);
		ReflectionTestUtils.setField(subjectModel, CONDITION_FIELD, condition);
		Assert.assertTrue(subjectModel.getCondition().isPresent());
		
		Assert.assertEquals(condition, subjectModel.getCondition().get());
		
		ReflectionTestUtils.setField(subjectModel, CONDITION_FIELD, null);
		Assert.assertFalse(subjectModel.getCondition().isPresent());
	}

}

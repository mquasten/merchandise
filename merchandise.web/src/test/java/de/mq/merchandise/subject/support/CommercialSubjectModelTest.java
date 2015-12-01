package de.mq.merchandise.subject.support;




import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;








import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;










import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.CommercialSubjectModel.EventType;
import de.mq.merchandise.support.Mapper;
import de.mq.util.event.Observer;

public class CommercialSubjectModelTest {
	
	private static final long NOT_PERSISTENT_ID = -1L;

	private static final String SEARCH_FIELD = "search";

	private static final String COMMERCIAL_SUBJECT_FIELD = "commercialSubject";

	private static final long ID = 19680528L;

	private final CommercialSubject search = Mockito.mock(CommercialSubject.class);
	
	private final CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);
	private final CommercialSubjectEventFascade commercialSubjectEventFascade = Mockito.mock(CommercialSubjectEventFascade.class);
	
	@SuppressWarnings("unchecked")
	private final  Mapper<Customer, CommercialSubject> customerIntoSubjectMapper = Mockito.mock(Mapper.class);
	
	private final CommercialSubjectModel model = new CommercialSubjectModelImpl(search, commercialSubject, commercialSubjectEventFascade, customerIntoSubjectMapper);
	
	private final Customer customer = Mockito.mock(Customer.class);
	
	@SuppressWarnings("unchecked")
	private final Observer<EventType> observer = Mockito.mock(Observer.class);
	
	@Before
	public final void setUp() {
		ReflectionUtils.doWithFields(model.getClass(), field -> {field.setAccessible(true);ReflectionUtils.setField(field, model, customer);} , Field -> Field.getType().equals(Customer.class));
	   Mockito.when(commercialSubject.id()).thenReturn(Optional.of(ID));
	}
	
	@Test
	public final void setSearch() {
		
		
		model.register(observer, EventType.SearchCriteriaChanged);
		CommercialSubject search = Mockito.mock(CommercialSubject.class);
		Assert.assertEquals(this.search,ReflectionTestUtils.getField(model, SEARCH_FIELD));
		model.setSearch(search);
		Assert.assertEquals(search,ReflectionTestUtils.getField(model, SEARCH_FIELD));
		
		
		Mockito.verify(customerIntoSubjectMapper).mapInto(customer, search);
		Mockito.verify(observer).process(EventType.SearchCriteriaChanged);
	}
	
	@Test
	public final void getSearch() {
		Assert.assertEquals(search, model.getSearch());
	}
	
	@Test
	public final void save() {
		
		model.register(observer, EventType.CommericalSubjectChanged);
		CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);
		
		Assert.assertEquals(this.commercialSubject, (CommercialSubject) ReflectionTestUtils.getField(model, COMMERCIAL_SUBJECT_FIELD));
		model.save(commercialSubject);
		
		Assert.assertEquals(CommercialSubjectImpl.class,  ReflectionTestUtils.getField(model, COMMERCIAL_SUBJECT_FIELD).getClass());
		
		
		Mockito.verify( customerIntoSubjectMapper).mapInto(customer, commercialSubject);
		Mockito.verify(commercialSubjectEventFascade).save(this.commercialSubject.id().get(), commercialSubject);
		
		Mockito.verify(observer).process(EventType.CommericalSubjectChanged);
	}
	
	
	@Test
	public final void setCustomer() {
		final Customer customer = Mockito.mock(Customer.class);
	
		model.register(observer, EventType.SearchCriteriaChanged);
		model.setCustomer(customer);
		
		ReflectionUtils.doWithFields(model.getClass(), field -> Assert.assertEquals(customer, ReflectionUtils.getField(field, model)), field -> field.getType().equals(Customer.class));
	   
		Mockito.verify(customerIntoSubjectMapper).mapInto(customer, this.search);
		Mockito.verify(observer).process(EventType.SearchCriteriaChanged);
	
	}
	
	
	@Test
	public final void setCommercialSubjectId() {
		
		model.register(observer, EventType.CommericalSubjectChanged);
		CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);
		Mockito.when(commercialSubjectEventFascade.commercialSubjectChanged(ID)).thenReturn(commercialSubject);
		model.setCommercialSubjectId(ID);
		
		Assert.assertEquals(commercialSubject, ReflectionTestUtils.getField(model, COMMERCIAL_SUBJECT_FIELD));
		Mockito.verify(observer).process(EventType.CommericalSubjectChanged);
		
	}
	
	
	@Test
	public final void setCommercialSubjectItemId() {
		model.register(observer, EventType.CommericalSubjectItemChanged);
		final CommercialSubjectItem item = Mockito.mock(CommercialSubjectItem.class);
		Mockito.when(commercialSubjectEventFascade.commericalSubjectItemChanged(ID)).thenReturn(item);
		model.setCommercialSubjectItemId(ID);
		
		Mockito.verify(observer).process(EventType.CommericalSubjectItemChanged);
		
		
		ReflectionUtils.doWithFields(model.getClass(), field -> {field.setAccessible(true); Assert.assertEquals(item, ReflectionUtils.getField(field, model));} , field -> field.getType().equals(CommercialSubjectItem.class));
		
		
	}
	
	@Test
	public final void setCommercialSubjectItemIdNull() {
		model.register(observer, EventType.CommericalSubjectItemChanged);
		model.setCommercialSubjectItemId(null);
		
		Mockito.verify(observer).process(EventType.CommericalSubjectItemChanged);
		ReflectionUtils.doWithFields(model.getClass(), field -> {field.setAccessible(true); Assert.assertEquals(CommercialSubjectItemImpl.class, ReflectionUtils.getField(field, model).getClass());} , field -> field.getType().equals(CommercialSubjectItem.class));
		
	}
	
	@Test
	public final void getConditions() {
		final CommercialSubjectItem item = Mockito.mock(CommercialSubjectItem.class);
		final Subject subject = Mockito.mock(Subject.class);
		final Collection<Condition> conditions = new ArrayList<>();
		final Condition condition = Mockito.mock(Condition.class);
		conditions.add(condition);
		Mockito.when(subject.conditions()).thenReturn(conditions);
		Mockito.when(item.subject()).thenReturn(subject);
		
		ReflectionUtils.doWithFields(model.getClass(), field -> {field.setAccessible(true); ReflectionUtils.setField(field, model, item);} , field -> field.getType().equals(CommercialSubjectItem.class));
		
		Assert.assertEquals(conditions, model.getConditions());
	}
	
	@Test
	public final void getCommercialSubject() {
		final CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);
		ReflectionTestUtils.setField(model, COMMERCIAL_SUBJECT_FIELD, commercialSubject);
		
		Assert.assertEquals(commercialSubject, model.getCommercialSubject().get());
	}
	
	@Test
	public final void getSubjects() {
		final Collection<Subject> subjects = new ArrayList<>();
		final Subject subject = Mockito.mock(Subject.class);
		subjects.add(subject);
		Mockito.when(commercialSubjectEventFascade.subjects(customer)).thenReturn(subjects);
		
		Assert.assertEquals(subjects, model.getSubjects());
	}
	
	
	@Test
	public final void  getCommercialSubjectItem() {
		final CommercialSubjectItem item = Mockito.mock(CommercialSubjectItem.class);
		ReflectionUtils.doWithFields(model.getClass(), field -> {field.setAccessible(true); ReflectionUtils.setField(field, model, item);} , field -> field.getType().equals(CommercialSubjectItem.class));
		Assert.assertEquals(item, model.getCommercialSubjectItem().get());
	}
	
	@Test
	public final void   delete() {
		model.register(observer, EventType.CommericalSubjectChanged);
		final CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);
		
		model.delete(commercialSubject);
		
		Mockito.verify(commercialSubjectEventFascade).delete(commercialSubject);
		Mockito.verify(observer).process(EventType.CommericalSubjectChanged);
		
		Assert.assertEquals(CommercialSubjectImpl.class, ReflectionTestUtils.getField(model, COMMERCIAL_SUBJECT_FIELD).getClass());
	}
	
	@Test
	public final void saveItem(){
		model.register(observer,  EventType.CommericalSubjectItemChanged);
		final CommercialSubjectItem commercialSubjectItem = Mockito.mock(CommercialSubjectItem.class);
		Mockito.when(commercialSubject.id()).thenReturn(Optional.of(ID));
		Mockito.when(commercialSubjectEventFascade.save(commercialSubjectItem, ID )).thenReturn(commercialSubject);
		model.save(commercialSubjectItem);
		
		Assert.assertEquals(commercialSubject, ReflectionTestUtils.getField(model, COMMERCIAL_SUBJECT_FIELD));
		Mockito.verify(observer).process(EventType.CommericalSubjectItemChanged);
		
		
		
	}
	
	@Test
	public final void deleteItem() {
		model.register(observer,  EventType.CommericalSubjectItemChanged);
		final CommercialSubjectItem commercialSubjectItem = Mockito.mock(CommercialSubjectItem.class);
		Mockito.when(commercialSubject.id()).thenReturn(Optional.of(ID));
		Mockito.when(commercialSubjectEventFascade.delete(commercialSubjectItem, ID )).thenReturn(commercialSubject);
		model.delete(commercialSubjectItem);
		
		Assert.assertEquals(commercialSubject, ReflectionTestUtils.getField(model, COMMERCIAL_SUBJECT_FIELD));
		Mockito.verify(observer).process(EventType.CommericalSubjectItemChanged);
	}
	
	@Test
	public final void  setCondition() {
		model.register(observer, EventType.ConditionChanged);
		final CommercialSubjectItemConditionImpl   commercialSubjectItemCondition = Mockito.mock(CommercialSubjectItemConditionImpl.class);
		
		Mockito.when(commercialSubjectEventFascade.conditionChanged(ID)).thenReturn(commercialSubjectItemCondition);
		
		model.setCondition(ID);
		
		ReflectionUtils.doWithFields(model.getClass(), field -> {field.setAccessible(true); Assert.assertEquals(commercialSubjectItemCondition, field.get(model)); }, field -> field.getType().equals(CommercialSubjectItemConditionImpl.class));
		
		
		Mockito.verify(observer).process(EventType.ConditionChanged);
	}
	
	@Test
	public final void  setConditionNotPersustent() {
		final CommercialSubjectItemConditionImpl[]   commercialSubjectItemCondition =  new CommercialSubjectItemConditionImpl[]{null};
		model.register(observer, EventType.ConditionChanged);
		model.setCondition(NOT_PERSISTENT_ID);
		
		ReflectionUtils.doWithFields(model.getClass(), field -> {field.setAccessible(true); commercialSubjectItemCondition[0]= (CommercialSubjectItemConditionImpl) field.get(model); }, field -> field.getType().equals(CommercialSubjectItemConditionImpl.class));
		
		Assert.assertEquals(ConditionImpl.class, commercialSubjectItemCondition[0].condition().getClass());
		Assert.assertEquals(NOT_PERSISTENT_ID, (long) commercialSubjectItemCondition[0].condition().id().get());
		Mockito.verify(observer).process(EventType.ConditionChanged);
	}

}

package de.mq.merchandise.subject.support;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import javax.persistence.Id;

import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.support.Mapper;
import de.mq.util.application.et.ExceptionTranslatorOperations;
import de.mq.util.application.et.ExceptionTranslatorOperations.ET;
import de.mq.util.event.support.ObservableImpl;


class CommercialSubjectModelImpl extends ObservableImpl<CommercialSubjectModel.EventType>  implements CommercialSubjectModel   {
	
	private  CommercialSubject search;
	
	private Customer customer;

	private CommercialSubject commercialSubject;
	
	private CommercialSubjectItem commercialSubjectItem = BeanUtils.instantiateClass(CommercialSubjectItemImpl.class) ; 
	
	private final CommercialSubjectEventFascade commercialSubjectEventFascade;
	
	private final  Mapper<Customer,CommercialSubject> customerMapper; 
	
	private CommercialSubjectItemConditionImpl commercialSubjectItemCondition;
	
	private ExceptionTranslatorOperations exceptionTranslatorOperations;
	private String inputValue;
	
	
	private Optional<String> currentInputValue = Optional.empty();

	CommercialSubjectModelImpl(final CommercialSubject search, final CommercialSubject commercialSubject, final CommercialSubjectEventFascade commercialSubjectEventFascade,  final  Mapper<Customer, CommercialSubject> customerIntoSubjectMapper, final ExceptionTranslatorOperations exceptionTranslatorOperations) {
		this.search = search;
		this.commercialSubjectEventFascade=commercialSubjectEventFascade;
		this.commercialSubject= commercialSubject;
		this.customerMapper=customerIntoSubjectMapper;
		this.exceptionTranslatorOperations=exceptionTranslatorOperations;
	}


	@Override
	public  final  void setSearch(final CommercialSubject search) {
		this.search=search;
		customerMapper.mapInto(customer, this.search);
		notifyObservers(EventType.SearchCriteriaChanged);
	}
	
	
	@Override
	public final  CommercialSubject getSearch() {
		
		return this.search;
	}
	
	@Override
	public final void save(final CommercialSubject commercialSubject) {
		customerMapper.mapInto(customer, commercialSubject);
	   commercialSubjectEventFascade.save(this.commercialSubject.id().orElse(null), commercialSubject);
		
		setCommercialSubjectId(null);
	}



	@Override
	public final void setCustomer(Customer customer) {
		this.customer=customer;
		setSearch(search);
		
	}


	@Override
	public final void setCommercialSubjectId(final Long commercialSubjectId) {
		if( commercialSubjectId==null){
			commercialSubject= BeanUtils.instantiateClass(CommercialSubjectImpl.class);
			notifyObservers(EventType.CommericalSubjectChanged);
			return;
		}
				
		commercialSubject= commercialSubjectEventFascade.commercialSubjectChanged(commercialSubjectId);
		Assert.notNull(commercialSubject, "CommercialSubject should be returned" );
		notifyObservers(EventType.CommericalSubjectChanged);
		
	}
	
	@Override
	public final  void setCommercialSubjectItemId(final Long itemId) {
		if( itemId==null){
			commercialSubjectItem= BeanUtils.instantiateClass(CommercialSubjectItemImpl.class);
			notifyObservers(EventType.CommericalSubjectItemChanged);
			return;
		}
		commercialSubjectItem = commercialSubjectEventFascade.commericalSubjectItemChanged(itemId);
		Assert.notNull(commercialSubjectItem);
		
		
		Assert.notNull(commercialSubjectItem, "Condition should be returned" );
		notifyObservers(EventType.CommericalSubjectItemChanged);
	
		
	}
	@Override
	public final  Collection<Condition> getConditions() {
		return commercialSubjectItem.subject().conditions();
	}
	
	
	@Override
	public final  Optional<CommercialSubject> getCommercialSubject() {
		return Optional.ofNullable(commercialSubject);
	}
	
	@Override
	public final  void delete(final CommercialSubject commercialSubject) {
		commercialSubjectEventFascade.delete(commercialSubject);
		setCommercialSubjectId(null);
	}
	
	@Override
	public final  Collection<Subject> getSubjects() {
		
		return commercialSubjectEventFascade.subjects(customer);
		
	}
	
	@Override
	public final   Optional<CommercialSubjectItem> getCommercialSubjectItem() {
		return Optional.ofNullable(commercialSubjectItem);
	}
	
	
	@Override
	public final  void save(final CommercialSubjectItem commercialSubjectItem) {
	
		Assert.isTrue(commercialSubject.id().isPresent(), "commercialSubject should be persistent");
	
		commercialSubject= commercialSubjectEventFascade.save(commercialSubjectItem, commercialSubject.id().get() );
		
		setCommercialSubjectItemId(null);
		
	}


	@Override
	public final void delete(CommercialSubjectItem commercialSubjectItem) {
		Assert.isTrue(commercialSubject.id().isPresent(), "CommercialSubject should be persistent");
		commercialSubject=commercialSubjectEventFascade.delete(commercialSubjectItem, commercialSubject.id().get());
		setCommercialSubjectItemId(null);
		
	}

	@Override
	public final   void setCondition(final Long conditionId) {
		
		
		if(conditionId  < 0 ) {
			
			commercialSubjectItemCondition=newCommercialSubjectItemCondition(); 
			notifyObservers(EventType.ConditionChanged);
			return;
		} 
		
		commercialSubjectItemCondition=commercialSubjectEventFascade.conditionChanged(conditionId);
		notifyObservers(EventType.ConditionChanged);
	}


	private CommercialSubjectItemConditionImpl newCommercialSubjectItemCondition() {
		CommercialSubjectItemConditionImpl commercialSubjectItemCondition=BeanUtils.instantiateClass(CommercialSubjectItemConditionImpl.class);
		final Condition condition = BeanUtils.instantiateClass(ConditionImpl.class);
		ReflectionUtils.doWithFields(condition.getClass(), field -> {field.setAccessible(true); ReflectionUtils.setField(field, condition, -1L);} , field -> field.isAnnotationPresent(Id.class));
		ReflectionUtils.doWithFields(CommercialSubjectItemConditionImpl.class, field ->  { field.setAccessible(true); ReflectionUtils.setField(field, commercialSubjectItemCondition, condition);} , field -> field.getType().equals(Condition.class));
	   return commercialSubjectItemCondition;
	}
	
	@Override
	public final   boolean hasCondition() {
		return commercialSubjectItemCondition.condition().id().orElse(-1L) > 0;
		
	}
	
	@Override
	public final String getInputValue() {
		return inputValue;
	}

	@Override
	public final void addInputValue(final Long conditionId) {
		
		commercialSubjectItem=commercialSubjectEventFascade.addInputValue(this, conditionId);
		notifyObservers(EventType.ConditionChanged);
	}
	
	
	 
	@Override
	@SuppressWarnings("unchecked")
	public final <T> Collection<T> inputValues(final Long conditionId) {
		final Collection<T> values = new ArrayList<>();
		commercialSubjectItem.conditionValues().stream().filter(entry -> entry.getKey().id().get().equals(conditionId)).forEach(entry -> values.addAll( (Collection<? extends T>) entry.getValue()));
		return Collections.unmodifiableCollection(values);
	 }
	
	@Override
	public final void setCurrentInputValue(final String value) {
		currentInputValue=Optional.ofNullable(value);
		notifyObservers(EventType.InputValueChanged);
	}
	
	@Override
	public final boolean hasCurrentInputValue() {
		return currentInputValue.isPresent();
	}


	@Override
	public void deleteInputValue() {
		Assert.isTrue(currentInputValue.isPresent());
		Assert.notNull(commercialSubjectItemCondition.condition());
		Assert.isTrue(commercialSubjectItemCondition.condition().id().isPresent());
		
		commercialSubjectItem=commercialSubjectEventFascade.deleteInputValue(commercialSubjectItemCondition.condition().id().get(), currentInputValue.get());
		notifyObservers(EventType.ConditionChanged);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T convertConditionValue(final String value, final Long conditionId)   {
		return  exceptionTranslatorOperations.doInTranslationWithResult(ET.newConfiguration().translate(NoSuchMethodException.class).to(IllegalStateException.class).translate(BeanInstantiationException.class).using((m,s) ->  (s.getCause() instanceof NumberFormatException) ? (NumberFormatException) s.getCause() : (BeanInstantiationException) s ).done(), () -> BeanUtils.instantiateClass((Constructor<T>) getCondition(conditionId).conditionDataType().targetClass.getConstructor(String.class), value));
	}

	@Override
	public Condition getCondition(final Long conditionId) {
	
		final Optional<Condition> condition = getConditions().stream().filter(c -> c.id().get().equals(conditionId)).findAny();
		Assert.isTrue(condition.isPresent(), "Condition not exists.");
		return condition.get();
	}

	@Override
	public boolean canConvertConditionValue(final String value, final Long conditionId) {
		try {
			convertConditionValue(value, conditionId);
			return true;
		} catch ( final NumberFormatException nf) {
			return false;
		}
	}
	
}

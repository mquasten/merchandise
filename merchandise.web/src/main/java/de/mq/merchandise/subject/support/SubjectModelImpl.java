package de.mq.merchandise.subject.support;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.support.Mapper;
import de.mq.merchandise.util.support.ObservableImpl;


class SubjectModelImpl extends ObservableImpl<SubjectModel.EventType> implements SubjectModel {
	private Subject searchCriteria; 
	private Customer customer;
	
	private Subject subject;

	private Condition condition;

	private final SubjectEventFascade subjectEventFascade;
	private final Mapper<Customer,Subject> customerIntoSubjectMapper;
	
	private final Collection<ConditionDataType> dataTypes = new HashSet<>();
	
	


	private final Collection<String> conditionTypes = new HashSet<>();
	
	SubjectModelImpl(final SubjectEventFascade subjectEventFascade, final Mapper<Customer,Subject> customerIntoSubjectMapper) {
		searchCriteria=BeanUtils.instantiateClass(SubjectImpl.class, Subject.class);
		customer=BeanUtils.instantiateClass(CustomerImpl.class, Customer.class);
		this.subjectEventFascade=subjectEventFascade;
		this.customerIntoSubjectMapper=customerIntoSubjectMapper;
		this.subject= BeanUtils.instantiateClass(SubjectImpl.class);
		this.condition= BeanUtils.instantiateClass(ConditionImpl.class);
		this.dataTypes.addAll(Arrays.asList(ConditionDataType.values()));
		
	}


	@Override
	public final Subject getSearchCriteria() {
		return searchCriteria; 
	}
	
	@Override
	public final void setCustomer(final Customer customer) {
		Assert.notNull(customer, "Customer is mandatory");
		if( this.customer.id().isPresent()){
			return ;
		}
		this.customer=customer;
		conditionTypes.clear();
		conditionTypes.addAll(customer.conditionTypes());
		setSerachCriteria(searchCriteria);
	}
	@Override
	public final void setSerachCriteria(final Subject searchCriteria) {
		Assert.notNull(searchCriteria, "SearchCriteria is mandatory");
		ReflectionUtils.doWithFields(searchCriteria.getClass(), field -> { field.setAccessible(true); ReflectionUtils.setField(field, searchCriteria, customer);}, field -> field.getType().equals(Customer.class));
		this.searchCriteria=searchCriteria;
		notifyObservers(EventType.SearchCriteriaChanged);
	}


	
	@Override
	public void setSubjectId(final Long subjectId) {
		if( subjectId==null){
			subject= BeanUtils.instantiateClass(SubjectImpl.class);
			notifyObservers(EventType.SubjectChanged);
			return;
		}
				
		subject= subjectEventFascade.subjectChanged(subjectId);
		Assert.notNull(subject, "Subject should be returned" );
		notifyObservers(EventType.SubjectChanged);
	}
	
	@Override
	public void setConditionId(final Long conditionId) {
		if( conditionId==null){
			condition= BeanUtils.instantiateClass(ConditionImpl.class);
			notifyObservers(EventType.ConditionChanged);
			return;
		}
		condition = subjectEventFascade.conditionChanged(conditionId);
		Assert.notNull(condition);
		Assert.notNull(condition.conditionDataType());
		Assert.hasText(condition.conditionType());
		this.dataTypes.add(condition.conditionDataType());
		this.conditionTypes.add(condition.conditionType());
		Assert.notNull(condition, "Condition should be returned" );
		notifyObservers(EventType.ConditionChanged);
	
		
	}
	
	
	@Override
	public void save(final Subject subject) {
		customerIntoSubjectMapper.mapInto(customer, subject);
		subjectEventFascade.save(this.subject.id().orElse(null), subject);
		setSubjectId(null);
	}


	@Override
	public void save(final Condition condition) {
		
		Assert.isTrue(subject.id().isPresent(), "Subject should be persistent");
	
		subject=subjectEventFascade.save(condition, subject.id().get());
		setConditionId(null);
	}

	@Override
	public final Optional<Subject> getSubject() {
		return Optional.ofNullable(subject);
	}
	
	@Override
	public final Optional<Condition> getCondition() {
		return Optional.ofNullable(condition);
	}
	

	@Override
	public void delete(final Subject subject) {
		subjectEventFascade.delete(subject);
		setSubjectId(null);
	}
	
	@Override
	public void delete(final Condition condition) {
		Assert.isTrue(subject.id().isPresent(), "Subject should be persistent");
		subject=subjectEventFascade.delete(condition, subject.id().get());
		setConditionId(null);
	}
	
	@Override
	public Map<ConditionCols, Collection<?>> getConditionValues() {
		final Map<ConditionCols, Collection<?>> results =   new HashMap<>();
		results.put(ConditionCols.ConditionType, conditionTypes.stream().sorted((c1,c2) -> c1.compareToIgnoreCase(c2)).collect(Collectors.toList()));
		results.put(ConditionCols.DataType, dataTypes.stream().sorted().collect(Collectors.toList()));
		return Collections.unmodifiableMap(results);
		
	}
	
}

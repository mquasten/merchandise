package de.mq.merchandise.subject.support;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.support.Mapper;
import de.mq.merchandise.util.support.ObservableImpl;

class CommercialSubjectModelImpl extends ObservableImpl<CommercialSubjectModel.EventType>  implements CommercialSubjectModel   {
	
	private  CommercialSubject search;
	
	private Customer customer;

	private CommercialSubject commercialSubject;
	
	private CommercialSubjectItem commercialSubjectItem = BeanUtils.instantiateClass(CommercialSubjectItemImpl.class) ; 
	
	private final CommercialSubjectEventFascade commercialSubjectEventFascade;
	
	private final  Mapper<Customer,CommercialSubject> customerMapper; 
	
	CommercialSubjectItemConditionImpl commercialSubjectItemCondition;
	 
	
	
	CommercialSubjectModelImpl(final CommercialSubject search, final CommercialSubject commercialSubject, final CommercialSubjectEventFascade commercialSubjectEventFascade,  final  Mapper<Customer, CommercialSubject> customerIntoSubjectMapper) {
		this.search = search;
		this.commercialSubjectEventFascade=commercialSubjectEventFascade;
		this.commercialSubject= commercialSubject;
		this.customerMapper=customerIntoSubjectMapper;
	
	}


	@Override
	public final void setSearch(final CommercialSubject search) {
		this.search=search;
		customerMapper.mapInto(customer, this.search);
		notifyObservers(EventType.SearchCriteriaChanged);
	}
	
	
	@Override
	public final CommercialSubject getSearch() {
		
		return this.search;
	}
	
	@Override
	public void save(final CommercialSubject commercialSubject) {
		customerMapper.mapInto(customer, commercialSubject);
	   commercialSubjectEventFascade.save(this.commercialSubject.id().orElse(null), commercialSubject);
		
		setCommercialSubjectId(null);
	}



	@Override
	public void setCustomer(Customer customer) {
		this.customer=customer;
		setSearch(search);
		
	}


	@Override
	public void setCommercialSubjectId(final Long commercialSubjectId) {
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
	public void setCommercialSubjectItemId(final Long itemId) {
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
	public final Collection<Condition> getConditions() {
		return commercialSubjectItem.subject().conditions();
	}
	
	
	@Override
	public final Optional<CommercialSubject> getCommercialSubject() {
		return Optional.ofNullable(commercialSubject);
	}
	
	@Override
	public final void delete(final CommercialSubject commercialSubject) {
		commercialSubjectEventFascade.delete(commercialSubject);
		setCommercialSubjectId(null);
	}
	
	@Override
	public final Collection<Subject> getSubjects() {
		
		return commercialSubjectEventFascade.subjects(customer);
		
	}
	
	@Override
	public final Optional<CommercialSubjectItem> getCommercialSubjectItem() {
		return Optional.ofNullable(commercialSubjectItem);
	}
	
	
	@Override
	public void save(final CommercialSubjectItem commercialSubjectItem) {
		
		Assert.isTrue(commercialSubject.id().isPresent(), "commercialSubject should be persistent");
	
		commercialSubject= commercialSubjectEventFascade.save(commercialSubjectItem, commercialSubject.id().get() );
		
		setCommercialSubjectItemId(null);
		
	}


	@Override
	public void delete(CommercialSubjectItem commercialSubjectItem) {
		Assert.isTrue(commercialSubject.id().isPresent(), "CommercialSubject should be persistent");
		commercialSubject=commercialSubjectEventFascade.delete(commercialSubjectItem, commercialSubject.id().get());
		setCommercialSubjectItemId(null);
		
	}

	@Override
	public final void setCondition(final Condition condition) {
		if( condition.id().orElse(-1L) <= 0 ) {
			
			commercialSubjectItemCondition=BeanUtils.instantiateClass(CommercialSubjectItemConditionImpl.class);
			ReflectionUtils.doWithFields(CommercialSubjectItemConditionImpl.class, field ->  { field.setAccessible(true); ReflectionUtils.setField(field, commercialSubjectItemCondition, condition);} , field -> field.getType().equals(Condition.class)); 
			notifyObservers(EventType.ConditionChanged);
			return;
		}
		
		commercialSubjectItemCondition=commercialSubjectEventFascade.conditionChanged(condition.id().get());
		notifyObservers(EventType.ConditionChanged);
	}
	
	@Override
	public final boolean hasCondition() {
		
		return commercialSubjectItemCondition.condition().id().orElse(-1L) > 0;
		
	}
}

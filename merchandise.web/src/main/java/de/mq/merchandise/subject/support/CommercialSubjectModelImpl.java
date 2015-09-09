package de.mq.merchandise.subject.support;

import java.lang.reflect.Field;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.util.support.ObservableImpl;

class CommercialSubjectModelImpl extends ObservableImpl<CommercialSubjectModel.EventType>  implements CommercialSubjectModel   {
	
	private  CommercialSubject search;
	
	private Customer customer;

	private CommercialSubject commercialSubject;
	
	private final CommercialSubjectEventFascade commercialSubjectEventFascade;
	
	CommercialSubjectModelImpl(final CommercialSubject search, final CommercialSubject commercialSubject, final CommercialSubjectEventFascade commercialSubjectEventFascade) {
		this.search = search;
		this.commercialSubjectEventFascade=commercialSubjectEventFascade;
		this.commercialSubject= commercialSubject;
	}


	@Override
	public final void setSearch(final CommercialSubject search) {
		this.search=search;
		final Field field = ReflectionUtils.findField(this.search.getClass(), "customer");
		Assert.notNull(field);
		field.setAccessible(true);
		
		ReflectionUtils.setField(field, this.search, customer);
		notifyObservers(EventType.SearchCriteriaChanged);
	}
	
	
	@Override
	public final CommercialSubject getSearch() {
		
		return this.search;
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
	public final Optional<CommercialSubject> getCommercialSubject() {
		return Optional.ofNullable(commercialSubject);
	}

}

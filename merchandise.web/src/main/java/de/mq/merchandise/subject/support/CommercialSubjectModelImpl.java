package de.mq.merchandise.subject.support;

import java.lang.reflect.Field;

import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;


import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.util.support.ObservableImpl;

class CommercialSubjectModelImpl extends ObservableImpl<CommercialSubjectModel.EventType>  implements CommercialSubjectModel   {
	
	private  CommercialSubject search;
	
	private Customer customer;
	
	CommercialSubjectModelImpl(final CommercialSubject search) {
		this.search = search;
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

}

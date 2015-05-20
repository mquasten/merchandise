package de.mq.merchandise.subject.support;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.util.support.ObservableImpl;


class SubjectModelImpl extends ObservableImpl<SubjectModel.EventType> implements SubjectModel {
	private Subject searchCriteria; 
	private Customer customer;
	
	private Optional<Subject> subject = Optional.empty();
	
	SubjectModelImpl() {
		searchCriteria=BeanUtils.instantiateClass(SubjectImpl.class, Subject.class);
		customer=BeanUtils.instantiateClass(CustomerImpl.class, Customer.class);
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
		setSerachCriteria(searchCriteria);
	}
	@Override
	public final void setSerachCriteria(final Subject searchCriteria) {
		Assert.notNull(searchCriteria, "Searchcriteria is mandatory");
		ReflectionUtils.doWithFields(searchCriteria.getClass(), field -> { field.setAccessible(true); ReflectionUtils.setField(field, searchCriteria, customer);}, field -> field.getType().equals(Customer.class));
		this.searchCriteria=searchCriteria;
		notifyObservers(EventType.SearchCriteriaChanged);
	}


	@Override
	public final void setSelected(final Subject subject) {
		this.subject=Optional.ofNullable(subject);
		notifyObservers(EventType.SubjectChanged);
	}
	
	@Override
	public final Optional<Subject> getSelected() {
		return subject;
	}
}

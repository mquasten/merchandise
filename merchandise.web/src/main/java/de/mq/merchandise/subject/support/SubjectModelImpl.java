package de.mq.merchandise.subject.support;



import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.util.Event;
import de.mq.merchandise.util.EventBuilder;
import de.mq.merchandise.util.support.ObservableImpl;


class SubjectModelImpl extends ObservableImpl<SubjectModel.EventType> implements SubjectModel {
	private Subject searchCriteria; 
	private Customer customer;
	
	private Subject subject;

	
	@Autowired
	ApplicationEventPublisher applicationEventPublisher;
	
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
	public void setSubjectId(final Long subjectId) {
		if( subjectId==null){
			subject=null;
			notifyObservers(EventType.SubjectChanged);
			return;
		}
		
		// https://spring.io/blog/2015/02/11/better-application-events-in-spring-framework-4-2
		final Optional<Subject> result = publishEvent(EventBuilder.of(EventType.SubjectChanged, Subject.class).withParameter(subjectId).build());
		Assert.isTrue(result.isPresent(), "Subject should be returned");
		subject = result.get();
		
		notifyObservers(EventType.SubjectChanged);
	}


	

	@Override
	public final Optional<Subject> getSubject() {
		return Optional.ofNullable(subject);
	}
	
	
	private<R,T> Optional<R> publishEvent(final Event<T, R> event) {
		applicationEventPublisher.publishEvent( event );
		return event.result();
	}
	
}

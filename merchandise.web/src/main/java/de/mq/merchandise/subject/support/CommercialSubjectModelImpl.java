package de.mq.merchandise.subject.support;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import de.mq.merchandise.customer.Customer;
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

}

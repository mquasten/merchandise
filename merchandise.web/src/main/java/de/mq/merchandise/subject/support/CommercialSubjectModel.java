package de.mq.merchandise.subject.support;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Optional;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.util.Observable;

public interface CommercialSubjectModel extends Observable<CommercialSubjectModel.EventType> {
	
	enum EventType {
		CountPaging,
		ListPaging, 
		SearchCriteriaChanged, 
		CommericalSubjectChanged, 
		CommercialSubjectSaved, 
		CommercialSubjectDeleted, ListSubjects;
		
	}

	void setSearch(CommercialSubject search);



	void setCustomer(final Customer customer);

	CommercialSubject getSearch();



	void setCommercialSubjectId(Long value);



	Optional<CommercialSubject> getCommercialSubject();



	void save(final CommercialSubject commercialSubject);



	void delete(final CommercialSubject commercialSubject);



	Collection<Entry<Long, String>> getSubjects();

}

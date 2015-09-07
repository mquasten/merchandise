package de.mq.merchandise.subject.support;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.util.Observable;

public interface CommercialSubjectModel extends Observable<CommercialSubjectModel.EventType> {
	
	enum EventType {
		CountPaging,
		ListPaging, SearchCriteriaChanged,
		
	}

	void setSearch(CommercialSubject search);



	void setCustomer(final Customer customer);

	CommercialSubject getSearch();

}

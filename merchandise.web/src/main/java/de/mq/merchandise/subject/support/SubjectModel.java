package de.mq.merchandise.subject.support;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.util.Observable;

public interface SubjectModel extends Observable<SubjectModel.EventType> {
	
	public enum EventType {
		SearchCriteriaChanged,
		SubjectChanged;
	}

	Subject getSearchCriteria();

	void setCustomer(final Customer customer);

	void setSerachCriteria(final Subject searchCriteria);

	void setSubjectId(final Long subjectId);
	




}
package de.mq.merchandise.subject.support;

import java.util.Optional;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.util.Observable;

public interface SubjectModel extends Observable<SubjectModel.EventType> {
	
	enum EventType {
		CountPaging,
		ListPaging,
		SearchCriteriaChanged,
		SubjectChanged,
		SubjectSaved;
	}

	Subject getSearchCriteria();

	void setCustomer(final Customer customer);

	void setSerachCriteria(final Subject searchCriteria);

	void setSubjectId(final Long subjectId);

	Optional<Subject> getSubject();
	
	void save(final Subject subject);
	




}
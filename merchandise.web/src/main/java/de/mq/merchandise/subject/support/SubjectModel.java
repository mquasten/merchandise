package de.mq.merchandise.subject.support;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.util.Observable;

public interface SubjectModel extends Observable<SubjectModel.EventType> {
	
	enum EventType {
		CountPaging,
		ListPaging,
		SearchCriteriaChanged,
		SubjectChanged,
		SubjectSaved, 
		SubjectDeleted,
		ConditionDeleted,
		ConditionChanged,
		ConditionSaved;
	}

	Subject getSearchCriteria();

	void setCustomer(final Customer customer);

	void setSerachCriteria(final Subject searchCriteria);

	void setSubjectId(final Long subjectId);

	Optional<Subject> getSubject();
	
	void save(final Subject subject);

	void delete(Subject subject);

	void setConditionId(Long value);

	Optional<Condition> getCondition();

	/*Collection<ConditionDataType> getDataTypes();

	Collection<String> getConditionTypes(); */

	void save(final Condition subject);

	Map<ConditionCols, Collection<?>> getConditionValues();

	void delete(Condition subject);

	boolean hasCondition(Condition condition);

	
	




}
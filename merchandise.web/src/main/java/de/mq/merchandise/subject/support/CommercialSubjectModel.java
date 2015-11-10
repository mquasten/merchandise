package de.mq.merchandise.subject.support;

import java.util.Collection;
import java.util.Optional;

import javax.validation.constraints.Pattern;


import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.util.Observable;

public interface CommercialSubjectModel extends Observable<CommercialSubjectModel.EventType> {
	
	enum EventType {
		CountPaging,
		ListPaging, 
		SearchCriteriaChanged, 
		CommericalSubjectChanged, 
		CommericalSubjectItemChanged,
		CommercialSubjectSaved, 
		CommercialSubjectDeleted, ListSubjects, ItemSaved, CommericalSubjectItemDeleted, ConditionChanged, AddInputValue;
		
	}

	void setSearch(CommercialSubject search);



	void setCustomer(final Customer customer);

	CommercialSubject getSearch();



	void setCommercialSubjectId(Long value);



	



	void save(final CommercialSubject commercialSubject);



	void delete(final CommercialSubject commercialSubject);



	Collection<Subject> getSubjects();



	Optional<CommercialSubjectItem> getCommercialSubjectItem();



	Optional<CommercialSubject> getCommercialSubject();



	void save(final CommercialSubjectItem commercialSubjectItem);



	void setCommercialSubjectItemId(final Long itemId);



	void delete(final CommercialSubjectItem commercialSubjectItem);



	Collection<Condition> getConditions();



	void setCondition(final Long  conditionId);



	boolean hasCondition();


	
	@Pattern(regexp= "\\S{1,50}" , message = "jsr303_input_value")
	String getInputValue();



	void addInputValue(final Long conditionId);



	<T> Collection<T> inputValues(final Long conditionId);



}

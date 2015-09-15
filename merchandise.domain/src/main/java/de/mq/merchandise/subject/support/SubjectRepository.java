package de.mq.merchandise.subject.support;

import java.util.Collection;
import java.util.Map.Entry;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.Subject;

interface SubjectRepository {

	static final String SUBJECTS_FOR_CUSTOMER_QUERY = "subjectsForCustomer";
	static final String SUBJECTS_MAP_FOR_CUSTOMER_QUERY = "subjectsForCustomerAsMap";
	static final String ID_PARAM_NAME = "id"; 
	static final String NAME_PARAM_NAME = "name"; 
	static final String DESC_PARAM_NAME = "desc"; 
	
	
	void save(final Subject subject);

	Collection<Subject> subjectsForCustomer(final Subject subject, final ResultNavigation paging);
	Number  subjectsForCustomer(final Subject subject);

	void remove(Subject subject);
	
	Subject subject(final Long id);
	
	Collection<Entry<Long,String>> subjectMapForCustomer(final Customer customer);
	
}
package de.mq.merchandise.subject.support;

import java.util.Collection;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.Subject;

interface SubjectRepository {

	static final String SUBJECTS_FOR_CUSTOMER_QUERY = "subjectsForCustomer";
	static final String ID_PARAM_NAME = "id"; 
	
	
	void save(final Subject subject);

	Collection<Subject> subjectsForCustomer(final Customer customer, final ResultNavigation paging);

	void remove(Subject subject);
	
}
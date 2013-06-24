package de.mq.merchandise.opportunity.support;

import java.util.Collection;

import de.mq.merchandise.BasicRepository;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.util.Paging;


public interface CommercialSubjectRepository extends BasicRepository<CommercialSubject, Long> {
	
	static final String SUBJECT_FOR_NAME_PATTERN = "subjectForNamePattern";
	
	Collection<CommercialSubject> forNamePattern(final Customer customer, final String namePattern, final Paging paging); 
	
	
}

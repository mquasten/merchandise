package de.mq.merchandise.subject.support;

import java.util.Collection;
import java.util.Map;

import de.mq.merchandise.ResultNavigation;

interface CommercialSubjectRepository {
	
	static final String COMMERCIAL_SUBJECT_BY_CRITERIA = "commercialSubjectByCriteria";
	
	static final String CUSTOMER_ID_PARAM = "customerId";
	static final String CUSTOMER_NAME_PARAM = "customerName";
	static final String SUBJECT_ID_PARAM = "subjectId";
	static final String SUBJECT_DESCRIPTION_PARAM = "subjectDescription";
	static final String SUBJECT_NAME_PARAM = "subjectName";
	static final String ITEM_NAME_PARAM = "itemName";
	static final String NAME_PARAM= "name";
	

	Collection<CommercialSubject> commercialSubjectsForCustomer(final Map<String,Object> criteria, final ResultNavigation resultNavigation);

}
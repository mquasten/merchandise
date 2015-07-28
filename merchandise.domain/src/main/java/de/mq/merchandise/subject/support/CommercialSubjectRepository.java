package de.mq.merchandise.subject.support;

import java.util.Collection;
import java.util.Map;

interface CommercialSubjectRepository {
	
	static final String COMMERCIAL_SUBJECT_BY_CRITERIA = "commercialSubjectByCriteria";

	Collection<CommercialSubject> forCriteria(final Map<String,Object> criteria);

}
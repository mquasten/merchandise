package de.mq.merchandise.opportunity.support;

import java.util.Collection;

import de.mq.merchandise.util.Paging;

public interface CommercialSubjectRepository {
	
	static final String SUBJECT_FOR_NAME_PATTERN = "subjectForNamePattern";
	
	Collection<CommercialSubject> forNamePattern(final String namePattern, final Paging paging);
	
	CommercialSubject save(final CommercialSubject commercialSubject); 

}

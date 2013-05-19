package de.mq.merchandise.opportunity;

import java.util.Collection;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.opportunity.support.CommercialSubject;
import de.mq.merchandise.util.Paging;

@Transactional(readOnly=true)
public interface CommercialSubjectService {

	Collection<CommercialSubject> subjects(final String patternForName, Paging paging);
	
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	CommercialSubject createOrUpdate(final CommercialSubject commercialSubject);
	
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void delete(final CommercialSubject commercialSubject);

}
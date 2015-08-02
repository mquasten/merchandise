package de.mq.merchandise.subject.support;

import java.util.Collection;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.ResultNavigation;

@Transactional(readOnly=true)
public interface CommercialSubjectService {
	@Transactional(readOnly=true)
	Collection<CommercialSubject> commercialSubjects(CommercialSubject commercialSubject, ResultNavigation resultNavigation);

	@Transactional(readOnly=true)
	Number commercialSubjects(CommercialSubject commercialSubject);

	@Transactional(readOnly=false , propagation=Propagation.REQUIRED)
	void save(CommercialSubject commercialSubject);

	@Transactional(readOnly=true)
	CommercialSubject commercialSubject(Long id);
	
	@Transactional(readOnly=false , propagation=Propagation.REQUIRED)
	void remove(CommercialSubject commercialSubject);

}
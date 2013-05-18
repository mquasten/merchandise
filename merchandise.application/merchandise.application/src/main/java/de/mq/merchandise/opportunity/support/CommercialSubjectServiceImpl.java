package de.mq.merchandise.opportunity.support;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.opportunity.CommercialSubjectService;
import de.mq.merchandise.opportunity.support.CommercialSubject;
import de.mq.merchandise.opportunity.support.CommercialSubjectRepository;
import de.mq.merchandise.util.Paging;

@Service
@Transactional(propagation=Propagation.REQUIRED , readOnly=true)
public class CommercialSubjectServiceImpl implements CommercialSubjectService {
	
	
	private final CommercialSubjectRepository commercialSubjectRepository;
	
	@Autowired
	public CommercialSubjectServiceImpl(final CommercialSubjectRepository commercialSubjectRepository){
		this.commercialSubjectRepository=commercialSubjectRepository;
	}
	
	
	@Override
	public final Collection<CommercialSubject> subjects(final String patternForName, final Paging paging) {
		return commercialSubjectRepository.forNamePattern(patternForName, paging);
		
	}


	@Override
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public final CommercialSubject createOrUpdate(final CommercialSubject commercialSubject) {
		return commercialSubjectRepository.save(commercialSubject);
	}
	
	

}

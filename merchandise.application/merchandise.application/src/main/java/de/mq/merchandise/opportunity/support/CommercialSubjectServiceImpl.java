package de.mq.merchandise.opportunity.support;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.opportunity.CommercialSubjectService;
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
	public final Collection<CommercialSubject> subjects(final Customer customer, final String patternForName, final Paging paging) {
		return commercialSubjectRepository.forNamePattern(customer, patternForName, paging);
		
	}


	@Override
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public final CommercialSubject createOrUpdate(final CommercialSubject commercialSubject) {
		return commercialSubjectRepository.save(commercialSubject);
	}


	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(CommercialSubject commercialSubject) {
		commercialSubjectRepository.delete(commercialSubject.id());
		
	}


	@Override
	public CommercialSubject subject(final Long id) {
		final CommercialSubject result = commercialSubjectRepository.forId(id);
		if ( result == null){
			throw new InvalidDataAccessApiUsageException("CommercialSubject not found");
		}
		return result;
	}
	
	

}

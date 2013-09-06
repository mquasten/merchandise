package de.mq.merchandise.opportunity.support;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.opportunity.CommercialSubjectService;
import de.mq.merchandise.util.BasicServiceImpl;
import de.mq.merchandise.util.Paging;

@Service
@Transactional(propagation=Propagation.REQUIRED , readOnly=true)
public class CommercialSubjectServiceImpl extends BasicServiceImpl<CommercialSubject> implements CommercialSubjectService {
	
	
	@Autowired
	public CommercialSubjectServiceImpl(final CommercialSubjectRepository commercialSubjectRepository){
		super(commercialSubjectRepository);
	}
	
	
	@Override
	public final Collection<CommercialSubject> subjects(final Customer customer, final String patternForName, final Paging paging) {
		Collection<CommercialSubject>  results = ((CommercialSubjectRepository) repository).forNamePattern(customer, patternForName, paging);
		
		 for(final DocumentsAware document : results){
			 System.out.println("****");
			 document.documents().size();
		 }
		 return results;
		
	}


	
	
	

}

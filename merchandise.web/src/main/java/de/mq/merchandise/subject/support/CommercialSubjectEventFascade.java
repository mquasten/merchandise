package de.mq.merchandise.subject.support;

import java.util.Collection;
import java.util.Map.Entry;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.support.CommercialSubjectModel.EventType;




interface   CommercialSubjectEventFascade {
	
	
	
	@CommercialSubjectEventQualifier(EventType.CountPaging)
	abstract Number countSubjects();
	


	@CommercialSubjectEventQualifier(EventType.ListPaging)
	abstract Collection<CommercialSubject> subjects(final ResultNavigation paging);

	
	@CommercialSubjectEventQualifier(EventType.CommericalSubjectChanged)
	abstract CommercialSubject commercialSubjectChanged(Long commercialSubjectId);


	@CommercialSubjectEventQualifier(EventType.CommercialSubjectSaved)
	abstract void save(final Long orElse, final CommercialSubject commercialSubject);


	@CommercialSubjectEventQualifier(EventType.CommercialSubjectDeleted)
	abstract void delete(final CommercialSubject commercialSubject);


	@CommercialSubjectEventQualifier(EventType.ListSubjects)
	abstract  Collection<Entry<Long,String>>  subjects(final Customer customer);

	
	
}

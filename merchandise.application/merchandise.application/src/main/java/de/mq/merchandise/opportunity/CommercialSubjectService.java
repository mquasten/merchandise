package de.mq.merchandise.opportunity;

import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.opportunity.support.CommercialSubject;
import de.mq.merchandise.util.BasicService;
import de.mq.merchandise.util.Paging;

@Transactional(readOnly=true)
public interface CommercialSubjectService extends BasicService<CommercialSubject> {

	Collection<CommercialSubject> subjects(final Customer customer, final String patternForName, Paging paging);

}
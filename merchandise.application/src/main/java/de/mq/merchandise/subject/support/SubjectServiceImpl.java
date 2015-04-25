package de.mq.merchandise.subject.support;

import java.util.Collection;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.Subject;

@Service
class SubjectServiceImpl implements SubjectService {
	
	
	private SubjectRepository subjectRepository;
	
	@Autowired
	SubjectServiceImpl(final SubjectRepository subjectRepository) {
		this.subjectRepository = subjectRepository;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.SubjectService#subjects(de.mq.merchandise.customer.Customer)
	 */
	@Override
	@Transactional(readOnly=true)
	public final Collection<Subject> subjects(final Customer customer, final ResultNavigation paging) {
		return subjectRepository.subjectsForCustomer(customer, paging);
	}

	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.SubjectService#save(de.mq.merchandise.subject.Subject)
	 */
	@Override
	@Transactional
	public final void save(final Subject subject) {
		subjectRepository.save(subject);
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.SubjectService#remove(de.mq.merchandise.subject.Subject)
	 */
	@Override
	@Transactional
	public final void remove(final Subject subject) {
		subjectRepository.remove(subject);
	}
}

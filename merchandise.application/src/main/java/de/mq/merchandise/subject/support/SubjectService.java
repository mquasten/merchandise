package de.mq.merchandise.subject.support;

import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.Subject;

public interface SubjectService {

	@Transactional
	Subject subject(Long id); 
	@Transactional
	Collection<Subject> subjects(Subject subject, final ResultNavigation paging);
	@Transactional
	Number subjects(Subject subject); 
	@Transactional
	void save(Subject subject);
	@Transactional
	void remove(Subject subject);
	@Transactional
	Collection<Subject> subjects(final Customer customer);

	

}
package de.mq.merchandise.subject.support;

import java.util.Collection;

import de.mq.merchandise.Paging;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.Subject;

interface SubjectService {

	public abstract Collection<Subject> subjects(Customer customer, final Paging paging);

	public abstract void save(Subject subject);

	public abstract void remove(Subject subject);

}
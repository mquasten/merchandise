package de.mq.merchandise.subject.support;

import org.springframework.stereotype.Component;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.support.SubjectMapper.SubjectMapperType;
import de.mq.merchandise.support.Mapper;
import de.mq.merchandise.support.ReflectionBasedFieldMapperImpl;

@Component
@SubjectMapper(SubjectMapperType.Customer2Subject)
class CustomerIntoSubjectMapperImpl<T> extends ReflectionBasedFieldMapperImpl implements Mapper<Customer,T> {

	@Override
	public T mapInto(final Customer source, final T target) {
		assign("customer", target, source);
		return target;
	}

}

package de.mq.merchandise.util;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface BasicService<T> {

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	T createOrUpdate(final T commercialSubject);

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	void delete(final T commercialSubject);

	T read(final Long id);

}
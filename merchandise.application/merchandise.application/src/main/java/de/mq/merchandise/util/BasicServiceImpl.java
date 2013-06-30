package de.mq.merchandise.util;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.BasicRepository;

@Transactional(readOnly = true)
public class BasicServiceImpl <T>  implements BasicService<T>{

	
	protected BasicRepository<T, Long> repository;
	

	
	public BasicServiceImpl(final BasicRepository<T, Long> repository) {
		this.repository = repository;
	}


	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public final T createOrUpdate(final T commercialSubject) {
		return repository.save(commercialSubject);
	}

	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public final void delete(final T commercialSubject) {
		repository.delete(((BasicEntity)commercialSubject).id());
	}

	
	public final T read(final Long id) {
		final T result = repository.forId(id);
		if ( result == null){
			throw new InvalidDataAccessApiUsageException("CommercialSubject not found");
		}
		return result;
	}

	
}
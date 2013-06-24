package de.mq.merchandise.opportunity.support;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;

import de.mq.merchandise.BasicRepository;
import de.mq.merchandise.util.PagingUtil;

public abstract class AbstractRepositoryImpl<T,V> implements BasicRepository<T,V>  {

	@PersistenceContext
	protected EntityManager entityManager;
	@Autowired
	protected PagingUtil entityManagerUtil;

	

	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.BasicREpository#save(de.mq.merchandise.opportunity.support.CommercialSubject)
	 */
	
	@Override
	public final T save(final T commercialSubject) {
		return entityManager.merge(commercialSubject);
	}

	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.BasicREpository#delete(de.mq.merchandise.opportunity.support.CommercialSubject)
	 */
	
	@Override
	public final void delete(final V id ) {
		final T existing = (T) entityManager.find(clazz(),  id);
		if( existing==null){
			return;
		}
		entityManager.remove(existing);
		
	}

	
	protected  abstract Class<? extends T> clazz();
	

	
	public final T forId(final V id) {
		return entityManager.find(clazz(), id);
		
	}
	
	

}
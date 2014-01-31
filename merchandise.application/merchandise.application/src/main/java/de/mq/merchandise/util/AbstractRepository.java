package de.mq.merchandise.util;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.mq.merchandise.BasicRepository;
import de.mq.merchandise.opportunity.support.Opportunity;

public abstract class AbstractRepository<T,V> implements BasicRepository<T,V>  {

	@PersistenceContext
	protected EntityManager entityManager;
	

	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.BasicREpository#save(de.mq.merchandise.opportunity.support.CommercialSubject)
	 */
	
	@Override
	public  T save(final T commercialSubject) {
		T result = entityManager.merge(commercialSubject);
		
		if (commercialSubject instanceof Opportunity) {
			 System.out.println("????" + ((Opportunity)commercialSubject).commercialRelations().size());
			
		}
		
		return result;
	}

	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.BasicREpository#delete(de.mq.merchandise.opportunity.support.CommercialSubject)
	 */
	
	@Override
	public   void delete(final V id ) {
		final T existing = (T) entityManager.find(entityImplementationClass(),  id);
		if( existing==null){
			return;
		}
		entityManager.remove(existing);
		
	}

	
	protected abstract Class<? extends T> entityImplementationClass();
	

	
	public  T forId(final V id) {
		return entityManager.find(entityImplementationClass(), id);
		
	}
	
	

}
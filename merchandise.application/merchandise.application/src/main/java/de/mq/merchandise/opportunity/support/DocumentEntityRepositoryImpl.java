package de.mq.merchandise.opportunity.support;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Profile;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;

@Repository
@Profile("db")
class DocumentEntityRepositoryImpl implements DocumentEntityRepository {
	
	
	@PersistenceContext
	private final EntityManager entityManger; 
	
	DocumentEntityRepositoryImpl(){
         this.entityManger=null;
	}
	
	DocumentEntityRepositoryImpl(final EntityManager entityManger) {
		this.entityManger=entityManger;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.DocumentEntityRepository#forId(java.lang.Long, java.lang.Class)
	 */
	@Override
	public final DocumentsAware forId(final Long id, Class<? extends DocumentsAware> clazz) {
		final DocumentsAware result =  entityManger.find(clazz, id);
	    entityExistsGuard(id, result);
		return result;
	}

	private void entityExistsGuard(final Long id, final DocumentsAware result) {
		if ( result == null){
			throw new InvalidDataAccessApiUsageException("CommercialSubject not found: " + id);
		}
	}
	
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.DocumentEntityRepository#save(de.mq.merchandise.opportunity.support.DocumentsAware)
	 */
	@Override
	public final void save(final DocumentsAware result) {
		entityManger.merge(result);
	}

}

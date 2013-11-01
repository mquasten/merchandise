package de.mq.merchandise;




public interface BasicRepository<T,V> {
	


		/**
		 * Merge an entity to a persistenceContext
		 * @param entity the entity that should bem merged
		 * @return the merged Entity with its ids
		 */
		T save(T entity);

		/**
		 * Delete an entity from a PersitenceContext
		 * @param id the entity that should be deleted
		 */
		void delete(final V Id);
		
		/**
		 * Find the entity by its id
		 * @param id the id of the entity
		 * @return the entity or null if not found
		 */
		T forId(final V id);

	

		
		
		

		


}

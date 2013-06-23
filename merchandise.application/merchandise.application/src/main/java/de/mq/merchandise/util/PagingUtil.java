package de.mq.merchandise.util;

import java.util.Collection;

import javax.persistence.EntityManager;

/**
 * Query data on a JPA entitymanager, dependend of a named query,
 * paging information and parameters
 * @author mquasten
 *
 */
public interface PagingUtil {

 /**
  * Query data on a JPA entitymanager 	
  * Depended from the number of results the paging paging.assignRowCounter() is set, so
  * that the pagiging can be coreccted if nesseary.
  * @param entityManager the entitymanger from JPA
  * @param clazz the type of the result collection 
  * @param paging the paging information 
  * @param namedQuery a named Query, defined with annotations on the entityclasses 
  * @param params Parameters for the query
  * @return the result as a collection
  */
  <T>  Collection<T> countAndQuery(final EntityManager entityManager, final Class<T> clazz, final Paging paging, final String namedQuery, final Parameter<?> ... params);

 
}
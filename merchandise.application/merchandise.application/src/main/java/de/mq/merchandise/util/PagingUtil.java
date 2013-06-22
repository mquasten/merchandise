package de.mq.merchandise.util;

import java.util.Collection;

import javax.persistence.EntityManager;

public interface PagingUtil {

	
  <T>  Collection<T> countAndQuery(final EntityManager entityManager, final Class<T> clazz, final Paging paging, final String namedQuery, final Parameter<?> ... params);

}
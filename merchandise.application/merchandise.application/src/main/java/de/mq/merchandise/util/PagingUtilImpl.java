package de.mq.merchandise.util;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Component;

@Component
public class PagingUtilImpl implements PagingUtil {

	private final long count(final EntityManager entityManager, final String namedQuery, final Parameter<?>... params) {
		final TypedQuery<Number> typedCountQuery = entityManager.createQuery(QueryUtils.createCountQueryFor(queryString(entityManager, namedQuery)), Number.class);
		bind(typedCountQuery, params);
		return typedCountQuery.getSingleResult().longValue();
	}

	private String queryString(final EntityManager entityManager, final String queryName) {
		return entityManager.createNamedQuery(queryName).unwrap(org.hibernate.Query.class).getQueryString().replaceFirst("[Oo][Rr][Dd][Ee][Rr].*[bB][Yy].*$", "");
	}

	private final <T> Collection<T> query(final EntityManager entityManager, final Class<T> clazz, final Paging paging, final String namedQuery, final Parameter<?>... params) {
		final TypedQuery<T> typedResultQuery = entityManager.createQuery(queryString(entityManager, namedQuery) + " order by " + paging.sortHint(), clazz);

		typedResultQuery.setFirstResult(paging.firstRow());
		typedResultQuery.setMaxResults(paging.pageSize());
		bind(typedResultQuery, params);

		return typedResultQuery.getResultList();
	}

	private <T> void bind(final TypedQuery<T> typedResultQuery, final Parameter<?>... params) {
		for (final Parameter<?> parameter : params) {
			typedResultQuery.setParameter(parameter.name(), parameter.value());
		}
	}

	@Override
	public final <T> Collection<T> countAndQuery(final EntityManager entityManager, final Class<T> clazz, final Paging paging, final String namedQuery, final Parameter<?>... params) {
		paging.assignRowCounter(count(entityManager, namedQuery, params));
		return query(entityManager, clazz, paging, namedQuery, params);
	}

}

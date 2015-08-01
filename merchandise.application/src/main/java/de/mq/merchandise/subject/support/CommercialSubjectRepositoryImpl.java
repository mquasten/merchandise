package de.mq.merchandise.subject.support;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Repository;

import de.mq.merchandise.ResultNavigation;


@Repository
 class CommercialSubjectRepositoryImpl implements CommercialSubjectRepository {
	
	private final Collection<String> params = Arrays.asList(CUSTOMER_ID_PARAM, SUBJECT_DESCRIPTION_PARAM, SUBJECT_NAME_PARAM, ITEM_NAME_PARAM ,NAME_PARAM);
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public final Collection<CommercialSubject> commercialSubjectsForCustomer(final Map<String,Object> criteria, final ResultNavigation resultNavigation) {
		final TypedQuery<CommercialSubject> query  = entityManager.createQuery(typedQuery(resultNavigation.orders()), CommercialSubject.class);
		
		params.forEach(param -> query.setParameter(param, criteria.get(param)));
		
		query.setFirstResult(resultNavigation.firstRow() .intValue());
		query.setMaxResults(resultNavigation.pageSize().intValue());
		return Collections.unmodifiableList(query.getResultList());
		
	}
	
	
	private String typedQuery(Collection<Order> orders) {

		final StringBuilder builder = new StringBuilder();

		orders.forEach(o -> {
			if (builder.length() > 0) {
				builder.append(",");
			}
			builder.append(String.format("%s %s", o.getProperty(), o.getDirection().name()));

		});

		String jpaString = entityManager.createNamedQuery(COMMERCIAL_SUBJECT_BY_CRITERIA, CommercialSubject.class).unwrap(org.hibernate.Query.class).getQueryString();

		if (!orders.isEmpty()) {
			jpaString += " order by " + builder.toString();
		}

		return jpaString;
	}


}

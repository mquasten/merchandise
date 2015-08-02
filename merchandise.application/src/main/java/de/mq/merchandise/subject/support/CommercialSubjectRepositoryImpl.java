package de.mq.merchandise.subject.support;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import de.mq.merchandise.ResultNavigation;

@Repository
class CommercialSubjectRepositoryImpl implements CommercialSubjectRepository {

	private final Collection<String> params = Arrays.asList(CUSTOMER_ID_PARAM, SUBJECT_DESCRIPTION_PARAM, SUBJECT_NAME_PARAM, ITEM_NAME_PARAM, NAME_PARAM);

	@PersistenceContext
	private EntityManager entityManager;

	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.CommercialSubjectRepository#commercialSubjectsForCustomer(java.util.Map, de.mq.merchandise.ResultNavigation)
	 */
	@Override
	public final Collection<CommercialSubject> commercialSubjectsForCustomer(final Map<String, Object> criteria, final ResultNavigation resultNavigation) {
		Assert.notNull(criteria, "CriteriaMap is mandatory");
		Assert.notNull(resultNavigation, "ResultNavigation is mandatory");
		final TypedQuery<CommercialSubject> query = entityManager.createQuery(typedQuery(resultNavigation.orders()), CommercialSubject.class);

		params.forEach(param -> query.setParameter(param, criteria.get(param)));

		query.setFirstResult(resultNavigation.firstRow().intValue());
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

	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.CommercialSubjectRepository#countCommercialSubjectsForCustomer(java.util.Map)
	 */
	@Override
	public final Number countCommercialSubjectsForCustomer(Map<String, Object> criteria) {
		Assert.notNull(criteria, "CriteriaMap is mandatory");
		final TypedQuery<Number> countQuery = entityManager.createQuery(entityManager.createNamedQuery(COMMERCIAL_SUBJECT_BY_CRITERIA, CommercialSubject.class).unwrap(org.hibernate.Query.class).getQueryString().replaceFirst("distinct[ ]+cs", "count(distinct cs)"), Number.class);
		params.forEach(param -> countQuery.setParameter(param, criteria.get(param)));
		return countQuery.getSingleResult();
	}

	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.CommercialSubjectRepository#save(de.mq.merchandise.subject.support.CommercialSubject)
	 */
	@Override
	public final void save(final CommercialSubject commercialSubject) {
		Assert.notNull(commercialSubject, "CommercialSubject is mandatory");
		final Optional<Long> id = entityManager.merge(commercialSubject).id();
		Assert.isTrue(id.isPresent(), "Id must be set, after save");

		ReflectionUtils.doWithFields(commercialSubject.getClass(), field -> {
			field.setAccessible(true);
			ReflectionUtils.setField(field, commercialSubject, id);
		}, field -> field.isAnnotationPresent(Id.class));

	}
	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.CommercialSubjectRepository#commercialSubject(java.lang.Long)
	 */
	@Override
	public final CommercialSubject commercialSubject(final Long id) {
		Assert.notNull(id, "Id is mandatory");
		return entityManager.find(CommercialSubjectImpl.class, id);
	}
	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.CommercialSubjectRepository#remove(de.mq.merchandise.subject.support.CommercialSubject)
	 */
	@Override
	public final void remove(CommercialSubject commercialSubject) {
		Assert.notNull(commercialSubject, "CommercialSubject is mandatory");
		if( ! commercialSubject.id().isPresent() ) {
			return;
		}
		final CommercialSubject  intoTheDust =  commercialSubject(commercialSubject.id().get());
		if( intoTheDust == null ){
			return;
		}
		entityManager.remove(intoTheDust);
	}

}

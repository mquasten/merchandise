package de.mq.merchandise.subject.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.subject.Subject;

@Repository
class SubjectRepositoryImpl implements SubjectRepository {

	@PersistenceContext
	private EntityManager entityManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.mq.merchandise.subject.support.SubjectRepository#save(de.mq.merchandise
	 * .subject.Subject)
	 */
	@Override
	@Transactional
	public final void save(final Subject subject) {
		final Optional<Long> id = entityManager.merge(subject).id();
		Assert.isTrue(id.isPresent(), "Id must be set, after save");
		ReflectionUtils.doWithFields(subject.getClass(), field -> {
			field.setAccessible(true);
			ReflectionUtils.setField(field, subject, id.get());
		}, field -> field.isAnnotationPresent(Id.class));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.mq.merchandise.subject.support.SubjectRepository#subjectsForCustomer
	 * (de.mq.merchandise.customer.Customer)
	 */
	@Override
	public Collection<Subject> subjectsForCustomer(final Subject subject, final ResultNavigation paging) {
		Assert.notNull(subject, "Subject is mandatory");
		Assert.notNull(subject.customer(), "Customer is mandatory");
		if (!subject.customer().id().isPresent()) {
			return Collections.unmodifiableCollection(new ArrayList<>());
		}
		final TypedQuery<Subject> query = buildSubjectQuery(changeNamedQueryWithOrder(paging.orders()), subject);
		query.setFirstResult(paging.firstRow().intValue());
		query.setMaxResults(paging.pageSize().intValue());

		return Collections.unmodifiableCollection(query.getResultList());

	}

	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.SubjectRepository#subjectsForCustomer(de.mq.merchandise.subject.Subject)
	 */
	public final Number subjectsForCustomer(final Subject subject) {
		Assert.notNull(subject, "Subject is mandatory");
		Assert.notNull(subject.customer(), "Customer is mandatory");
		if (!subject.customer().id().isPresent()) {
			return 0;
		}
		final TypedQuery<Number> query = buildSubjectQuery(changeNamedQueryWithCount(), subject);
		return query.getSingleResult();

	}

	private <T> TypedQuery<T> buildSubjectQuery(final TypedQuery<T> query, final Subject subject) {

		query.setParameter(SubjectRepository.ID_PARAM_NAME, subject.customer().id().get());
		String name = "";
		if (StringUtils.hasText(subject.name())) {
			name = subject.name();
		}
		name += "%";
		query.setParameter(SubjectRepository.NAME_PARAM_NAME, name);
		String desc = "";
		if (StringUtils.hasText(subject.description())) {
			desc = subject.description();
		}
		desc += "%";

		query.setParameter(SubjectRepository.DESC_PARAM_NAME, desc);

		return query;
	}

	private TypedQuery<Subject> changeNamedQueryWithOrder(Collection<Order> orders) {

		final StringBuilder builder = new StringBuilder();

		orders.forEach(o -> {
			if (builder.length() > 0) {
				builder.append(",");
			}
			builder.append(String.format("%s %s", o.getProperty(), o.getDirection().name()));

		});

		String jpaString = entityManager.createNamedQuery(SubjectRepository.SUBJECTS_FOR_CUSTOMER_QUERY, Subject.class).unwrap(org.hibernate.Query.class).getQueryString();

		if (!orders.isEmpty()) {
			jpaString += " order by " + builder.toString();
		}

		final TypedQuery<Subject> query = entityManager.createQuery(jpaString, Subject.class);
		return query;
	}

	private TypedQuery<Number> changeNamedQueryWithCount() {
		final String jpaString = entityManager.createNamedQuery(SubjectRepository.SUBJECTS_FOR_CUSTOMER_QUERY, Subject.class).unwrap(org.hibernate.Query.class).getQueryString();

		final TypedQuery<Number> query = entityManager.createQuery(jpaString.replaceFirst("[ ]s[ ]", " count(s) "), Number.class);
		return query;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.mq.merchandise.subject.support.SubjectRepository#remove(de.mq.merchandise
	 * .subject.Subject)
	 */
	@Override
	public final void remove(final Subject subject) {
		Assert.notNull(subject, "Subject is mandatory");
		if (!subject.id().isPresent()) {
			return;
		}

		final Subject toBeDeleted = entityManager.find(subject.getClass(), subject.id().get());
		if (toBeDeleted == null) {
			return;
		}
		entityManager.remove(toBeDeleted);
	}
	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.SubjectRepository#subject(java.lang.Long)
	 */
	@Override
	public Subject subject(Long id) {
		return entityManager.find(SubjectImpl.class, id);
	}

	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.SubjectRepository#subjectMapForCustomer(de.mq.merchandise.customer.Customer)
	 */
	
	@Override
	public final Collection<Entry<Long, String>> subjectMapForCustomer(final Customer customer) {
		Assert.notNull(customer);
		Assert.isTrue(customer.id().isPresent());
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		final TypedQuery<Entry<Long,String>> query =  entityManager.createNamedQuery(SUBJECTS_MAP_FOR_CUSTOMER_QUERY,(Class<Entry<Long,String>>) (Class) Entry.class);
		query.setParameter(SubjectRepository.ID_PARAM_NAME, customer.id().get());
		return Collections.unmodifiableCollection( query.getResultList());
	}

}

package de.mq.merchandise.opportunity.support;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Comparator;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.util.ReflectionUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.CustomerMemoryReposioryMock;
import de.mq.merchandise.util.AbstractPagingMemoryRepository;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Paging;
import de.mq.merchandise.util.Parameter;
import de.mq.merchandise.util.ParameterImpl;

@Repository
@Profile("mock")
public class CommercialSubjectMemoryRepositoryMock extends AbstractPagingMemoryRepository<CommercialSubject> implements CommercialSubjectRepository {

	static final CommercialSubject[] DEFAULTS = new CommercialSubject[] { new CommercialSubjectImpl(null, "EscortService", "Nicole's special services"),
			new CommercialSubjectImpl(null, "Music-Downloads", "Flatrate für Musik") };

	private CustomerMemoryReposioryMock customerMemoryReposioryMock;

	@Autowired
	public CommercialSubjectMemoryRepositoryMock(final CustomerMemoryReposioryMock customerMemoryReposioryMock) {
		this.customerMemoryReposioryMock = customerMemoryReposioryMock;
	}

	@PostConstruct
	void init() {
		for (final CommercialSubject commercialSubject : DEFAULTS) {
			EntityUtil.setId(commercialSubject, randomId());
			final Field field = ReflectionUtils.findField(commercialSubject.getClass(), "customer");
			field.setAccessible(true);
			ReflectionUtils.setField(field, commercialSubject, customerMemoryReposioryMock.forId(CustomerMemoryReposioryMock.DEFAULT_CUSTOMER_ID));
			storedValues.put(commercialSubject.id(), commercialSubject);
		}
	}

	@Override
	public Collection<CommercialSubject> forNamePattern(final Customer customer, final String namePattern, final Paging paging) {
		return super.forPattern(paging, new ParameterImpl<Long>(PARAMETER_CUSTOMER_ID, customer.id()), new ParameterImpl<String>(PARAMETER_SUBJECT_NAME, namePattern.replaceAll("[%]", ".*")));

	}

	@Override
	protected boolean match(final CommercialSubject value, final Parameter<?>... params) {
		final Long customerId = super.param(params, PARAMETER_CUSTOMER_ID, Long.class);
		final String pattern = super.param(params, PARAMETER_SUBJECT_NAME, String.class);
		if (!(value.customer().id() == customerId)) {
			return false;
		}

		return value.name().matches(pattern);

	}

	@Override
	protected Comparator<CommercialSubject> comparator() {

		return new Comparator<CommercialSubject>() {

			@Override
			public int compare(final CommercialSubject o1, final CommercialSubject o2) {

				return o1.name().compareTo(o2.name());
			}
		};
	}

}

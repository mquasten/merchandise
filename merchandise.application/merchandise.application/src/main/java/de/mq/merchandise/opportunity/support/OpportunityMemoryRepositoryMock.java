package de.mq.merchandise.opportunity.support;

import java.util.Collection;
import java.util.Comparator;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.util.AbstractPagingMemoryRepository;
import de.mq.merchandise.util.Paging;
import de.mq.merchandise.util.Parameter;
import de.mq.merchandise.util.ParameterImpl;

@Repository
@Profile("mock")
public class OpportunityMemoryRepositoryMock extends AbstractPagingMemoryRepository<Opportunity> implements OpportunityRepository {

	@Override
	public final Collection<Opportunity> forNamePattern(final Customer customer, final String namePattern, final Paging paging) {
		return super.forPattern(paging, new ParameterImpl<String>(OpportunityRepository.PARAMETER_OPPORTUNITY_NAME, namePattern.replaceAll("[%]", ".*")),  new ParameterImpl<Long>(OpportunityRepository.PARAMETER_CUSTOMER_ID, customer.id()));
	}

	@Override
	protected final boolean match(final Opportunity opportunity, final Parameter<?>... params) {
		final String pattern = super.param(params, PARAMETER_OPPORTUNITY_NAME, String.class);
		final Long customerId = super.param(params, PARAMETER_CUSTOMER_ID, Long.class);
		if( opportunity.customer().id() != customerId){
			return false;
		}
		if( ! opportunity.name().matches(pattern)){
			return false;
		}
		
		return true;
	}

	@Override
	protected final Comparator<Opportunity> comparator() {
		
		return new Comparator<Opportunity>() {

			@Override
			public int compare(Opportunity o1, Opportunity o2) {
				return o1.name().compareTo(o2.name());
			}
		};
	}

	
	

}

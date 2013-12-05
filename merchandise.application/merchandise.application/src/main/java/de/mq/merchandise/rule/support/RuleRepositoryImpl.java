package de.mq.merchandise.rule.support;

import java.util.Collection;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.rule.Rule;
import de.mq.merchandise.util.AbstractRepository;
import de.mq.merchandise.util.Paging;
import de.mq.merchandise.util.PagingUtil;
import de.mq.merchandise.util.ParameterImpl;

@Repository
@Profile("db")
class RuleRepositoryImpl extends AbstractRepository<Rule, Long> implements RuleRepository  {
	
	
	@Autowired
	private PagingUtil pagingUtil; 
	
	RuleRepositoryImpl(final EntityManager entityManager, final PagingUtil pagingUtil) {
		this.entityManager = entityManager;
		this.pagingUtil = pagingUtil;
	}
	
	RuleRepositoryImpl() {
		
	}
	
	
	
	@Override
	public final Collection<Rule> forNamePattern(final Customer customer, final String namePattern, final Paging paging ) {
		return pagingUtil.countAndQuery(entityManager, Rule.class, paging, RULE_FOR_NAME_PATTERN, new ParameterImpl<String>(PARAMETER_RULE_NAME, namePattern ), new ParameterImpl<Long>(PARAMETER_CUSTOMER_ID, customer.id() ));
	}

	@Override
	protected final Class<? extends Rule> entityImplementationClass() {
		return RuleImpl.class;
	}

	
}

package de.mq.merchandise.rule.support;

import java.util.Collection;

import de.mq.merchandise.BasicRepository;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.rule.Rule;
import de.mq.merchandise.util.Paging;

interface RuleRepository extends BasicRepository<Rule, Long> {
	
	static final String RULE_FOR_NAME_PATTERN = "ruleForNamePattern";
	
	static final String PARAMETER_RULE_NAME = "name";
	static final String PARAMETER_CUSTOMER_ID = "customerId";

	Collection<Rule> forNamePattern(final Customer customer, final String namePattern, final Paging paging);

}
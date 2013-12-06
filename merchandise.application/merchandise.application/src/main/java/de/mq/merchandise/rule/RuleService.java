package de.mq.merchandise.rule;

import java.util.Collection;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.rule.Rule;
import de.mq.merchandise.util.BasicService;
import de.mq.merchandise.util.Paging;

public interface RuleService extends BasicService<Rule>{

	
	public abstract Collection<Rule> rules(Customer customer, String patternForName, Paging paging);

}
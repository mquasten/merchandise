package de.mq.merchandise.rule.support;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.rule.Rule;
import de.mq.merchandise.rule.RuleService;
import de.mq.merchandise.util.BasicServiceImpl;
import de.mq.merchandise.util.Paging;

@Service
@Transactional(readOnly=true )
public class RuleServiceImpl extends BasicServiceImpl<Rule> implements RuleService {

	@Autowired
	public RuleServiceImpl(final RuleRepository ruleRepository) {
		super(ruleRepository);
	}
	
	@Override
	public final Collection<Rule> rules(final Customer customer, final String patternForName, final Paging paging) {
		return ((RuleRepository) repository).forNamePattern(customer, patternForName, paging);
	}


}

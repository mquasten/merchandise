package de.mq.merchandise.rule.support;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.rule.Rule;
import de.mq.merchandise.rule.RuleService;
import de.mq.merchandise.util.Paging;

public class RuleServiceTest {
	
	private static final String NAME_PATTERN = "hotScoreForArtist";

	private final RuleRepository ruleRepository = Mockito.mock(RuleRepository.class);
	
	private final RuleService ruleService = new RuleServiceImpl(ruleRepository);
	
	private Customer customer = Mockito.mock(Customer.class);
	private Paging paging = Mockito.mock(Paging.class);
	
	private Rule rule = Mockito.mock(Rule.class);
	
	@Test
	public final void rules() {
		final Collection<Rule> rules = new ArrayList<>();
		rules.add(rule);
		Mockito.when(ruleRepository.forNamePattern(customer, NAME_PATTERN, paging)).thenReturn(rules);
		
		Collection<Rule> results = ruleService.rules(customer, NAME_PATTERN, paging);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(rule, results.iterator().next());
	}

}

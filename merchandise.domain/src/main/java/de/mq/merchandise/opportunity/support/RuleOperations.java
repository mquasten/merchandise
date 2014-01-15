package de.mq.merchandise.opportunity.support;

import java.util.List;

import de.mq.merchandise.rule.Rule;

public interface RuleOperations {

	List<RuleInstance> ruleInstances();

	void assign(final Rule rule, final int priority);
	
	void remove(final Rule rule);

	RuleInstance ruleInstance(final Rule rule);

}
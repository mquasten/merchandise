package de.mq.merchandise.opportunity.support;

import java.util.List;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.rule.Rule;

public interface RuleInstance  extends BasicEntity {

	

	void assign(final String name, final String value);

	void assign(final int priority);

	String parameter(final String name);

	Rule rule();

	int priority();
	
	boolean forRule(final Rule rule);
	
	List<String> parameterNames();

	
}
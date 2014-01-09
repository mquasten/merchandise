package de.mq.merchandise.opportunity.support;

import de.mq.merchandise.rule.Rule;

interface RuleInstance {

	void assign(String name, String value);

	void assign(int priority);

	String parameter(String name);

	Rule rule();

	int priority();

}
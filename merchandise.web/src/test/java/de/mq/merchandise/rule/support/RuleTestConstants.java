package de.mq.merchandise.rule.support;

import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.rule.Rule;
import de.mq.merchandise.util.EntityUtil;

public class RuleTestConstants {
	
	public static final  Rule rule() {
		final Rule rule =  new RuleImpl(EntityUtil.create(CustomerImpl.class), "Artist hot score calculation");
		ReflectionTestUtils.setField(rule, "id", (long) (Math.random() * 1e12 ));
		return rule;
	}

}
